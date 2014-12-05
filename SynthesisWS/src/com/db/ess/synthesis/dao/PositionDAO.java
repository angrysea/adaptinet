package com.db.ess.synthesis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.TaskHelper;
import com.db.ess.synthesis.dvo.Exceptions;
import com.db.ess.synthesis.dvo.GetPositionExceptionsResponse;
import com.db.ess.synthesis.dvo.GetPositionRequest;
import com.db.ess.synthesis.dvo.GetPositionResponse;
import com.db.ess.synthesis.dvo.GetSwapExceptionsRequest;
import com.db.ess.synthesis.dvo.GetSwapExceptionsResponse;
import com.db.ess.synthesis.dvo.Position;
import com.db.ess.synthesis.dvo.PositionException;
import com.db.ess.synthesis.dvo.SwapException;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;
import com.db.ess.synthesis.util.SynthesisConstants;
import com.db.ess.synthesis.util.cache.CachedBasis;
import com.db.ess.synthesis.util.cache.CachedBook;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class PositionDAO extends SynthesisBaseDAO{
	private static final Logger logger = Logger.getLogger(PositionDAO.class
			.getName());
	private static final String GET_POSITIONS = "{ call dbo.SYN_GetPositions (";
	private static final String GET_EXCEPTIONS = "{ call dbo.SYN_GetSwapException (";

	public GetPositionResponse getPositions(GetPositionRequest request)
			throws Exception {
		logger.info("Inside getPositions(), request received: "+request);
		GetPositionResponse response = new GetPositionResponse();
		int location = request.getlocation();

		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getPositions(request, ESSLocation.LONDON, response);
			break;
			
		case ESSLocation.NEWYORK:
			getPositions(request, ESSLocation.NEWYORK, response);
			break;

		case ESSLocation.GLOBAL:
			getPositions(request, ESSLocation.LONDON, response);
			getPositions(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}
	public GetSwapExceptionsResponse getPositionExceptions(
			GetSwapExceptionsRequest request) throws Exception {
		logger.info("Inside getPositionExceptions(), request received: " + request);
		GetSwapExceptionsResponse response = new GetSwapExceptionsResponse();
		int location = request.getlocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getPositionExceptions(request, ESSLocation.LONDON, response);
			break;

		case ESSLocation.NEWYORK:
			getPositionExceptions(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public GetPositionResponse getPositions(GetPositionRequest request,
			int location, GetPositionResponse response) throws Exception {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		String query = createQueryString(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			cstmt.setQueryTimeout(300); /* timeout after 5 minutes */
			setParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			int count = 0;
			while (rs.next()) {
				if (count > SynthesisConstants.MAX_RESULT_SET_SIZE) {
					response.setreturnResponse(getResultSetOverFlowResponse());
					break;
				}
				Position position = populatePosition(rs,location,request.getasOfDate());
				response.setpositions(position);
				count++;
			}
			
			logger.info("Found ["+count+"] Positions For the given search criteria.");
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}

		return response;
	}
	
	public void getPositionExceptions(GetSwapExceptionsRequest request,
			int location, GetSwapExceptionsResponse response) throws Exception {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		String query = createPositionExceptionQueryString(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setPositionExceptionParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			SwapException swapException = new SwapException();
			while (rs.next()) {
				Exceptions cal = populatePositionException(rs);
				if (1 == cal.getspread()) {
					swapException.setspreadExceptions(cal);
				} else {
					swapException.setrateExceptions(cal);
				}
			}
			response.setswapException(swapException);
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
	}
	
	private String createPositionExceptionQueryString(
			GetSwapExceptionsRequest request) {
		StringBuilder qb = new StringBuilder(GET_EXCEPTIONS);
		StringBuilder log = new StringBuilder(GET_EXCEPTIONS);

		if (request.getswapNum() > 0) {
			qb.append("? ) }");
			log.append("@SwapNum = " + request.getswapNum());
			log.append(") } ");
			logger.info("Executing Query"+log.toString());
		}
		return qb.toString();
	}
	private void setPositionExceptionParameters(GetSwapExceptionsRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getswapNum());
	}
	private Exceptions populatePositionException(ResultSet rs) throws SQLException {
		Exceptions exception = new Exceptions();
		int i = 0;

		int instrId = rs.getInt(++i);
		int countryId = rs.getInt(++i);
		int institutionId = rs.getInt(++i);
		int custid = rs.getInt(++i);
		int swapNum = rs.getInt(++i);
		String enteredBy = rs.getString(++i);
		double rate = rs.getDouble(++i);

		Timestamp startDateTimeStamp = rs.getTimestamp(++i);
		Date startDate = startDateTimeStamp == null ? null : new Date(
				startDateTimeStamp.getTime());
		Timestamp endDateInputTimeStamp = rs.getTimestamp(++i);
		Date endDateInput = endDateInputTimeStamp == null ? null : new Date(
				endDateInputTimeStamp.getTime());
		Timestamp enterTimeTimeStamp = rs.getTimestamp(++i);
		Date enterTime = enterTimeTimeStamp == null ? null : new Date(
				enterTimeTimeStamp.getTime());

		int exceptionId = rs.getInt(++i);
		int spread = rs.getInt(++i);
		int swapId = rs.getInt(++i);
		String longShort = rs.getString(++i);
		int eventId = rs.getInt(++i);
		String requestor = rs.getString(++i);
		int reasonId = rs.getInt(++i);
		String description = rs.getString(++i);
		String reasonComment = rs.getString(++i);
		String bookSeries = rs.getString(++i);
		String actionedByUser = rs.getString(++i);
		String type = rs.getString(++i);

		exception.setstartDate(startDate);
		exception.setendDate(endDateInput);
		exception.setenterTime(enterTime);
		exception.setswapNum(swapNum);
		exception.settype(type);
		exception.setuserName(enteredBy);
		exception.setspread(spread);
		exception.settype(type);
		return exception;
	}


	private Position populatePosition(ResultSet rs, int location,Date asOfDate) throws SQLException {
		Position position = new Position();
		position.setessLocation(location);
		position.setasOfDate(asOfDate);
		int i = 0;
		position.setswapNum(rs.getInt(++i));
		
		Timestamp swapMaturityTimeStamp = rs.getTimestamp(++i);
		Date swapMaturity = swapMaturityTimeStamp == null ? null : new Date(
				swapMaturityTimeStamp.getTime());
		position.setswapMaturity(swapMaturity);
		
		position.setcustomerName(rs.getString(++i));
		position.setcustomerId(rs.getInt(++i));
		position.setinstitutionName(rs.getString(++i));
		position.setinstitutionId(rs.getInt(++i));
		position.settradePartyName(rs.getString(++i));
		int SwapCcy = rs.getInt(++i);
		position.setavgBaseCost(rs.getDouble(++i));
		position.setmktPrice(rs.getDouble(++i));
		position.setresetPriceBase(rs.getDouble(++i));
		position.setavgCost(rs.getDouble(++i));
		position.setqty(rs.getDouble(++i) * -1);
		position.setcurrentSpread(rs.getDouble(++i));
		position.setsprExcType(rs.getString(++i));
		
		Timestamp sprExcStartTimeStamp = rs.getTimestamp(++i);
		Date sprExcStart = sprExcStartTimeStamp == null ? null : new Date(
				sprExcStartTimeStamp.getTime());
		position.setsprExcStart(sprExcStart);
		
		Timestamp sprExcEndTimeStamp = rs.getTimestamp(++i);
		Date sprExcEnd = sprExcEndTimeStamp == null ? null : new Date(
				sprExcEndTimeStamp.getTime());
		position.setsprExcEnd(sprExcEnd);
				
		Timestamp sprExcTimeStamp = rs.getTimestamp(++i);
		Date sprExc = sprExcTimeStamp == null ? null : new Date(
				sprExcTimeStamp.getTime());
		position.setsprExcTime(sprExc);
		position.setsprExcUser(rs.getString(++i));
		position.setsprExcReason(rs.getString(++i));
		position.setsprExcComment(rs.getString(++i));
		position.setsprExcBookSeries(rs.getString(++i));
		position.setsprExcCountry(rs.getString(++i));
		position.setsprExcInstitution(rs.getString(++i));
		position.setsprExcLegalEntity(rs.getString(++i));
		position.setsprSwapNumber(rs.getInt(++i));
		position.setsprExcTicker(rs.getString(++i));
		position.setcurrentRate(rs.getDouble(++i));
		position.setrateExcType(rs.getString(++i));
		
		Timestamp rateExcStartTimeStamp = rs.getTimestamp(++i);
		Date rateExcStart = rateExcStartTimeStamp == null ? null : new Date(
				rateExcStartTimeStamp.getTime());
		position.setrateExcStart(rateExcStart);
				
		Timestamp rateExcEndTimeStamp = rs.getTimestamp(++i);
		Date rateExcEnd = rateExcEndTimeStamp == null ? null : new Date(
				rateExcEndTimeStamp.getTime());
		position.setrateExcEnd(rateExcEnd);
				
		Timestamp rateExcTimeStamp = rs.getTimestamp(++i);
		Date rateExcTime = rateExcTimeStamp == null ? null : new Date(
				rateExcTimeStamp.getTime());
		position.setrateExcTime(rateExcTime);
		position.setrateExcUser(rs.getString(++i));
		position.setrateExcReason(rs.getString(++i));
		position.setrateExcComment(rs.getString(++i));
		position.setrateExcBookSeries(rs.getString(++i));
		position.setrateExcCountry(rs.getString(++i));
		position.setrateExcInstitution(rs.getString(++i));
		position.setrateExcLegalEntity(rs.getString(++i));
		position.setrateSwapNumber(rs.getInt(++i));
		position.setrateExcTicker(rs.getString(++i));
		position.setfixedVariableRate(rs.getInt(++i));
		Timestamp prevIntResetTimeStamp = rs.getTimestamp(++i);
		Date prevIntReset = prevIntResetTimeStamp == null ? null : new Date(
				prevIntResetTimeStamp.getTime());
		position.setprevIntResetDate(prevIntReset);
				
		Timestamp nextIntResetTimeStamp = rs.getTimestamp(++i);
		Date nextIntReset = nextIntResetTimeStamp == null ? null : new Date(
				nextIntResetTimeStamp.getTime());
		position.setnextIntResetDate(nextIntReset);
			
		Timestamp prevEqResetTimeStamp = rs.getTimestamp(++i);
		Date prevEqReset = prevEqResetTimeStamp == null ? null : new Date(
				prevEqResetTimeStamp.getTime());
		position.setprevEqResetDate(prevEqReset);
			
		Timestamp nextEqResetTimeStamp = rs.getTimestamp(++i);
		Date nextEqReset = nextEqResetTimeStamp == null ? null : new Date(
				nextEqResetTimeStamp.getTime());
		position.setnextEqResetDate(nextEqReset);
	
		int fundingSwap = rs.getInt(++i);
		position.setbookId(rs.getInt(++i));
		position.setswapId(rs.getInt(++i));
		position.setinstrId(rs.getInt(++i));
		position.seteqLegId(rs.getInt(++i));
		position.setintLegId(rs.getInt(++i));
		position.setsprExcId(rs.getInt(++i));
		position.setrateExcId(rs.getInt(++i));
		position.setalgoValue(rs.getDouble(++i));
		position.setalgoCharge(rs.getDouble(++i));
		position.setalgoCcy(rs.getString(++i));
		position.setnyCostOfFunds(rs.getDouble(++i));
		
		Timestamp tradeTimeStamp = rs.getTimestamp(++i);
		Date tradeDate = tradeTimeStamp == null ? null : new Date(
				tradeTimeStamp.getTime());
		position.settradeDate(tradeDate);
			
		Timestamp effDateTimeStamp = rs.getTimestamp(++i);
		Date effDate = effDateTimeStamp == null ? null : new Date(
				effDateTimeStamp.getTime());
		position.seteffDate(effDate);
			
		Timestamp termDateTimeStamp = rs.getTimestamp(++i);
		Date termDate = termDateTimeStamp == null ? null : new Date(
				termDateTimeStamp.getTime());
		position.settermDate(termDate);
			
		Timestamp finalEquityValTimeStamp = rs.getTimestamp(++i);
		Date finalEquityValDate = finalEquityValTimeStamp == null ? null : new Date(
				finalEquityValTimeStamp.getTime());
		position.setfinalEquityValuationDate(finalEquityValDate);
	
		position.seteqPayFreq(rs.getString(++i));
		position.setintComment(rs.getString(++i));
		position.setextComment(rs.getString(++i));
		position.setholdingPeriodDays(rs.getInt(++i));
		position.setqtyLongTradeable(rs.getDouble(++i));
		
		Timestamp exDateTimeStamp = rs.getTimestamp(++i);
		Date exDate = exDateTimeStamp == null ? null : new Date(
				exDateTimeStamp.getTime());
		position.setexDate(exDate);
			
		Timestamp recordDateTimeStamp = rs.getTimestamp(++i);
		Date recordDate = recordDateTimeStamp == null ? null : new Date(
				recordDateTimeStamp.getTime());
		position.setrecordDate(recordDate);
				
		Timestamp paymentDateTimeStamp = rs.getTimestamp(++i);
		Date paymentDate = paymentDateTimeStamp == null ? null : new Date(
				paymentDateTimeStamp.getTime());
		position.setpaymentDate(paymentDate);
		
		position.setavgCostBasePrevCob(rs.getDouble(++i));
		position.setavgCostPayPrevCob(rs.getDouble(++i));
		position.setqtyPrevCob(rs.getDouble(++i));
		position.setticker(rs.getString(++i));
		position.setCUSIP(rs.getString(++i));
		position.setSEDOL(rs.getString(++i));
		position.setinstrumentdescription(rs.getString(++i));
		int instrumentTypeId = rs.getInt(++i);
		int countryId = rs.getInt(++i);
		int tradeCcyId = rs.getInt(++i);
		int settleCcyId = rs.getInt(++i);
		position.setISIN(rs.getString(++i));
		position.setmultiplier(rs.getDouble(++i));
		position.setintTicker(rs.getString(++i));
		
		CachedCurrency swapCurrency = RefCache.getCurrency(SwapCcy,location);
		if (swapCurrency != null) {
			position.setswapCcy(swapCurrency.getSwiftCode());
			CachedBasis basis = RefCache.getBasisDomain(swapCurrency
					.getSwapBasis(),location);
			if (basis != null) {
				position.setswapCcySwapBasisDays(basis.getDays());
			}
		}
		CachedBook book = RefCache.getBook(position.getbookId(),location);
		if(book != null){
			position.setbookname(book.getName());
			position.setaccountname(book.getAccountname());
			position.setdeskname(book.getDeskname());
			position.setbusinessunitname(book.getBusinessUnitName());
		}else{
			logger.warn("Book object found null in Ref Cache for bookId : "+position.getbookId());
		}
		position.setinstrumenttype(RefCache.getFITypeDomain(instrumentTypeId,location));
		position.setcountrycode(RefCache.getCountry(countryId,location));
		CachedCurrency tradeCurrency = RefCache.getCurrency(tradeCcyId,location);
		if (tradeCurrency != null) {
			position.settradecurrency(tradeCurrency.getSwiftCode());
		}
		CachedCurrency settleCurrency = RefCache.getCurrency(settleCcyId,location);
		if (settleCurrency != null) {
			position.setpaymentcurrency(settleCurrency.getSwiftCode());
		}

		position.setstrategy(RefCache.getStrategyDomain(fundingSwap,location));
		position.setdailyCOFBlend(rs.getDouble(++i));
		position.setdailyFunding(rs.getDouble(++i));
		position.setdiffESSAndAlgo(rs.getDouble(++i));
		position.setdailyClientFinancingCharge(rs.getDouble(++i));
		position.setdailyNYPnL(rs.getDouble(++i));
		position.setdailyESSCharge(rs.getDouble(++i));
		position.setntlRpt(rs.getDouble(++i));
		position.setlongNtlRpt(rs.getDouble(++i));
		position.setshortNtlRpt(rs.getDouble(++i));
		position.setMktPricePay(rs.getDouble(++i));
		position.setresetPricePay(rs.getDouble(++i));
		
		position.setmktPricePrevCob(rs.getDouble(++i));
		position.setfxRateBase(rs.getDouble(++i));
		position.setfxRatePay(rs.getDouble(++i));
		position.setfxRatePrevBase(rs.getDouble(++i));
		position.setfxRatePrevPay(rs.getDouble(++i));
		position.setfxRateInAlgoCcy(rs.getDouble(++i));
		position.setisPrimary(rs.getString(++i).equals("true")?true:false);

		return position;
	}

	private String createQueryString(GetPositionRequest request) {

		StringBuilder qb = new StringBuilder(GET_POSITIONS);
		StringBuilder log = new StringBuilder(GET_POSITIONS);

		qb.append("@UserId = ?");
		
		qb.append(", @OpenOnly = ?");
		log.append(", @OpenOnly = " + request.getopenOnly());
		
		if (request.getpositionType() != null) {
			qb.append(", @PositionType = ?");
			log.append(", @PositionType = " + request.getpositionType());
		}
		if (request.getasOfDate() != null) {
			qb.append(", @AsOfDate = ?");
			log.append(", @AsOfDate = " + request.getasOfDate());
		}
		if (request.geteffectiveDate() != null) {
			qb.append(", @EffectiveDate = ?");
			log.append(", @EffectiveDate = " + request.geteffectiveDate());
		}
		if (request.gettermDate() != null) {
			qb.append(", @TermDate = ?");
			log.append(", @TermDate = " + request.gettermDate());
		}
		if (request.gettradeDate() != null) {
			qb.append(", @TradeDate = ?");
			log.append(", @TradeDate = " + request.gettradeDate());
		}
		if (request.getswapNum() != null) {
			qb.append(", @SwapNum = ?");
			String swapNum = request.getswapNum();
			String swapNumQuery = getWildCardSearchInt(swapNum,"s.swapNum");
			log.append(", @SwapNum = " + swapNumQuery);
		}
		if (request.getsecCodeType() != null && request.getsecCodeType().length() > 0) {
			qb.append(", @SecCodeType = ?");
			log.append(", @SecCodeType = " + request.getsecCodeType());
		}
		if (request.getsecCode() != null && request.getsecCode().length() > 0) {
			qb.append(", @SecCode = ?");
			log.append(", @SecCode = " + TaskHelper.createWhere(request.getsecCode(), request.getsecCodeType()));
		}
		if (request.getlegalEntity()!= null	&& request.getlegalEntity().length() > 0) {
			qb.append(", @Customer = ?");
			String legalEntity = request.getlegalEntity();
			//String legalEntityQuery = getWildCardSearchString(legalEntity,"upper(le.name)");
			String legalEntityQuery = TaskHelper.convert(legalEntity,"upper(le.name)",false);
			log.append(", @Customer = " + legalEntityQuery);
		}
		if (request.getinstitution() != null && request.getinstitution().length() > 0) {
			qb.append(", @Institution = ?");
			String institution = request.getinstitution();
			//String institutionQuery = getWildCardSearchString(institution,"upper(n.name)");
			String institutionQuery = TaskHelper.convert(institution,"upper(n.name)",false);
			log.append(", @Institution = " + institutionQuery);
		}
		if (request.getbook()!= null && request.getbook().length() > 0) {
			qb.append(", @Book = ?");
			String book = request.getbook();
			String bookQuery = getWildCardSearchString(book,"upper(b.name)");
			log.append(", @Book = " + bookQuery);
		}
		if (request.getaccount()!= null && request.getaccount().length() > 0) {
			qb.append(", @Account = ?");
			String account = request.getaccount();
			String accountQuery = getWildCardSearchString(account,"upper(a.name)");
			log.append(", @Account = " + accountQuery);
		}
		if (request.getdesk() != null && request.getdesk().length() > 0) {
			qb.append(", @Desk = ?");
			String desk = request.getdesk();
			String deskQuery = getWildCardSearchString(desk,"upper(d.name)");
			log.append(", @Desk = " + deskQuery);
		}
		if (request.getbusinessUnit() != null && request.getbusinessUnit().length() > 0) {
			qb.append(", @BusinessUnit = ?");
			String businessUnit = request.getbusinessUnit();
			String businessUnitQuery = getWildCardSearchString(businessUnit,"upper(bu.name)");
			log.append(", @BusinessUnit = " + businessUnitQuery);
		}
		if (request.getswapName() != null && request.getswapName().length() > 0) {
			qb.append(", @SwapName = ?");
			log.append(", @SwapName = " + request.getswapName());
		}
		if (request.gettradeParty() != null && request.gettradeParty().length() > 0) {
			qb.append(", @TradePartyName = ?");
			log.append(", @TradePartyName = " + request.gettradeParty());
		}
		if (request.getdescription() != null && request.getdescription().length() > 0) {
			qb.append(", @Description = ?");
			log.append(", @Description = " + request.getdescription());
		}
		if (request.getsearchAll() > 0) {
			qb.append(", @SearchAll = ?");
			log.append(", @SearchAll = " + request.getsearchAll());
		}
		if (request.getinstrCcy() != null && request.getinstrCcy().length() > 0) {
			qb.append(", @InstrCcy = ?");
			String instrCcy = request.getinstrCcy();
			String instrCcyQuery = getWildCardSearchString(instrCcy,"swiftCode");
			log.append(", @InstrCcy = " + instrCcyQuery);
		}
		if (request.getpayCcy() != null && request.getpayCcy().length() > 0) {
			qb.append(", @PayCcy = ?");
			String payCcy = request.getpayCcy();
			String payCcyQuery = getWildCardSearchString(payCcy,"swiftCode");
			log.append(", @PayCcy = " + payCcyQuery);
		}
		if (request.getinstrDesc() != null && request.getinstrDesc().length() > 0) {
			qb.append(", @InstrDesc = ?");
			String instrDesc = request.getinstrDesc();
			String instrDescQuery = getWildCardSearchString(instrDesc,"upper(i.description)");
			log.append(", @InstrDesc = " + instrDescQuery);
		}
		if (request.getexceptionID() != null && request.getexceptionID().length() > 0) {
			qb.append(", @ExceptionID = ?");
			log.append(", @ExceptionID = " + request.getexceptionID());
		}
		qb.append(") } ");
		log.append(") } ");

		logger.info("Executing query:"+log.toString());

		return qb.toString();
	}

	private void setParameters(GetPositionRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getuserId());
		cstmt.setInt(++i, request.getopenOnly());
		
		if (request.getpositionType() != null
				&& request.getpositionType().length() > 0)
			cstmt.setString(++i, request.getpositionType());
		if (request.getasOfDate() != null)
			cstmt.setDate(++i, new java.sql.Date(request.getasOfDate()
					.getTime()));
		if (request.geteffectiveDate() != null)
			cstmt.setDate(++i, new java.sql.Date(request.geteffectiveDate()
					.getTime()));
		if (request.geteffectiveDate() != null)
			cstmt.setDate(++i, new java.sql.Date(request.geteffectiveDate()
					.getTime()));
		if (request.gettermDate() != null)
			cstmt.setDate(++i, new java.sql.Date(request.gettermDate()
					.getTime()));
		if (request.gettradeDate() != null)
			cstmt.setDate(++i, new java.sql.Date(request.gettradeDate()
					.getTime()));
		if (request.getswapNum()!= null){
			String swapNum = request.getswapNum();
			String swapNumQuery = getWildCardSearchInt(swapNum,"s.swapNum");
			logger.info("SwapNumQuery from getWildCardSearchInt : "+swapNumQuery);
			cstmt.setString(++i, swapNumQuery);
		}
		if (request.getsecCodeType() != null
				&& request.getsecCodeType().length() > 0)
			cstmt.setString(++i, request.getsecCodeType());
		if (request.getsecCode() != null
				&& request.getsecCode().length() > 0){
			    String secCodeQuery = TaskHelper.createWhere(request.getsecCode(), request.getsecCodeType());
			    logger.info("secCodeQuery from TaskHelper.createWhere : "+secCodeQuery);
				cstmt.setString(++i, secCodeQuery);
		}
		if (request.getlegalEntity()!= null){
			String legalEntity = request.getlegalEntity();
			String legalEntityQuery = TaskHelper.convert(legalEntity,"upper(le.name)",false);
			logger.info("legalEntityQuery from TaskHelper.convertWithComma : "+legalEntityQuery);
			cstmt.setString(++i, legalEntityQuery);
		}
		if (request.getinstitution()!= null){
			String institution = request.getinstitution();
			String institutionQuery = TaskHelper.convert(institution,"upper(n.name)",false);
			logger.info("institutionQuery from TaskHelper.convertWithComma : "+institutionQuery);
			cstmt.setString(++i, institutionQuery);
		}
		if (request.getbook()!= null){
			String book = request.getbook();
			String bookQuery = getWildCardSearchString(book,"upper(b.name)");
			logger.info("bookQuery from getWildCardSearchString : "+bookQuery);
			cstmt.setString(++i, bookQuery);
		}
		if (request.getaccount()!= null){
			String account = request.getaccount();
			String accountQuery = getWildCardSearchString(account,"upper(a.name)");
			logger.info("accountQuery from getWildCardSearchString : "+accountQuery);
			cstmt.setString(++i, accountQuery);
		}
		if (request.getdesk()!= null){
			String desk = request.getdesk();
			String deskQuery = getWildCardSearchString(desk,"upper(d.name)");
			logger.info("deskQuery from getWildCardSearchString : "+deskQuery);
			cstmt.setString(++i, deskQuery);
		}
		if (request.getbusinessUnit()!= null){
			String businessUnit = request.getbusinessUnit();
			String businessUnitQuery = getWildCardSearchString(businessUnit,"upper(bu.name)");
			logger.info("businessUnitQuery from getWildCardSearchString : "+businessUnitQuery);
			cstmt.setString(++i, businessUnitQuery);
		}
		if (request.getswapName() != null && request.getswapName().length() > 0)
			cstmt.setString(++i, request.getswapName());
		if (request.gettradeParty() != null
				&& request.gettradeParty().length() > 0)
			cstmt.setString(++i, request.gettradeParty());
		if (request.getdescription() != null
				&& request.getdescription().length() > 0)
			cstmt.setString(++i, request.getdescription());
		if (request.getsearchAll() >0)
			cstmt.setInt(++i, request.getsearchAll());
		if (request.getinstrCcy() != null
				&& request.getinstrCcy().length() > 0){
			String instrCcy = request.getinstrCcy();
			String instrCcyQuery = getWildCardSearchString(instrCcy,"swiftCode");
			logger.info("instrCcyQuery from getWildCardSearchString : "+instrCcyQuery);
			cstmt.setString(++i,instrCcyQuery);
		}
		if (request.getpayCcy() != null
				&& request.getpayCcy().length() > 0){
			String payCcy = request.getpayCcy();
			String payCcyQuery = getWildCardSearchString(payCcy,"swiftCode");
			logger.info("payCcyQuery from getWildCardSearchString : "+payCcyQuery);
			cstmt.setString(++i,payCcyQuery);
		}
		if (request.getinstrDesc() != null
				&& request.getinstrDesc().length() > 0){
			String instrDesc = request.getinstrDesc();
			String instrDescQuery = getWildCardSearchString(instrDesc,"upper(i.description)");
			logger.info("instrDescQuery from getWildCardSearchString : "+instrDescQuery);
			cstmt.setString(++i,instrDescQuery);
		}
		if (request.getexceptionID() != null
				&& request.getexceptionID().length() > 0){
			cstmt.setString(++i,request.getexceptionID());
		}
	}
}
