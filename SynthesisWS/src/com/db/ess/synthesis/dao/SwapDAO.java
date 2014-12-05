package com.db.ess.synthesis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.omg.CORBA.Request;

import com.db.ess.synthesis.dao.tasks.TaskHelper;
import com.db.ess.synthesis.dvo.Exceptions;
import com.db.ess.synthesis.dvo.GetSwapDatesRequest;
import com.db.ess.synthesis.dvo.GetSwapDatesResponse;
import com.db.ess.synthesis.dvo.GetSwapExceptionsRequest;
import com.db.ess.synthesis.dvo.GetSwapExceptionsResponse;
import com.db.ess.synthesis.dvo.GetSwapTransactionRequest;
import com.db.ess.synthesis.dvo.GetSwapTransactionResponse;
import com.db.ess.synthesis.dvo.GetSwapTypeRequest;
import com.db.ess.synthesis.dvo.GetSwapTypeResponse;
import com.db.ess.synthesis.dvo.ResetCalendar;
import com.db.ess.synthesis.dvo.Swap;
import com.db.ess.synthesis.dvo.SwapAudit;
import com.db.ess.synthesis.dvo.GetSwapAuditRequest;
import com.db.ess.synthesis.dvo.GetSwapAuditResponse;
import com.db.ess.synthesis.dvo.GetSwapRequest;
import com.db.ess.synthesis.dvo.GetSwapResponse;
import com.db.ess.synthesis.dvo.SwapDate;
import com.db.ess.synthesis.dvo.SwapException;
import com.db.ess.synthesis.dvo.SwapTransaction;
import com.db.ess.synthesis.dvo.GetSwapTransactionArgRequest;
import com.db.ess.synthesis.dvo.GetSwapTransactionArgResponse;
import com.db.ess.synthesis.dvo.SwapTransactionArg;
import com.db.ess.synthesis.dvo.SwapTypes;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;
import com.db.ess.synthesis.util.SynthesisConstants;
import com.db.ess.synthesis.util.cache.CachedBasis;
import com.db.ess.synthesis.util.cache.CachedBook;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class SwapDAO extends SynthesisBaseDAO {

	private static final Logger logger = Logger.getLogger(SwapDAO.class
			.getName());

	private static final String GET_SWAPS = "{ call dbo.SYN_GetSwaps (";

	private static final String GET_SWAP_DATES = "{ call dbo.SYN_GetSwapIntEqtDates (";

	private static final String GET_SWAP_TRANSACTION = "{ call dbo.SYN_GetSwapTransaction (";

	private static final String GET_SWAP_TRANSACTION_ARG = "{ call dbo.SYN_GetSwapTransactionArg (";

	private static final String GET_SWAPAUDIT = "SELECT t.auditTypeCode, a.comment, "
			+ "u.firstName+' '+u.lastName userName, u.emailAddress, a.time "
			+ "from SwapAudit a INNER JOIN SwapAuditTypeDomain t ON a.type = "
			+ "t.auditType LEFT OUTER JOIN ETS_Entitlement..EM_User u ON a.userId"
			+ " = u.userId WHERE a.swapId = ?";

	private static final String GET_EXCEPTIONS = "{ call dbo.SYN_GetSwapException (";
	private static final String GET_SWAPTYPES = "{ call dbo.SYN_GetSwapTypes(";

	public GetSwapResponse getSwaps(GetSwapRequest request) throws Exception {

		GetSwapResponse response = new GetSwapResponse();
		int location = request.getlocation();

		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getSwaps(request, ESSLocation.LONDON, response);
			break;

		case ESSLocation.NEWYORK:
			getSwaps(request, ESSLocation.NEWYORK, response);
			break;

		case ESSLocation.GLOBAL:
			getSwaps(request, ESSLocation.LONDON, response);
			getSwaps(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public GetSwapDatesResponse getSwapDates(GetSwapDatesRequest request)
			throws Exception {
		logger.info("Inside getSwapDates(), request received: " + request);
		GetSwapDatesResponse response = new GetSwapDatesResponse();
		int location = request.getlocation();
		SwapDate swapDate = new SwapDate();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getSwapDates(request, ESSLocation.LONDON, response);
			break;

		case ESSLocation.NEWYORK:
			getSwapDates(request, ESSLocation.NEWYORK, response);
			break;
		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public GetSwapExceptionsResponse getSwapExceptions(
			GetSwapExceptionsRequest request) throws Exception {
		logger.info("Inside getSwapExceptions(), request received: " + request);
		GetSwapExceptionsResponse response = new GetSwapExceptionsResponse();
		int location = request.getlocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getSwapExceptions(request, ESSLocation.LONDON, response);
			break;

		case ESSLocation.NEWYORK:
			getSwapExceptions(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public GetSwapTypeResponse getSwapTypes(GetSwapTypeRequest request)
			throws Exception {
		logger.info("Inside getSwapTypes(), request received: " + request);
		GetSwapTypeResponse response = new GetSwapTypeResponse();
		int location = request.getlocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getSwapTypes(request, ESSLocation.LONDON, response);
			break;

		case ESSLocation.NEWYORK:
			getSwapTypes(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public GetSwapTypeResponse getSwapTypes(GetSwapTypeRequest request,
			int location, GetSwapTypeResponse response) throws Exception {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		String query = createSwapTypesQuery(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setSwapTypesParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				response.setswapTypes(populateSwapTypes(rs));
			}

		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}

		return response;
	}

	private String createSwapTypesQuery(GetSwapTypeRequest request) {
		StringBuilder qb = new StringBuilder(GET_SWAPTYPES);
		StringBuilder log = new StringBuilder(GET_SWAPTYPES);

		if (request.getswapNum() > 0) {
			qb.append("@SwapNum = ? ");
			log.append("@SwapNum = " + request.getswapNum());
		}
		qb.append(" )} ");
		log.append(") }");

		logger.info("Executing query:"+log.toString());

		return qb.toString();
	}

	private void setSwapTypesParameters(GetSwapTypeRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getswapNum());
	}

	private SwapTypes populateSwapTypes(ResultSet rs) throws SQLException {
		SwapTypes swapTypes = new SwapTypes();

		int i = 0;
		swapTypes.setdescription(rs.getString(++i));

		return swapTypes;
	}

	public void getSwaps(GetSwapRequest request, int location,
			GetSwapResponse response) throws Exception {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String query = createQueryString(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			
			cstmt = conn.prepareCall(query);
			cstmt.setQueryTimeout(300); /* timeout after 5 minutes */
			setParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			Map<Integer, List<Integer>> swapToBtBMap = new HashMap<Integer, List<Integer>>();

			// Populate SwapNum to BTB Map.
			while (rs.next()) {
				updateswapToBtBMap(rs, swapToBtBMap);
			}

			boolean moreResult = cstmt.getMoreResults();

			if (moreResult) {
				rs2 = cstmt.getResultSet();
				int count = 0;
				while (rs2.next()) {
					if (count > SynthesisConstants.MAX_RESULT_SET_SIZE) {
						response.setreturnResponse(getResultSetOverFlowResponse());
						break;
					}

					Swap swap = populateSwap(rs2, location);
					int swapNum = swap.getswapNumber();
					if (swapToBtBMap.containsKey(swapNum)) {
						swap.setlinked(true);
						List<Integer> btbList = swapToBtBMap.get(swapNum);
						for (int btb : btbList) {
							swap.setswapBtB(btb);
						}
					}

					response.setswaps(swap);
					count++;
				}
				logger.info("--------------------------------------");
				logger.info("Found [" + count
						+ "] Swaps For the given search criteria.");
				logger.info("--------------------------------------");
			}
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
			// SQLUtils.closeResources(conn, cstmt, rs2);
		}
	}

	private void updateswapToBtBMap(ResultSet rs,
			Map<Integer, List<Integer>> swapToBtBMap) throws SQLException {
		int i = 0;
		int swapNum = rs.getInt(++i);
		int linkedSwap = rs.getInt(++i);

		if (swapToBtBMap.containsKey(swapNum)) {
			List<Integer> btbList = swapToBtBMap.get(swapNum);
			btbList.add(linkedSwap);
		} else {
			List<Integer> btbList = new ArrayList<Integer>();
			btbList.add(linkedSwap);
			swapToBtBMap.put(swapNum, btbList);
		}
	}

	public void getSwapDates(GetSwapDatesRequest request, int location,
			GetSwapDatesResponse response) throws Exception {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		SwapDate swapDate = new SwapDate();
		String query = createSwapDatesQueryString(request);
		long time = System.currentTimeMillis();
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setSwapDatesParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			int i =0;
			while (rs.next()) {
				ResetCalendar cal = populateResetCalendar(rs);
				if ("EQUITY".equals(cal.getrowType())) {
					swapDate.setequityResetCalendar(cal);
				} else {
					swapDate.setinterestResetCalendar(cal);
				}
				i++;
			}
			response.setswapDate(swapDate);
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
	}

	public void getSwapExceptions(GetSwapExceptionsRequest request,
			int location, GetSwapExceptionsResponse response) throws Exception {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		String query = createSwapExceptionQueryString(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setSwapExceptionParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			SwapException swapException = new SwapException();
			while (rs.next()) {
				Exceptions cal = populateSwapException(rs);
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

	public GetSwapTransactionResponse getSwapTransactions(
			GetSwapTransactionRequest request) throws Exception {
		logger.info("Inside getSwapTransactions(), request received: "
				+ request);
		GetSwapTransactionResponse response = new GetSwapTransactionResponse();
		int location = request.getlocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getSwapTransactions(request, ESSLocation.LONDON, response);
			break;

		case ESSLocation.NEWYORK:
			getSwapTransactions(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public GetSwapTransactionResponse getSwapTransactions(
			GetSwapTransactionRequest request, int location,
			GetSwapTransactionResponse response) throws Exception {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		String query = createSwapTransactionQuery(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setSwapTransactionParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				response.setswapTransactions(populateSwapTransaction(rs,location));
			}

		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}

		return response;
	}

	public GetSwapTransactionArgResponse getSwapTransactionArgs(
			GetSwapTransactionArgRequest request) throws Exception {
		logger.info("Inside getSwapTransactionArgs(), request received: "
				+ request);
		GetSwapTransactionArgResponse response = new GetSwapTransactionArgResponse();
		int location = request.getlocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getSwapTransactionArgs(request, ESSLocation.LONDON, response);
			break;

		case ESSLocation.NEWYORK:
			getSwapTransactionArgs(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public void getSwapTransactionArgs(GetSwapTransactionArgRequest request,
			int location, GetSwapTransactionArgResponse response)
			throws Exception {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;

		String query = createSwapTransactionArgQuery(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setSwapTransactionArgParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				response.setswapTransactionsArgs(populateSwapTransactionArg(rs));
			}

		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
	}

	public GetSwapAuditResponse getAudit(GetSwapAuditRequest request)
			throws Exception {
		logger.info("Inside getAudit(), request received: " + request);
		GetSwapAuditResponse response = new GetSwapAuditResponse();
		int location = request.getlocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getAudit(request, ESSLocation.LONDON, response);
			break;

		case ESSLocation.NEWYORK:
			getAudit(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;

	}

	public void getAudit(GetSwapAuditRequest request, int location,
			GetSwapAuditResponse response) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_SWAPAUDIT);
			long time = System.currentTimeMillis(); 
			stmt.setInt(1, request.getswapId());
			rs = stmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				SwapAudit audit = new SwapAudit();
				int i = 0;
				audit.setswapId(request.getswapId());
				audit.setactionType(rs.getString(++i));
				audit.setcomment(rs.getString(++i));
				audit.setuserName(rs.getString(++i));
				audit.setemail(rs.getString(++i));
				audit.settime(rs.getDate(++i));
				response.setswapAudits(audit);
			}
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}

	private Swap populateSwap(ResultSet rs, int location) throws Exception {
		Swap swap = new Swap();

		swap.setessLocation(location);
		int i = 0;
		swap.setswapId(rs.getInt(++i));
		swap.setswapNumber(rs.getInt(++i));
		swap.setswapName(rs.getString(++i));
		int tradePartyId = rs.getInt(++i);
		swap.setcontact(rs.getString(++i));

		Timestamp tradeDateTimeStamp = rs.getTimestamp(++i);
		Date tradeDate = tradeDateTimeStamp == null ? null : new Date(
				tradeDateTimeStamp.getTime());
		swap.settradeDate(tradeDate);

		Timestamp effDateTimeStamp = rs.getTimestamp(++i);
		Date effDate = effDateTimeStamp == null ? null : new Date(
				effDateTimeStamp.getTime());
		swap.seteffDate(effDate);

		Timestamp termDateTimeStamp = rs.getTimestamp(++i);
		Date termDate = termDateTimeStamp == null ? null : new Date(
				termDateTimeStamp.getTime());
		swap.settermDate(termDate);

		int swapType = rs.getInt(++i);
		int strategy = rs.getInt(++i);
		int status = rs.getInt(++i);
		swap.setinternalLoan(rs.getInt(++i));
		swap.setpayEqAsRealized(rs.getInt(++i));
		swap.setcalcAgent(rs.getInt(++i));
		swap.setneedDoc(rs.getInt(++i));
		swap.setstructureNum(rs.getInt(++i));
		int collType = rs.getInt(++i);
		swap.setcollLong(rs.getDouble(++i));
		swap.setcollShort(rs.getDouble(++i));
		swap.setintThreshold(rs.getDouble(++i));
		swap.setextThreshold(rs.getDouble(++i));
		int country = rs.getInt(++i);
		swap.setcommAmt(rs.getDouble(++i));
		int commType = rs.getInt(++i);
		int commPayType = rs.getInt(++i);
		int eqReturnType = rs.getInt(++i);
		int payCcy = rs.getInt(++i);
		int tradeCcy = rs.getInt(++i);
		int divPayType = rs.getInt(++i);
		swap.setfxDivs(rs.getInt(++i));
		swap.setfxDivDate(rs.getInt(++i));
		swap.setdivFxOffset(rs.getInt(++i));
		swap.setshortSpread(rs.getDouble(++i));
		swap.setlongSpread(rs.getDouble(++i));
		swap.setshortNetDivPct(rs.getDouble(++i));
		swap.setlongNetDivPct(rs.getDouble(++i));
		int intBasis = rs.getInt(++i);
		int intPeriod = rs.getInt(++i);
		swap.setintTicker(rs.getString(++i));
		swap.setfixedRate(rs.getInt(++i));
		int eqPayFreq = rs.getInt(++i);
		int eqPayCal = rs.getInt(++i);
		int eqPayRuleType = rs.getInt(++i);
		int eqPayBump = rs.getInt(++i);
		swap.seteqPayOffset(rs.getInt(++i));
		int eqPayPriority = rs.getInt(++i);
		int eqPaySeq = rs.getInt(++i);
		swap.seteqResetSource(rs.getString(++i));
		int eqResetCal = rs.getInt(++i);
		int eqResetRuleType = rs.getInt(++i);
		int eqResetBump = rs.getInt(++i);
		swap.seteqResetOffset(rs.getInt(++i));
		int fxResetCal = rs.getInt(++i);
		swap.setfxResetSource(rs.getString(++i));
		swap.setfxResetOffset(rs.getInt(++i));
		int intPayFreq = rs.getInt(++i);
		int intPayCal = rs.getInt(++i);
		int intPayRuleType = rs.getInt(++i);
		int intPayBump = rs.getInt(++i);
		swap.setintPayOffset(rs.getInt(++i));
		int intPayPriority = rs.getInt(++i);
		int intPaySeq = rs.getInt(++i);
		swap.setintResetSource(rs.getString(++i));
		int intResetFreq = rs.getInt(++i);
		int intResetCal = rs.getInt(++i);
		int intResetRuleType = rs.getInt(++i);
		int intResetBump = rs.getInt(++i);
		swap.setintResetOffset(rs.getInt(++i));
		swap.setintFirstFixing(rs.getDouble(++i));
		swap.setcustomerId(rs.getInt(++i));
		swap.setbookId(rs.getInt(++i));
		swap.setautomature(rs.getInt(++i));
		swap.setautomatureDate(rs.getString(++i));
		swap.setbtbEntityId1(rs.getInt(++i));
		swap.setbtbEntityName1(RefCache.getLegalEntity(rs.getInt(i),location));
		int btbBookId1 = rs.getInt(++i);
		CachedBook btbBook1;
		if ((btbBook1 = RefCache.getBook(btbBookId1,location)) != null) {
			swap.setbtbBookName1(btbBook1.getName());
		}
		swap.setbtbEntityId2(rs.getInt(++i));
		swap.setbtbEntityName2(RefCache.getLegalEntity(rs.getInt(i),location));
		int btbBookId2 = rs.getInt(++i);
		CachedBook btbBook2;
		if ((btbBook2 = RefCache.getBook(btbBookId2,location)) != null) {
			swap.setbtbBookName2(btbBook2.getName());
		}
		int btbBUId1 = rs.getInt(++i);
		swap.setbtbBUName1(RefCache.getBusinessUnit(btbBUId1,location));
		int btbAccountId1 = rs.getInt(++i);
		swap.setbtbAccountName1(RefCache.getAccount(btbAccountId1,location));
		int btbBUId2 = rs.getInt(++i);
		swap.setbtbBUName2(RefCache.getBusinessUnit(btbBUId2,location));
		int btbAccountId2 = rs.getInt(++i);
		swap.setbtbAccountName2(RefCache.getAccount(btbAccountId2,location));

		swap.setinstitutionName(rs.getString(++i));
		swap.setcustomerName(rs.getString(++i));
		swap.settotalReturnSwap(rs.getInt(++i));
		swap.settermFunding(rs.getInt(++i));
		swap.setmaturityTag(rs.getString(++i));
		swap.setindexDivEntitle(rs.getString(++i));
		swap.setdivRounding(rs.getInt(++i));
		swap.setborrowSpreadShort(rs.getDouble(++i));
		swap.setborrowSpreadLong(rs.getDouble(++i));
		Timestamp proptermDateTimeStamp = rs.getTimestamp(++i);
		Date proptermDate = proptermDateTimeStamp == null ? null : new Date(
				proptermDateTimeStamp.getTime());
		swap.setproptermDate(proptermDate);
		swap.setmwenabled(rs.getInt(++i));
		swap.setprofitCentreName(rs.getString(++i));
		swap.setcopyEquityDates(rs.getInt(++i));
		swap.setisPrimary(rs.getString(++i).equalsIgnoreCase("true")?true:false);
		

		swap.settradePartyName(RefCache.getLegalEntity(tradePartyId,location));
		swap.setswapType(RefCache.getSwapTypeDomain(swapType,location));
		swap.setstrategy(RefCache.getStrategyDomain(strategy,location));
		swap.setstatus(RefCache.getSwapStatusDomain(status,location));
		swap.setcollType(RefCache.getCollateralTypeDomain(collType,location));
		swap.setcountry(RefCache.getCountry(country,location));
		swap.setcommType(RefCache.getCommissionTypeDomain(commType,location));
		swap.setcommPayType(RefCache.getCommissionPayDomain(commPayType,location));
		swap.seteqReturnType(RefCache.getReturnTypeDomain(eqReturnType,location));
		CachedCurrency currency = null;
		if ((currency = RefCache.getCurrency(payCcy,location)) != null)
			swap.setpayCcy(currency.getSwiftCode());
		if ((currency = RefCache.getCurrency(tradeCcy,location)) != null)
			swap.settradeCcy(currency.getSwiftCode());
		swap.setdivPayType(RefCache.getPayDivDomain(divPayType,location));
		CachedBasis basis = null;
		if ((basis = RefCache.getBasisDomain(intBasis,location)) != null)
			swap.setintBasis(basis.getUnitTypeCode());
		swap.seteqPayFreq(RefCache.getFreqTypeDomain(eqPayFreq,location));
		swap.seteqPayCal(RefCache.getLogicalCal(eqPayCal,location));
		swap.seteqPayBump(RefCache.getBumpConventionDomain(eqPayBump,location));
		swap.seteqResetCal(RefCache.getLogicalCal(eqResetCal,location));
		swap.seteqResetBump(RefCache.getBumpConventionDomain(eqResetBump,location));
		swap.setfxResetCal(RefCache.getLogicalCal(fxResetCal,location));
		swap.setintPayFreq(RefCache.getFreqTypeDomain(intPayFreq,location));
		swap.setintPayCal(RefCache.getLogicalCal(intPayCal,location));
		swap.setintPayBump(RefCache.getBumpConventionDomain(intPayBump,location));
		swap.setintResetFreq(RefCache.getFreqTypeDomain(intResetFreq,location));
		swap.setintResetCal(RefCache.getLogicalCal(intResetCal,location));
		swap.setintResetBump(RefCache.getBumpConventionDomain(intResetBump,location));

		if (swap.getfixedRate() == 0) {
			swap.setintPeriod(RefCache.getPeriodDomain(intPeriod,location));
			swap.setintTicker(swap.getintTicker());
		}
		else
			swap.setintTicker(null);

		CachedBook book = null;

		if ((book = RefCache.getBook(swap.getbookId(),location)) != null) {
			swap.setadpAcctNum(book.getAdpAcctNum());
			swap.setbookName(book.getName());
			swap.setaccountName(book.getAccountname());
			swap.setdeskName(book.getDeskname());
			swap.setbusinessNameUnit(book.getBusinessUnitName());
		}

		switch (eqPayRuleType) {
		case 1:
			swap.seteqPayRuleType("ADV");
			break;
		case 2:
			swap.seteqPayRuleType("ARR");
			break;
		default:
			break;
		}

		switch (eqPayPriority) {
		case 0:
			swap.seteqPayPriority("Start Date");
			break;
		case 1:
			swap.seteqPayPriority("StartDate using ShortStub");
			break;
		case 2:
			swap.seteqPayPriority("Reset Date");
			break;
		default:
			break;
		}

		swap.seteqPaySeq(eqPaySeq % 100);

		switch (eqPaySeq / 100) {
		case 0:
			swap.seteqPaySeqType("DaysOfMonth");
			break;
		case 1:
			swap.seteqPaySeqType("Monday");
			break;
		case 2:
			swap.seteqPaySeqType("'Tuesday");
			break;
		case 3:
			swap.seteqPaySeqType("Wednesday");
			break;
		case 4:
			swap.seteqPaySeqType("Thursday");
			break;
		case 5:
			swap.seteqPaySeqType("Friday");
			break;
		default:
			break;
		}

		switch (eqResetRuleType) {
		case 1:
			swap.seteqResetRuleType("ADV");
			break;
		case 2:
			swap.seteqResetRuleType("ARR");
			break;
		default:
			break;
		}

		switch (intPayRuleType) {
		case 1:
			swap.setintPayRuleType("ADV");
			break;
		case 2:
			swap.setintPayRuleType("ARR");
			break;
		default:
			break;
		}

		switch (intPayPriority) {
		case 0:
			swap.setintPayPriority("Start Date");
			break;
		case 1:
			swap.setintPayPriority("StartDate using ShortStub");
			break;
		case 2:
			swap.setintPayPriority("Reset Date");
			break;
		default:
			break;
		}

		swap.setintPaySeq(intPaySeq % 100);
		switch (intPaySeq / 100) {
		case 0:
			swap.setintPaySeqType("DaysOfMonth");
			break;
		case 1:
			swap.setintPaySeqType("Monday");
			break;
		case 2:
			swap.setintPaySeqType("'Tuesday");
			break;
		case 3:
			swap.setintPaySeqType("Wednesday");
			break;
		case 4:
			swap.setintPaySeqType("Thursday");
			break;
		case 5:
			swap.setintPaySeqType("Friday");
			break;
		default:
			break;
		}

		switch (intResetRuleType) {
		case 1:
			swap.setintResetRuleType("ADV");
			break;
		case 2:
			swap.setintResetRuleType("ARR");
			break;
		default:
			break;
		}
		// swap.setessLocation(location);

		return swap;
	}

	private ResetCalendar populateResetCalendar(ResultSet rs)
			throws SQLException {
		ResetCalendar resetCalendar = new ResetCalendar();
		int i = 0;

		Timestamp settleDateTimeStamp = rs.getTimestamp(++i);
		Date settleDate = settleDateTimeStamp == null ? null : new Date(
				settleDateTimeStamp.getTime());
		Timestamp tradeDateTimeStamp = rs.getTimestamp(++i);
		Date tradeDate = tradeDateTimeStamp == null ? null : new Date(
				tradeDateTimeStamp.getTime());
		Timestamp payDateTimeStamp = rs.getTimestamp(++i);
		Date payDate = payDateTimeStamp == null ? null : new Date(
				payDateTimeStamp.getTime());
		Timestamp fxDateTimestamp = rs.getTimestamp(++i);
		Date fxDate = fxDateTimestamp == null ? null : new Date(
				fxDateTimestamp.getTime());

		int legId = rs.getInt(++i);
		double resetValue = rs.getDouble(++i);
		int countryId = rs.getInt(++i);
		String rowType = rs.getString(++i);

		resetCalendar.setstartDate(tradeDate);
		resetCalendar.setresetDate(settleDate);
		resetCalendar.setpayDate(payDate);
		resetCalendar.setfxDate(fxDate);
		resetCalendar.setresetValue(resetValue);
		resetCalendar.setrowType(rowType);

		return resetCalendar;
	}

	private Exceptions populateSwapException(ResultSet rs) throws SQLException {
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

	private SwapTransaction populateSwapTransaction(ResultSet rs,int location)
			throws SQLException {
		SwapTransaction swapTransaction = new SwapTransaction();

		int i = 0;

		swapTransaction.seteventId(rs.getInt(++i));
		swapTransaction.setlongShort(rs.getInt(++i));
		swapTransaction.setticker(rs.getString(++i));
		swapTransaction.setswiftCode(rs.getString(++i));

		Timestamp tradeDateTimeStamp = rs.getTimestamp(++i);
		Date tradeDate = tradeDateTimeStamp == null ? null : new Date(
				tradeDateTimeStamp.getTime());
		swapTransaction.settradeDate(tradeDate);

		Timestamp settleDateTimeStamp = rs.getTimestamp(++i);
		Date settleDate = settleDateTimeStamp == null ? null : new Date(
				settleDateTimeStamp.getTime());
		swapTransaction.setsettleDate(settleDate);

		swapTransaction.setqty(rs.getDouble(++i));
		swapTransaction.setprice(rs.getDouble(++i));
		swapTransaction.setbasePrice(rs.getDouble(++i));
		swapTransaction.setfxRate(rs.getDouble(++i));
		swapTransaction.setintRate(rs.getDouble(++i));
		swapTransaction.setspreadEx(rs.getString(++i));
		swapTransaction.seteventType(rs.getInt(++i));
		swapTransaction.setcommission(rs.getDouble(++i));
		swapTransaction.setcommissionType(rs.getString(++i));
		int commisionCode=rs.getInt(++i);
		swapTransaction.setcommissionPay(RefCache.getCommissionPayDomain(commisionCode,location));
		swapTransaction.setnotional(rs.getDouble(++i));
		swapTransaction.setdescription(rs.getString(++i));
		swapTransaction.setfiId(rs.getInt(++i));
		swapTransaction.seteventCode(rs.getString(++i));

		Timestamp maturityDateTimeStamp = rs.getTimestamp(++i);
		Date maturityDate = maturityDateTimeStamp == null ? null : new Date(
				maturityDateTimeStamp.getTime());
		swapTransaction.setmaturityDate(maturityDate);

		Timestamp enterTimeStamp = rs.getTimestamp(++i);
		Date enterTime = enterTimeStamp == null ? null : new Date(
				enterTimeStamp.getTime());
		swapTransaction.setenterTime(enterTime);

		Timestamp orgEnterTimeStamp = rs.getTimestamp(++i);
		Date orgEnterTime = orgEnterTimeStamp == null ? null : new Date(
				orgEnterTimeStamp.getTime());
		swapTransaction.setoriginalEnterTime(orgEnterTime);
		swapTransaction.setcorpAction(rs.getString(++i));
		swapTransaction.setswapNum(rs.getInt(++i));
		swapTransaction.setcustomerId(rs.getInt(++i));
		swapTransaction.settradePartyId(rs.getInt(++i));
		swapTransaction.setbookId(rs.getInt(++i));
		swapTransaction.setlegId(rs.getInt(++i));
		swapTransaction.setbuySell(rs.getDouble(++i));
		swapTransaction.setbaseAmount(rs.getDouble(++i));
		swapTransaction.setnotionalBaseCcy(rs.getDouble(++i));
		swapTransaction.setpayAmount(rs.getDouble(++i));
		swapTransaction.setnotionalPayCcy(rs.getDouble(++i));
		swapTransaction.setclientRef(rs.getString(++i));
		return swapTransaction;
	}

	private String createSwapDatesQueryString(GetSwapDatesRequest request) {
		StringBuilder qb = new StringBuilder(GET_SWAP_DATES);
		StringBuilder log = new StringBuilder(GET_SWAP_DATES);
		qb.append("? ) }");
		log.append("@SwapNum = " + request.getswapNum());
		log.append(") }");
		logger.info("Executing query:"+log.toString());
		return qb.toString();

	}

	private String createSwapTransactionQuery(GetSwapTransactionRequest request) {
		StringBuilder qb = new StringBuilder(GET_SWAP_TRANSACTION);
		StringBuilder log = new StringBuilder(GET_SWAP_TRANSACTION);

		if (request.getswapNum() > 0) {
			qb.append("@SwapNum = ?");
			log.append("@SwapNum = " + request.getswapNum());
		}
		if (request.getinstrId() != null) {
			qb.append(", @InstrId = ?");
			log.append(", @InstrId = " + request.getinstrId());
		}
		if (request.geteventType() != null) {
			qb.append(",@EventType = ?");
			log.append(", @EventType = " + request.geteventType());
		}
		if (request.getdateType() != null) {
			qb.append(",@DateType = ?");
			log.append(", @DateType = " + request.getdateType());

		}
		if (request.getdateValueFrom() != null){
			qb.append(", @DateValueFrom = ?");
		log.append(", @DateValueFrom = " + request.getdateValueFrom());
		}
		if (request.getdateValueTo() != null){
			qb.append(", @DateValueTo = ?");
			log.append(", @DateValueTo = " + request.getdateValueTo());
		}
		qb.append(")}");
		log.append(") }");
		logger.info("Executing query:"+log.toString());

		return qb.toString();

	}

	private String createSwapTransactionArgQuery(
			GetSwapTransactionArgRequest request) {
		StringBuilder qb = new StringBuilder(GET_SWAP_TRANSACTION_ARG);
		StringBuilder log = new StringBuilder(GET_SWAP_TRANSACTION_ARG);

		if (request.getswapNum() > 0) {
			qb.append("@SwapNum = ? ");
			log.append("@SwapNum = " + request.getswapNum());

		}
		qb.append(" )} ");
		log.append(") }");
		logger.info("Executing query:"+log.toString());
		return qb.toString();
	}

	private String createSwapExceptionQueryString(
			GetSwapExceptionsRequest request) {
		StringBuilder qb = new StringBuilder(GET_EXCEPTIONS);
		StringBuilder log = new StringBuilder(GET_EXCEPTIONS);
		if (request.getswapNum() > 0) {
			qb.append("? ) }");
			log.append("@SwapNum = " + request.getswapNum());
			log.append(") }");
			logger.info("Executing query:"+log.toString());
		}
		return qb.toString();
	}

	private String createQueryString(GetSwapRequest request) {

		StringBuilder qb = new StringBuilder(GET_SWAPS);
		StringBuilder log = new StringBuilder(GET_SWAPS);

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
		if (request.getswapNum() != null) {
			qb.append(", @SwapNum = ?");
			String swapNum = request.getswapNum();
			 String swapNumQuery = getWildCardSearchInt(swapNum,"s.swapNum");
			log.append(", @SwapNum = " + swapNumQuery);
		}
		if (request.getstatus() != null) {
			qb.append(", @Status = ?");
			log.append(", @Status = " + request.getstatus());
		}
		if (request.getlegalEntity() != null && request.getlegalEntity().length() > 0) {
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
		if (request.getswapName() != null && request.getswapName().length() > 0) {
			qb.append(", @SwapName = ?");
			String swapName = request.getswapName();
			String swapNameQuery = getWildCardSearchString(swapName,"upper(s.name)");
			log.append(", @SwapName = " + swapNameQuery);
		}
		if (request.gettradeParty() != null && request.gettradeParty().length() > 0) {
			qb.append(", @TradePartyName = ?");
			String tradeParty = request.gettradeParty();
			String tradePartyQuery = getWildCardSearchString(tradeParty,"upper(le2.name)");
			log.append(", @TradePartyName = " + tradePartyQuery);
		}
	
		qb.append(", @OpenPosition = ?");
		log.append(", @OpenPosition = " + request.getopenPosition());
	
		if (request.getstrategy() != null && request.getstrategy().length() > 0) {
			qb.append(", @Strategy = ?");
			String strategy = request.getstrategy();
			String strategyQuery = getWildCardSearchString(strategy,"upper(sd.code)");
			log.append(", @Strategy = " + strategyQuery);
		}
		if (request.getdomain() != null && request.getdomain().length() > 0) {
			qb.append(", @Domain = ?");
			String domain = request.getdomain();
			String domainQuery = getWildCardSearchString(domain,"upper(std.typeCode)");
			log.append(", @Domain = " + domainQuery);
		}
		
		qb.append(", @MaturedSwaps = ?");
		log.append(", @MaturedSwaps = " + request.getmaturedSwaps());
		
		if (request.getsearchAll() > 0) {
			qb.append(", @SearchAll = ?");
			log.append(", @SearchAll = " + request.getsearchAll());
		}
		if (request.getautomatureDate() != null) {
			qb.append(", @AutomatureDate = ?");
			log.append(", @AutomatureDate = " + request.getautomatureDate());
		}
		qb.append(") }");
		log.append(") }");

		logger.info("Executing query:"+log.toString());

		return qb.toString();
	}

	private SwapTransactionArg populateSwapTransactionArg(ResultSet rs)
			throws SQLException {
		SwapTransactionArg swapTransactionArg = new SwapTransactionArg();

		int i = 0;
		swapTransactionArg.setinstrId(rs.getInt(++i));
		swapTransactionArg.setticker(rs.getString(++i));

		return swapTransactionArg;
	}

	private void setSwapDatesParameters(GetSwapDatesRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getswapNum());
	}

	private void setSwapExceptionParameters(GetSwapExceptionsRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getswapNum());
	}

	private void setSwapTransactionParameters(
			GetSwapTransactionRequest request, CallableStatement cstmt)
			throws SQLException {
		int i = 0;
		if (request.getswapNum()> 0)
			cstmt.setInt(++i, request.getswapNum());
		if (request.getinstrId() != null)
			cstmt.setString(++i, request.getinstrId());
		if (request.geteventType() != null)
			cstmt.setString(++i, request.geteventType());
		if (request.getdateType() != null)
			cstmt.setString(++i, request.getdateType());
		if (request.getdateValueFrom() != null)
			cstmt.setDate(++i, new java.sql.Date(request.getdateValueFrom()
					.getTime()));
		if (request.getdateValueTo() != null)
			cstmt.setDate(++i, new java.sql.Date(request.getdateValueTo()
					.getTime()));
	}

	private void setSwapTransactionArgParameters(
			GetSwapTransactionArgRequest request, CallableStatement cstmt)
			throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getswapNum());
	}

	private void setParameters(GetSwapRequest request, CallableStatement cstmt)
			throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getuserId());
		if (request.getdateType() != null)
			cstmt.setString(++i, request.getdateType());
		if (request.getdateValueFrom() != null)
			cstmt.setString(++i, request.getdateValueFrom());
		if (request.getdateValueTo() != null)
			cstmt.setString(++i, request.getdateValueTo());
		if (request.getswapNum()!= null){
			String swapNum = request.getswapNum();
			 String swapNumQuery = getWildCardSearchInt(swapNum,"s.swapNum");
			logger.info("SwapNumQuery from getWildCardSearchInt : "+swapNumQuery);
			cstmt.setString(++i, swapNumQuery);
		}
		if (request.getstatus() != null
				&& request.getstatus().length() > 0)
			cstmt.setString(++i, request.getstatus());
		if (request.getlegalEntity()!= null){
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
		if (request.getswapName()!= null){
			String swapName = request.getswapName();
			String swapNameQuery = getWildCardSearchString(swapName,"upper(s.name)");
			logger.info("swapNameQuery from getWildCardSearchString : "+swapNameQuery);
			cstmt.setString(++i, swapNameQuery);
		}
		if (request.gettradeParty()!= null){
			String tradeParty = request.gettradeParty();
			String tradePartyQuery = getWildCardSearchString(tradeParty,"upper(le2.name)");
			logger.info("tradePartyQuery from getWildCardSearchString : "+tradePartyQuery);
			cstmt.setString(++i, tradePartyQuery);
		}
		cstmt.setInt(++i, request.getopenPosition());
		
		if (request.getstrategy()!= null){
			String strategy = request.getstrategy();
			String strategyQuery = getWildCardSearchString(strategy,"upper(sd.code)");
			logger.info("strategyQuery from getWildCardSearchString : "+strategyQuery);
			cstmt.setString(++i, strategyQuery);
		}
		if (request.getdomain()!= null){
			String domain = request.getdomain();
			String domainQuery = getWildCardSearchString(domain,"upper(std.typeCode)");
			logger.info("domainQuery from getWildCardSearchString : "+domainQuery);
			cstmt.setString(++i, domainQuery);
		}
		cstmt.setInt(++i, request.getmaturedSwaps());
		
		if (request.getautomatureDate() != null)
			cstmt.setDate(++i, new java.sql.Date(request.getautomatureDate()
					.getTime()));
		if (request.getdebug() > 0)
			cstmt.setInt(++i, request.getdebug());
	}

}
