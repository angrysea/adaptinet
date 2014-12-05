package com.db.ess.synthesis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.TaskHelper;
import com.db.ess.synthesis.dvo.GetTradeDividendsRequest;
import com.db.ess.synthesis.dvo.GetTradeDividendsResponse;
import com.db.ess.synthesis.dvo.GetTradeRequest;
import com.db.ess.synthesis.dvo.GetTradeResponse;
import com.db.ess.synthesis.dvo.Trade;
import com.db.ess.synthesis.dvo.TradeDividend;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;
import com.db.ess.synthesis.util.SynthesisConstants;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class TradeDAO extends SynthesisBaseDAO{

	private static final Logger logger = Logger.getLogger(TradeDAO.class
			.getName());
	private static final String GET_TRADE = "{ call dbo.SYN_GetTrades (";
	
	private static final String GET_TRADE_DIVIDENDS = "{ call dbo.SYN_GetTradeDividends (";

	public GetTradeResponse getTrades(GetTradeRequest request) throws Exception {
		logger.info("Inside getTrades(), request received: "+request); 
		GetTradeResponse response = new GetTradeResponse();
		int location = request.getlocation();

		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

		case ESSLocation.LONDON:
			getTrades(request, ESSLocation.LONDON, response);
			break;
			
		case ESSLocation.NEWYORK:
			getTrades(request, ESSLocation.NEWYORK, response);
			break;

		case ESSLocation.GLOBAL:
			getTrades(request, ESSLocation.LONDON, response);
			getTrades(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public GetTradeResponse getTrades(GetTradeRequest request, int location,
			GetTradeResponse response) throws Exception {

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
				Trade position = populateTrade(rs,location);
				response.settrades(position);
				count++;
			}
			logger.info("Found ["+count+"] Trades For the given search criteria.");
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			ex.printStackTrace();
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}

		return response;
	}

	public GetTradeDividendsResponse getTradeDividends(
			GetTradeDividendsRequest request) throws Exception{
		logger.info("Inside getTradeDividends(), request received: "+request); 
		GetTradeDividendsResponse response = new GetTradeDividendsResponse();
		
		int location = request.getlocation();

		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getTradeDividends(request, ESSLocation.LONDON, response);
			break;
			
		case ESSLocation.NEWYORK:
			getTradeDividends(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
		
	}
	
	public GetTradeDividendsResponse getTradeDividends(
			GetTradeDividendsRequest request, int location,
			GetTradeDividendsResponse response) throws Exception {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		String query = createDividendQueryString(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setDividendParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				TradeDividend dividend = populateTradeDividend(rs,location);
				response.setdividends(dividend);
			}

		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			ex.printStackTrace();
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
		return response;
	}
	
	private TradeDividend populateTradeDividend(ResultSet rs,int location) throws SQLException {
		TradeDividend tradeDividend = new TradeDividend();
		int i = 0;

		Timestamp exDateTimeStamp = rs.getTimestamp(++i);
		Date exDate = exDateTimeStamp == null ? null : new Date(
				exDateTimeStamp.getTime());
		
		Timestamp announceDateTimeStamp = rs.getTimestamp(++i);
		Date announceDate = announceDateTimeStamp == null ? null : new Date(
				announceDateTimeStamp.getTime());
		
		Timestamp recordDateTimeStamp = rs.getTimestamp(++i);
		Date recordDate = recordDateTimeStamp == null ? null : new Date(
				recordDateTimeStamp.getTime());
		
		Timestamp payDateTimeStamp = rs.getTimestamp(++i);
		Date payDate = payDateTimeStamp == null ? null : new Date(
				payDateTimeStamp.getTime());
		
		double divRate = rs.getDouble(++i);
		double altRate = rs.getDouble(++i);
		
		String currency = rs.getString(++i);
		int divAltCcy = rs.getInt(++i);
		double divAmount = rs.getDouble(++i);
		double divAltAmount = rs.getDouble(++i);
		double exception = rs.getDouble(++i);
		int status = rs.getInt(++i);
		String ticker = rs.getString(++i);
		int classType = rs.getInt(++i);
		double taxRates = rs.getDouble(++i);
		String SDMStatus = rs.getString(++i);
		String ESSStatus = rs.getString(++i);
		
		tradeDividend.setticker(ticker);
		tradeDividend.setexDate(exDate);
		tradeDividend.setrecordDate(recordDate);
		tradeDividend.setpayDate(payDate);
		tradeDividend.setdivRate(divRate);
		tradeDividend.setaltRate(altRate);
		tradeDividend.setdivCcy(currency);
		tradeDividend.setdivAmount(divAmount);
		tradeDividend.setexception(exception);
		CachedCurrency divAltCurrency = null;
		if((divAltCurrency = RefCache.getCurrency(divAltCcy,location))!=null)
			tradeDividend.setdivAltCcy(divAltCurrency.getSwiftCode());
		
		return tradeDividend;
	}
	
	private Trade populateTrade(ResultSet rs,int location) throws SQLException {
		Trade trade = new Trade();
		
		int i = 0;
		trade.setessLocation(location);
		trade.setaccountName(rs.getString(++i));
		trade.setamortizedAmount(rs.getDouble(++i));
		trade.setpayAmount(rs.getDouble(++i));
		trade.setbaseAmount(rs.getDouble(++i));
		trade.setbookName(rs.getString(++i));
		trade.setbusinessUnitName(rs.getString(++i));
		int buySell = rs.getInt(++i);
		trade.setcloseNotionalBaseCcy(rs.getDouble(++i));
		trade.setcloseNotionalPayCcy(rs.getDouble(++i));
		trade.setcloseQuantity(rs.getDouble(++i));
		
		double flatFee = rs.getDouble(++i);
		int commPayType = rs.getInt(++i);
		String commpayTypecode = rs.getString(++i);
		trade.setcommPayTypeCode(RefCache.getCommissionPayDomain(commPayType,location));
		trade.setcommRate(rs.getDouble(++i));
		int commType = rs.getInt(++i);
		String commTypeCode = rs.getString(++i);
		trade.setcommTypeCode(RefCache.getCommissionTypeDomain(commType,location));  
		trade.setcorporateAction(rs.getString(++i));  
		String coutryCode = rs.getString(++i);       
		trade.setCUSIP(rs.getString(++i));  
		trade.setcustomerId(rs.getInt(++i));
		trade.setcustomerName(RefCache.getLegalEntity(rs.getInt(i),location));
		trade.setdeskName(rs.getString(++i));
		Timestamp enterTimeStamp = rs.getTimestamp(++i);
		Date enterTime = enterTimeStamp == null ? null : new Date(
				enterTimeStamp.getTime());
		trade.setenterTime(enterTime);
		int enterbyUserId = rs.getInt(++i);
		trade.setenteredby(rs.getString(++i));
		trade.seteventId(rs.getInt(++i));
		int eventTypeId = rs.getInt(++i);
		trade.seteventTypeCode(rs.getString(++i));
		trade.setfxRate(rs.getDouble(++i));
		trade.sethedgeBasePrice(rs.getDouble(++i));
		trade.sethedgePayPrice(rs.getDouble(++i)); // this step sets up nothing but need to keep it as it increments the counter
		//trade.sethedgePayPrice(trade.gethedgeBasePrice()*(1/(trade.getfxRate()==0?1:trade.getfxRate())));
		trade.sethedgePayPrice(trade.gethedgeBasePrice()*(trade.getfxRate()==0?1:trade.getfxRate()));
		trade.setinstitutionId (rs.getInt(++i));
		trade.setinstitutionName(RefCache.getInstitution(rs.getInt(i),location));
		int instrumentCcyId = rs.getInt(++i);
		trade.setinstrumentCcyCode(rs.getString(++i));
		trade.setinstrumentId(rs.getInt(++i));
		trade.setinstrumentType(rs.getString(++i));
		trade.setinstrumentDesc(rs.getString(++i));
		trade.setintRate(rs.getDouble(++i));
		trade.setISIN(rs.getString(++i));
		trade.setlongShort(rs.getInt(++i));
		trade.setlegId(rs.getInt(++i));
		trade.setlegNumber(rs.getInt(++i));
		String liquiditySummary = rs.getString(++i);
		trade.setlongNotionalBaseCcy(rs.getDouble(++i));
		trade.setlongNotionalPayCcy(rs.getDouble(++i));
		trade.setlongQuantity(rs.getDouble(++i));
		trade.setmultiplier(rs.getInt(++i));
		trade.setnotionalBaseCcy(rs.getDouble(++i));
		trade.setnotionalPayCcy(rs.getDouble(++i));
		trade.setopenNotionalBaseCcy(rs.getDouble(++i));
		trade.setopenNotionalPayCcy(rs.getDouble(++i));
		trade.setopenQuantity(rs.getDouble(++i));
		trade.setorigswapNumber(rs.getInt(++i));
		trade.setpriceBaseCcy(rs.getDouble(++i));
		trade.setpricePayCcy(rs.getDouble(++i));
		trade.setquantity(rs.getDouble(++i));
		trade.setregion(rs.getString(++i));
		trade.setricSuffix(rs.getString(++i));
		trade.setSEDOL(rs.getString(++i));
		
		Timestamp settleTimeStamp = rs.getTimestamp(++i);
		Date settleDate = settleTimeStamp == null ? null : new Date(
				settleTimeStamp.getTime());
		trade.setsettleDate(settleDate);
		trade.setshortNotionalBaseCcy(rs.getDouble(++i));
		trade.setshortNotionalPayCcy(rs.getDouble(++i));
		trade.setshortQuantity(rs.getDouble(++i)); 
		trade.setspreadException(rs.getDouble(++i)); 
		trade.setSTARSID(rs.getInt(++i)); 
		trade.setstrategy(rs.getString(++i)); 
		int swapCcyId = rs.getInt(++i);
		CachedCurrency swapCurrency;
		if((swapCurrency=RefCache.getCurrency(swapCcyId,location))!=null ){
			trade.setswapCcy(swapCurrency.getSwiftCode());
		}
	
		trade.setswapId(rs.getInt(++i));
		trade.setswapNumber(rs.getInt(++i));
		
		Timestamp swapMaturityTimeStamp = rs.getTimestamp(++i);
		Date swapMaturityDate = swapMaturityTimeStamp == null ? null : new Date(
				swapMaturityTimeStamp.getTime());
		trade.setswapMaturity(swapMaturityDate);   
		
		trade.setticker(rs.getString(++i));
		trade.settracerId(rs.getInt(++i));
		
		Timestamp tradeTimeStamp = rs.getTimestamp(++i);
		Date tradeDate = tradeTimeStamp == null ? null : new Date(
				tradeTimeStamp.getTime());
		trade.settradeDate(tradeDate);   
		trade.settradeId(rs.getInt(++i));
		trade.setprofitCenter(rs.getString(++i));
		trade.settaxlotMaturityDate(rs.getDate(++i));
		int primaryExchangeId =	rs.getInt(++i);
		trade.setprimaryExchangeCode(rs.getString(++i));
		
		Timestamp lastexTimeStamp = rs.getTimestamp(++i);
		Date lastexDate = lastexTimeStamp == null ? null : new Date(
				lastexTimeStamp.getTime());
		trade.setlastexDate(lastexDate); 
		
		trade.setcounterParty(rs.getString(++i));
		trade.settradeSource(rs.getString(++i));
		trade.setinterestTicker(rs.getString(++i));
		trade.setcurrentSpread(rs.getDouble(++i));
		trade.setcurrentRate(rs.getDouble(++i));
		trade.settradePartyId ( rs.getInt(++i));
		trade.settradePartyName(RefCache.getLegalEntity( rs.getInt(i),location));
		int fundingSwap = rs.getInt(++i);
		int bookId = rs.getInt(++i);
		int countyId = rs.getInt(++i);
		trade.setcountryCode(RefCache.getCountry(rs.getInt(i),location));
		double netPrice = rs.getDouble(++i);
		trade.setnetPrice(netPrice);
		
		int tradeCcyId = rs.getInt(++i);
		CachedCurrency tradeCcy = RefCache.getCurrency(tradeCcyId, location);
		if (tradeCcy != null) {
		    trade.settradeCcy(tradeCcy.getSwiftCode());
		}
		trade.setisPrimary(rs.getString(++i).equals("true")?true:false);
		/**
		 * set up the commission amount based upon the comm rate type 
		 */
		if(commType==0)
		{
			trade.setcommAmountPay(Math.abs((trade.getshortQuantity()+trade.getlongQuantity()))*trade.getcommRate());
		}
		else if (commType==1)
		{
			trade.setcommAmountPay(Math.abs((trade.getshortQuantity()+trade.getlongQuantity()))*(trade.getcommRate()/10000.0)*trade.getpricePayCcy());
		}
		else if (commType==2)
			trade.setcommAmountPay(flatFee);

		return trade;
	}
		
	
	private String createDividendQueryString(GetTradeDividendsRequest request){
		StringBuilder qb = new StringBuilder(GET_TRADE_DIVIDENDS);
		StringBuilder log = new StringBuilder(GET_TRADE_DIVIDENDS);

		qb.append("@Ticker = ?");
		if(request.gettradeDate() != null){
			qb.append(", @TradeDate = ?");
			log.append(", @TradeDate = " + request.gettradeDate());

		}
		qb.append(") }");
		log.append(") } ");
		logger.info("Executing query:"+log.toString());
		return qb.toString();
	}

	private String createQueryString(GetTradeRequest request) {

		StringBuilder qb = new StringBuilder(GET_TRADE);
		StringBuilder log = new StringBuilder(GET_TRADE);

		qb.append("@UserId = ?");
		if (request.getdateType() != null) {
			qb.append(", @DateType = ?");
			log.append(", @DateType = " + request.getdateType());
		}
		if (request.getdateValueFrom() != null) {
			qb.append(", @DateValueFrom = ?");
			log.append(", @DateValueFrom = " + request.getdateValueFrom());
		}
		if (request.getdateValueTo() != null) {
			qb.append(", @DateValueTo = ?");
			log.append(", @DateValueTo = " + request.getdateValueTo());
		}
		if(request.getenteredBy() != null) {
			qb.append(", @EnteredBy = ?");
			log.append(", @EnteredBy = " + request.getenteredBy());
		}
		if (request.getswapNum() != null) {
			qb.append(", @SwapNum = ?");
			String swapNum = request.getswapNum();
			 String swapNumQuery = getWildCardSearchInt(swapNum,"s.swapNum");
			log.append(", @SwapNum = " + swapNumQuery);
		}
		if (request.getswapName() != null && request.getswapName().length() > 0) {
			qb.append(", @SwapName = ?");
			String swapName = request.getswapName();
			String swapNameQuery = getWildCardSearchString(swapName,"upper(s.name)");
			log.append(", @SwapName = " + swapNameQuery);
		}
		if (request.getlegalEntity() != null && request.getlegalEntity().length() > 0) {
			qb.append(", @Customer = ?");
			String legalEntity = request.getlegalEntity();
			String legalEntityQuery = TaskHelper.convert(legalEntity,"upper(le.name)",false);
			log.append(", @Customer = " + legalEntityQuery);
		}
		if (request.getinstitution() != null && request.getinstitution().length() > 0) {
			qb.append(", @Institution = ?");
			String institution = request.getinstitution();
			String institutionQuery = TaskHelper.convert(institution,"upper(n.name)",false);
			log.append(", @Institution = " + institutionQuery);
		}
		if (request.getbook() != null && request.getbook().length() > 0) {
			qb.append(", @Book = ?");
			String book = request.getbook();
			String bookQuery = getWildCardSearchString(book,"upper(b.name)");
			log.append(", @Book = " + bookQuery);
		}
		if (request.getaccount() != null && request.getaccount().length() > 0) {
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
		if (request.gettradeParty() != null && request.gettradeParty().length() > 0) {
			qb.append(", @TradePartyName = ?");	
			String tradeParty = request.gettradeParty();
			String tradePartyQuery = getWildCardSearchString(tradeParty,"upper(le1.name)");
			log.append(", @TradePartyName = " + tradePartyQuery);	
		}
		if (request.getextType() != null && request.getextType().length() > 0) {
			qb.append(", @ExtType = ?");
			log.append(", @ExtType = " + request.getextType());
		}
		if (request.getextValue() != null && request.getextValue().length() > 0) {
			qb.append(", @ExtValue = ?");
			log.append(", @ExtValue = " + TaskHelper.createWhere(request.getextValue(), request.getextType()));
		}
		if(request.geteventId() > 0 ) {
			qb.append(", @EventId = ?");
			log.append(", @EventId = " + request.geteventId());
		}
		if (request.getinstrId() > 0) {
			qb.append(", @InstrId = ?");
			log.append(", @InstrId = " + request.getinstrId());
		}
		if (request.getstarsId() > 0) {
			qb.append(", @STRARSID = ?");
			log.append(", @STRARSID = " + request.getstarsId());
		}
		if (request.gettracerId() > 0) {
			qb.append(", @TRACERID = ?");
			log.append(", @TRACERID = " + request.gettracerId());
		}
		if (request.getsearchAll() > 0) {
			qb.append(", @SearchAll = ?");
			log.append(", @SearchAll = " + request.getsearchAll());
		}
	
		qb.append(", @IncludeCancel = ?");
		log.append(", @IncludeCancel = " + request.getincludeCancel());
	
		if (request.getdebug() > 0) {
			qb.append(", @Debug = ?");
			log.append(", @Debug = " + request.getdebug());
		}
		qb.append(") } ");
		log.append(") } ");
		
		logger.info("Executing query:"+log.toString());
		
		return qb.toString();
	}
	
	private void setDividendParameters(GetTradeDividendsRequest request, CallableStatement cstmt)
			throws SQLException{
		int i = 0;
		cstmt.setInt(++i, request.getinstrumentId());
		if(request.gettradeDate() != null){
			cstmt.setDate(++i, new java.sql.Date(request.gettradeDate().getTime()));
		}
	}

	private void setParameters(GetTradeRequest request, CallableStatement cstmt)
			throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getuserId());
		if (request.getdateType()!= null){
			cstmt.setString(++i, request.getdateType());
		}
		if (request.getdateValueFrom() != null){
			cstmt.setString(++i, request.getdateValueFrom());
		}
		if (request.getdateValueTo() != null){
			cstmt.setString(++i, request.getdateValueTo());
		}
		if(request.getenteredBy() != null){
			cstmt.setString(++i, request.getenteredBy());
		}
		if (request.getswapNum()!= null){
			String swapNum = request.getswapNum();
			String swapNumQuery = getWildCardSearchInt(swapNum,"s.swapNum");
			logger.info("SwapNumQuery from getWildCardSearchInt : "+swapNumQuery);
			cstmt.setString(++i, swapNumQuery);
		}
		if (request.getswapName()!= null){
			String swapName = request.getswapName();
			String swapNameQuery = getWildCardSearchString(swapName,"upper(s.name)");
			logger.info("swapNameQuery from getWildCardSearchString : "+swapNameQuery);
			cstmt.setString(++i, swapNameQuery);
		}
		if (request.getlegalEntity()!= null&& request.getlegalEntity().length() > 0){
			String legalEntity = request.getlegalEntity();
			String legalEntityQuery = TaskHelper.convert(legalEntity,"upper(le.name)",false);
			logger.info("legalEntityQuery from getWildCardSearchString : "+legalEntityQuery);
			cstmt.setString(++i, legalEntityQuery);
		}
		if (request.getinstitution()!= null){
			String institution = request.getinstitution();
			String institutionQuery = TaskHelper.convert(institution,"upper(n.name)",false);
			logger.info("institutionQuery from getWildCardSearchString : "+institutionQuery);
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
		if (request.gettradeParty()!= null){
			String tradeParty = request.gettradeParty();
			String tradePartyQuery = getWildCardSearchString(tradeParty,"upper(le1.name)");
			logger.info("tradePartyQuery from getWildCardSearchString : "+tradePartyQuery);
			cstmt.setString(++i, tradePartyQuery);
		}
		if (request.getextType() != null && request.getextType().length() > 0){
			cstmt.setString(++i, request.getextType());
		}
		if(request.getextValue() != null){
			String extValueQuery = TaskHelper.createWhere(request.getextValue(), request.getextType());
			logger.info("extValueQuery from TaskHelper.createWhere : "+extValueQuery);
			cstmt.setString(++i, extValueQuery);
		}
		if(request.geteventId() > 0 ){
			cstmt.setInt(++i,request.geteventId());
		}
		if (request.getinstrId() > 0){
			cstmt.setInt(++i, request.getinstrId());
		}
		if (request.getstarsId() > 0){
			cstmt.setInt(++i, request.getstarsId());
		}
		if (request.gettracerId() > 0){
			cstmt.setInt(++i, request.gettracerId());
		}
		if (request.getsearchAll() > 0){
			cstmt.setInt(++i, request.getsearchAll());
		}
		cstmt.setInt(++i, request.getincludeCancel());
		if (request.getdebug() > 0){
			cstmt.setInt(++i, request.getdebug());
		}
	}
}


