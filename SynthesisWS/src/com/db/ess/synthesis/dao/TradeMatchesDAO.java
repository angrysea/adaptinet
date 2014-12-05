package com.db.ess.synthesis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.TaskHelper;
import com.db.ess.synthesis.dvo.EligibleOpeningTrade;
import com.db.ess.synthesis.dvo.GetEligibleOpeningTradesRequest;
import com.db.ess.synthesis.dvo.GetEligibleOpeningTradesResponse;
import com.db.ess.synthesis.dvo.GetTradeMatchesRequest;
import com.db.ess.synthesis.dvo.GetTradeMatchesResponse;
import com.db.ess.synthesis.dvo.TradeMatch;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;
import com.db.ess.synthesis.util.SynthesisConstants;
import com.db.ess.synthesis.util.cache.RefCache;


public class TradeMatchesDAO extends SynthesisBaseDAO {

	private static final Logger logger = Logger.getLogger(TradeMatchesDAO.class.getName());
	private static final String GET_TRADEMATCHES = "{ call dbo.SYN_GetTradeMatches (";
	private static final String GET_ELIGIBLEOPENINGS = 
		" { call dbo.SYN_GetEligibleOpeningLots (@SwapLegId=?, @TradeDate=?, @SettleDate=?, " +
		" @EventId=?, @InstrumentId=?) }";

	public GetTradeMatchesResponse getTradeMaches (GetTradeMatchesRequest request) throws Exception {

		GetTradeMatchesResponse response = new GetTradeMatchesResponse();

		int location = request.getlocation();
		switch (location) {

		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;
			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getTradeMaches(request, ESSLocation.LONDON, response);
			break;
		case ESSLocation.NEWYORK:
			getTradeMaches(request, ESSLocation.NEWYORK, response);
			break;
		case ESSLocation.GLOBAL:
			getTradeMaches(request, ESSLocation.LONDON, response);
			getTradeMaches(request, ESSLocation.NEWYORK, response);
			break;
		default:
			throw new Exception("Invalid or Unknown location.");

		}
		return response;

	}

	public GetTradeMatchesResponse getTradeMaches(GetTradeMatchesRequest request,
			int location, GetTradeMatchesResponse response) throws Exception {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = createQueryString(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			int count = 0;
			while (rs.next()) {
				if (count > SynthesisConstants.MAX_RESULT_SET_SIZE) {
					response.setreturnResponse(getResultSetOverFlowResponse());
					break;
				}
				TradeMatch tradeMatch = populateTradeMatch(rs,location);
				response.settradeMatches(tradeMatch);
				count++;
			}

			logger.info("---------------------------------------------------");
			logger.info("Found ["+count+"] TradeMatches For the given search criteria.");
			logger.info("---------------------------------------------------");
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
		return response;

	}
	
	
	public GetEligibleOpeningTradesResponse getEligibleOpeningTrades 
	    (GetEligibleOpeningTradesRequest request) throws Exception {

		GetEligibleOpeningTradesResponse response = new GetEligibleOpeningTradesResponse();

		int location = request.getlocation();
		switch (location) {

		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;
		case ESSLocation.LONDON:
			getEligibleOpeningTrades(request, ESSLocation.LONDON, response);
			break;
		case ESSLocation.NEWYORK:
			getEligibleOpeningTrades(request, ESSLocation.NEWYORK, response);
			break;
		case ESSLocation.GLOBAL:
			getEligibleOpeningTrades(request, ESSLocation.LONDON, response);
			getEligibleOpeningTrades(request, ESSLocation.NEWYORK, response);
			break;
		default:
			throw new Exception("Invalid or Unknown location.");

		}
		return response;

	}

	public GetEligibleOpeningTradesResponse getEligibleOpeningTrades
	    (GetEligibleOpeningTradesRequest request,
		 int location, GetEligibleOpeningTradesResponse response) throws Exception {
		
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(GET_ELIGIBLEOPENINGS);
			cstmt.setInt(1, request.getswapLegId());
			cstmt.setString(2, request.gettradeDate());
			cstmt.setString(3, request.getsettleDate());
			cstmt.setInt(4, request.geteventId());
			cstmt.setInt(5, request.getinstrId());
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			int count = 0;
			while (rs.next()) {
				if (count > SynthesisConstants.MAX_RESULT_SET_SIZE) {
					response.setreturnResponse(getResultSetOverFlowResponse());
					break;
				}
				EligibleOpeningTrade eligibleTrade = new EligibleOpeningTrade();
				int i = 0;
				eligibleTrade.seteventId(rs.getInt(++i));
				eligibleTrade.setlegId(rs.getInt(++i));
				eligibleTrade.setinstrId(rs.getInt(++i));
				eligibleTrade.setqty(rs.getFloat(++i));
				Timestamp tradeTimeStamp = rs.getTimestamp(++i);
				Date tradeDate = tradeTimeStamp == null ? null : new Date(
						tradeTimeStamp.getTime());
				eligibleTrade.settradeDate(tradeDate); 
				Timestamp settleTimeStamp = rs.getTimestamp(++i);
				Date settleDate = settleTimeStamp == null ? null : new Date(
						settleTimeStamp.getTime());
				eligibleTrade.setsettleDate(settleDate);
				eligibleTrade.setprice(rs.getFloat(++i));
				eligibleTrade.setmatchedQty(rs.getFloat(++i));
				eligibleTrade.setlotAge(rs.getInt(++i));
				response.seteligibleOpeningTrades(eligibleTrade);
				count++;
			}

			logger.info("---------------------------------------------------");
			logger.info("Found ["+count+"] TradeMatches For the given search criteria.");
			logger.info("---------------------------------------------------");
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
		return response;

	}


	private String createQueryString(GetTradeMatchesRequest request) {
		StringBuilder qb = new StringBuilder(GET_TRADEMATCHES);
		StringBuilder log = new StringBuilder(GET_TRADEMATCHES);
		qb.append("@UserId = ?");
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
		if (request.getenteredBy() != null) {
			qb.append(", @EnteredBy = ?");
			log.append(", @EnteredBy = " + request.getenteredBy());
		}
		if (request.getswapNum() != null) {
			qb.append(", @SwapNum = ?");
			log.append(", @SwapNum = " + request.getswapNum());
		}
		if (request.getswapName() != null && request.getswapName().length() > 0) {
			qb.append(", @SwapName = ?");
			log.append(", @SwapName = " + request.getswapName());
		}
		if (request.getcustomer()!= null && request.getcustomer().length() > 0) {
			qb.append(", @Customer = ?");
			log.append(", @Customer = " + request.getcustomer());
		}
		if (request.getinstitution() != null && request.getinstitution().length() > 0) {
			qb.append(", @Institution = ?");
			log.append(", @Institution = " + request.getinstitution());
		}

		if (request.getbook()!= null && request.getbook().length() > 0) {
			qb.append(", @Book = ?");
			log.append(", @Book = " + request.getbook());
		}
		if (request.getaccount()!= null && request.getaccount().length() > 0) {
			qb.append(", @Account = ?");
			log.append(", @Account = " + request.getaccount());
		}
		if (request.getdesk() != null && request.getdesk().length() > 0) {
			qb.append(", @Desk = ?");
			log.append(", @Desk = " + request.getdesk());
		}
		if (request.getbusinessUnit() != null && request.getbusinessUnit().length() > 0) {
			qb.append(", @BusinessUnit = ?");
			log.append(", @BusinessUnit = " + request.getbusinessUnit());
		}
		if (request.gettradePartyName() != null && request.gettradePartyName().length() > 0) {
			qb.append(", @TradePartyName = ?");
			log.append(", @TradePartyName = " + request.gettradePartyName());
		}
		if (request.getextType() != null) {
			qb.append(",@ExtType = ?");
			log.append(", @ExtType = " + request.getextType());
		}
		if (request.getextValue() != null) {
			qb.append(",@ExtValue = ?");
			log.append(", @ExtValue = " + request.getextValue());
		}
		if (request.geteventId() != 0) {
			qb.append(",@EventId = ?");
			log.append(", @EventId = " + request.geteventId());
		}
		if (request.getinstrId() != 0) {
			qb.append(", @InstrId = ?");
			log.append(", @InstrId = " + request.getinstrId());
		}
		if (request.getSTRARSID() != 0) {
			qb.append(", @STRARSID = ?");
			log.append(", @STRARSID = " + request.getSTRARSID());
		}
		if (request.getTRACERID() != 0) {
			qb.append(", @TRACERID = ?");
			log.append(", @TRACERID = " + request.getTRACERID());
		}
		if (request.getsearchAll() > 0) {
			qb.append(", @SearchAll = ?");
			log.append(", @SearchAll = " + request.getsearchAll());
		}
		if (request.getincludeCancel() > 0) {
			qb.append(", @IncludeCancel = ?");
			log.append(", @IncludeCancel = " + request.getincludeCancel());
		}
		qb.append(") } ");
		log.append(") } ");
		logger.info(log.toString());
		return qb.toString();
	}

	private void setParameters(GetTradeMatchesRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getuserId());
		if (request.getdateType()!= null)
			cstmt.setString(++i, request.getdateType());
		if (request.getdateValueFrom() != null)
			cstmt.setString(++i, request.getdateValueFrom());
		if (request.getdateValueTo() != null)
			cstmt.setString(++i, request.getdateValueTo());
		if (request.getenteredBy()!= null)
			cstmt.setString(++i, request.getenteredBy());
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
		if (request.getcustomer()!= null && request.getcustomer().length() > 0)
			cstmt.setString(++i, request.getcustomer());
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
		if (request.gettradePartyName()!= null){
			String tradeParty = request.gettradePartyName();
			String tradePartyQuery = getWildCardSearchString(tradeParty,"upper(le1.name)");
			logger.info("tradePartyQuery from getWildCardSearchString : "+tradePartyQuery);
			cstmt.setString(++i, tradePartyQuery);
		}
		if (request.getextType() != null && request.getextType().length() > 0)
			cstmt.setString(++i, request.getextType());
		if (request.getextValue() != null && request.getextValue().length() > 0)
			cstmt.setString(++i, request.getextValue());
		if (request.geteventId()> 0)
			cstmt.setInt(++i, request.geteventId());
		if (request.getinstrId()> 0)
			cstmt.setInt(++i, request.getinstrId());
		if (request.getSTRARSID() >0)
			cstmt.setInt(++i, request.getSTRARSID());
		if (request.getTRACERID() > 0)
			cstmt.setInt(++i, request.getTRACERID());
		if (request.getsearchAll() > 0)
			cstmt.setInt(++i, request.getsearchAll());
		if (request.getincludeCancel() > 0)
			cstmt.setInt(++i, request.getincludeCancel());
		if (request.getdebug() > 0)
			cstmt.setInt(++i, request.getdebug());
	}

	private TradeMatch populateTradeMatch(ResultSet rs, int location) throws SQLException {

		TradeMatch tradeMatch = new TradeMatch();
		int i = 0;
		tradeMatch.setswapId(rs.getInt(++i));
		tradeMatch.setswapNumber(rs.getInt(++i));
		tradeMatch.setcustomerId(rs.getInt(++i));
		tradeMatch.setbookId(rs.getInt(++i));
		tradeMatch.setlegId(rs.getInt(++i));
		tradeMatch.seteventId(rs.getInt(++i));
		tradeMatch.setinstrId(rs.getInt(++i));
		tradeMatch.setinstitutionId(rs.getInt(++i));
		Timestamp tradeTimeStamp = rs.getTimestamp(++i);
		Date tradeDate = tradeTimeStamp == null ? null : new Date(
				tradeTimeStamp.getTime());
		tradeMatch.settradeDate(tradeDate); 
		tradeMatch.setquantity(rs.getFloat(++i));
		tradeMatch.setpriceBaseCcy(rs.getFloat(++i));
		tradeMatch.setpricePayCcy(rs.getFloat(++i));
		tradeMatch.setbookName(rs.getString(++i));
		tradeMatch.setticker(rs.getString(++i));
		tradeMatch.setmatchId(rs.getInt(++i));
		tradeMatch.seteventIdMatch(rs.getInt(++i));
		Timestamp MatchSettleTimeStamp = rs.getTimestamp(++i);
		Date matchSettleDate = MatchSettleTimeStamp == null ? null : new Date(
				MatchSettleTimeStamp.getTime());
		tradeMatch.setmatchSettleDate(matchSettleDate); 
		tradeMatch.setmatchQty(rs.getFloat(++i));
		tradeMatch.setmatchRealizedAmt(rs.getFloat(++i));
		tradeMatch.setmatchRealizedAmtBase(rs.getFloat(++i));
		tradeMatch.setmatchAvgCost(rs.getFloat(++i));
		tradeMatch.setmatchAvgCostBase(rs.getFloat(++i));
		tradeMatch.setmatchAccountMethod(rs.getString(++i));
		tradeMatch.setmatchType(rs.getInt(++i));
		tradeMatch.setinstitutionName(RefCache.getInstitution(tradeMatch.getinstitutionId(),location));
		tradeMatch.setlotAge(rs.getInt(++i));
		return tradeMatch;

	}

}
