package com.db.ess.synthesis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.TaskHelper;
import com.db.ess.synthesis.dvo.CreateDividendRequest;
import com.db.ess.synthesis.dvo.CreateDividendResponse;
import com.db.ess.synthesis.dvo.DeletedDividend;
import com.db.ess.synthesis.dvo.Dividend;
import com.db.ess.synthesis.dvo.DividendAudit;
import com.db.ess.synthesis.dvo.GetActionSpillQueueRequest;
import com.db.ess.synthesis.dvo.GetActionSpillQueueResponse;
import com.db.ess.synthesis.dvo.GetDeletedDividendRequest;
import com.db.ess.synthesis.dvo.GetDeletedDividendResponse;
import com.db.ess.synthesis.dvo.GetDividendAuditRequest;
import com.db.ess.synthesis.dvo.GetDividendAuditResponse;
import com.db.ess.synthesis.dvo.GetDividendRequest;
import com.db.ess.synthesis.dvo.GetDividendResponse;
import com.db.ess.synthesis.dvo.GetImpactedSwapRequest;
import com.db.ess.synthesis.dvo.GetImpactedSwapResponse;
import com.db.ess.synthesis.dvo.GetUpdateImpactedSwapRequest;
import com.db.ess.synthesis.dvo.GetUpdateImpactedSwapResponse;
import com.db.ess.synthesis.dvo.ImpactedSwap;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;
import com.db.ess.synthesis.util.SynthesisConstants;



public class DividendDAO extends SynthesisBaseDAO {
	
	
	private static final Logger logger = Logger.getLogger(DividendDAO.class.getName());
	private static final String GET_DIVIDEND = "{ call dbo.SYN_GetDividend(";
	private static final String ACTION_SPILL_QUEUE ="{ call dbo.SYN_ActionSpillQueue(";
	private static final String CREATE_DIVIDEND="{ call dbo.SYN_CreateDividend (";
	private static final String GET_IMPACTED_SWAPS="{ call dbo.SYN_GetImpactedSwaps (";
	private static final String GET_DELETED_DIVIDEND="{ call dbo.SYN_GetDeletedDividends (";
	private static final String GET_UPDATE_IMPACTED_SWAP="{ call dbo.SYN_UpdateImpactedSwaps (";
	//private static final String GET_DIVIDEND_AUDIT = "{ call dbo.SYN_GetDividendAudit(@Ticker=?,@EnterTime=?) }";
	private static final String GET_DIVIDEND_AUDIT = "select actionType,changeDescription,underlyingTicker,ticker," +
			"divCurrency,divGrossRate,divNetRate,exDate,recordDate,paymentDate,dividendSource,feedId," +
			"userComments,userIdApply,changeApplyTime,feedVendorComment from SYN_DividendAudit " +
			" where ";
	
	
	public GetDividendResponse getDividend (GetDividendRequest request) throws Exception {

		GetDividendResponse response = new GetDividendResponse();
		logger.info("Inside getDividend...");
		  int location = request.getlocation();
		  switch (location) {

			    case ESSLocation.UNKNOWN:
			    	location = ESSLocation.LONDON;
			    // Fall thru using london as a default.
			    case ESSLocation.LONDON:
			    	getDividend(request, ESSLocation.LONDON, response);
			    break;
			    case ESSLocation.NEWYORK:
			    	getDividend(request, ESSLocation.NEWYORK, response);
			    break;
			    case ESSLocation.GLOBAL:
			    	getDividend(request, ESSLocation.LONDON, response);
			    	getDividend(request, ESSLocation.NEWYORK, response);
			    break;
			    default:
			    	throw new Exception("Invalid or Unknown location.");

		  }
		 return response;

		}
	
	public GetDividendResponse getDividend(GetDividendRequest request,
			int location, GetDividendResponse response) throws Exception {
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
					    Dividend dividend = populateDividend(rs,location);
					    response.setdividends(dividend);
					    count++;
					}

			logger.info("Found ["+count+"] Dividend For the given search criteria.");
			} catch (Exception ex) {
				logger.error("Failed to execute query: ", ex); 
				throw new Exception(ex);
			} finally {
				SQLUtils.closeResources(conn, cstmt, rs);
				}
		  return response;

		}
	
	
	private String createQueryString(GetDividendRequest request) {
		StringBuilder qb = new StringBuilder(GET_DIVIDEND);
		StringBuilder log = new StringBuilder(GET_DIVIDEND);
		qb.append("@UserId = ?");
		if (request.getstatus() != null) {
			qb.append(", @Status = ?");
			log.append(", @Status = " + request.getstatus());
		}
		if (request.getdateType() != null) {
			qb.append(", @DateType = ?");
			log.append(", @DateType = " + request.getdateType());
		}
		if (request.getdateValueFrom() != null &&request.getdateValueFrom().length()>0){
			qb.append(", @DateValueFrom = ?");
			log.append(", @DateValueFrom = " + request.getdateValueFrom());
		}
		if (request.getdateValueTo() != null){
			qb.append(", @DateValueTo = ?");
			log.append(", @DateValueTo = " + request.getdateValueTo());
			}
		if (request.getsecCodeType() != null) {
			qb.append(", @SecCodeType = ?");
			log.append(", @SecCodeType = " + request.getsecCodeType());
		}
		if (request.getsecCode() != null) {
			qb.append(", @SecCode = ?");
			log.append(", @SecCode = " + TaskHelper.getSecCode(request.getsecCode()));
		}
		if (request.getinstrCcy()!= null && request.getinstrCcy().length() > 0) {
			qb.append(", @InstrCcy = ?");
			String instrCcy = request.getinstrCcy();
			String instrCcyQuery = getWildCardSearchString(instrCcy,"swiftCode");
			log.append(", @InstrCcy = " + instrCcyQuery);
		}
		if (request.getinstrCountry() != null) {
			qb.append(", @InstrCountry = ?");
			String instrCountry = request.getinstrCountry();
			String instrCountryQuery = getWildCardSearchString(instrCountry,"upper(ctry.code)");
			log.append(", @InstrCountry = " + instrCountryQuery);
		}
		
		if (request.getissueCountry()!= null) {
			qb.append(", @IssueCountry = ?");
			String issueCountry = request.getissueCountry();
			String issueCountryQuery = getWildCardSearchString(issueCountry,"upper(ictry.code)");
			log.append(", @IssueCountry = " + issueCountryQuery);
		}
		if (request.getdivCcy()!= null) {
			qb.append(", @DivCcy = ?");
			String divCcy = request.getdivCcy();
			String divCcyQuery = getWildCardSearchString(divCcy,"swiftCode");
			log.append(", @DivCcy = " + divCcyQuery);
		}
		if (request.getfeedId()!=null) {
			qb.append(", @FeedId = ?");
			log.append(", @FeedId = " + request.getfeedId());
		}
		if (request.getdivAmount() != 0) {
			qb.append(", @DivAmount = ?");
			log.append(", @DivAmount = " + request.getdivAmount());
		}
		if (request.getindexDividends() != 0) {
			qb.append(", @IndexDividends = ?");
			log.append(", @IndexDividends = " + request.getindexDividends());
		}
		if (request.getspillCount() >= 0) {
			qb.append(", @SpillCount = ?");
			log.append(", @SpillCount = " + request.getspillCount());
		}
		if (request.getdivId() > 0) {
			qb.append(", @DivId = ?");
			log.append(", @DivId = " + request.getdivId());
		}
		qb.append(") } ");
		log.append(") } ");
		logger.info("Executing query:"+log.toString());
		return qb.toString();
	}
	
	private void setParameters(GetDividendRequest request,
			CallableStatement cstmt) throws SQLException {
			int i = 0;
			cstmt.setInt(++i, request.getuserId());
			if (request.getstatus()!= null && request.getstatus().length() > 0)
				cstmt.setString(++i, request.getstatus());
			if (request.getdateType()!= null && request.getdateType().length() > 0)
				cstmt.setString(++i, request.getdateType());
			if (request.getdateValueFrom() != null && request.getdateValueFrom().length() > 0)
				cstmt.setString(++i, request.getdateValueFrom());
			if (request.getdateValueTo() != null && request.getdateValueTo().length() > 0)
				cstmt.setString(++i, request.getdateValueTo());
			if (request.getsecCodeType() != null && request.getsecCodeType().length() > 0)
				cstmt.setString(++i, request.getsecCodeType());
			if (request.getsecCode() != null && request.getsecCode().length() > 0)
			{
				String secCodeQuery = TaskHelper.getSecCode(request.getsecCode());
				logger.info("secCodeQuery from TaskHelper.getSecCode : "+secCodeQuery);
				cstmt.setString(++i, secCodeQuery);
			}
			if (request.getinstrCcy()!= null && request.getinstrCcy().length() > 0){
				String instrCcy = request.getinstrCcy();
				String instrCcyQuery = getWildCardSearchString(instrCcy,"swiftCode");
				logger.info("instrCcyQuery from getWildCardSearchString : "+instrCcyQuery);
				cstmt.setString(++i, instrCcyQuery);
				}
			if (request.getinstrCountry()!= null && request.getinstrCountry().length() > 0){
				String instrCountry = request.getinstrCountry();
				String instrCountryQuery = getWildCardSearchString(instrCountry,"upper(ctry.code)");
				logger.info("instrCountryQuery from getWildCardSearchString : "+instrCountryQuery);
				cstmt.setString(++i, instrCountryQuery);
				}
			if (request.getissueCountry()!= null && request.getissueCountry().length() > 0){
				String issueCountry = request.getissueCountry();
				String issueCountryQuery = getWildCardSearchString(issueCountry,"upper(ictry.code)");
				logger.info("issueCountryQuery from getWildCardSearchString : "+issueCountryQuery);
				cstmt.setString(++i, issueCountryQuery);
				}
			if (request.getdivCcy() != null && request.getdivCcy().length() > 0){
				String divCcy = request.getdivCcy();
				String divCcyQuery = getWildCardSearchString(divCcy,"swiftCode");
				logger.info("divCcyQuery from getWildCardSearchString : "+divCcyQuery);
				cstmt.setString(++i, divCcyQuery);
				}
			if (request.getfeedId()!=null && request.getfeedId().length()>0)
				cstmt.setString(++i, request.getfeedId());
			if (request.getdivAmount()>0)
				cstmt.setDouble(++i, request.getdivAmount());
			if (request.getindexDividends()>0)
				cstmt.setInt(++i, request.getindexDividends());
			if (request.getspillCount()>=0)
				cstmt.setInt(++i, request.getspillCount());
			if (request.getdivId()>0)
				cstmt.setInt(++i, request.getdivId());
		}

	
	private Dividend populateDividend(ResultSet rs, int location) throws SQLException {
		
		
		Dividend dividend = new Dividend();
			int i = 0;
			dividend.setessLocation(Integer.toString(location));
			Timestamp exDateTimeStamp = rs.getTimestamp(++i);
			Date exDate = exDateTimeStamp == null ? null : new Date(
			exDateTimeStamp.getTime());
			dividend.setexDate(exDate); 
		
			Timestamp recDateTimeStamp = rs.getTimestamp(++i);
			Date recDate = recDateTimeStamp == null ? null : new Date(
			recDateTimeStamp.getTime());
			dividend.setrecDate(recDate);
			
			Timestamp payDateTimeStamp = rs.getTimestamp(++i);
			Date payDate = payDateTimeStamp == null ? null : new Date(
			payDateTimeStamp.getTime());
			dividend.setpayDate(payDate);
			
			Timestamp annDateTimeStamp = rs.getTimestamp(++i);
			Date annDate = annDateTimeStamp == null ? null : new Date(
			annDateTimeStamp.getTime());
			dividend.setannDate(annDate); 
			
			Timestamp updateDateTimeStamp = rs.getTimestamp(++i);
			Date updateDateTime = updateDateTimeStamp == null ? null : new Date(
			updateDateTimeStamp.getTime());
			dividend.setupdateDateTime(updateDateTime);
			
			dividend.setdivAmount(rs.getDouble(++i));
			dividend.setnetDivAmount(rs.getDouble(++i));
			dividend.settaxRate(rs.getDouble(++i));
			dividend.setdivCcy(rs.getString(++i));
			dividend.setindexTicker(rs.getString(++i));
			dividend.setindexDivisor(rs.getDouble(++i));
			dividend.setindexIncludedPct(rs.getDouble(++i));
			dividend.setindexShares(rs.getDouble(++i));
			dividend.setindexInvWeight(rs.getDouble(++i));
			dividend.setindexFxRate(rs.getDouble(++i));
			dividend.setunderDivAmount(rs.getDouble(++i));
			dividend.setunderDivCcy(rs.getString(++i));
			dividend.setdivId(rs.getInt(++i));
			dividend.setfeedId(rs.getString(++i));
			dividend.setInstrId(rs.getInt(++i));
			dividend.setsynthType(rs.getInt(++i));
			dividend.setsynthStatus(rs.getInt(++i));
			dividend.setsynthPendingSpill(rs.getInt(++i));
			dividend.setunderlyingId(rs.getInt(++i));
			dividend.setswapsEntitled(rs.getString(++i));
			dividend.setqueueDays(rs.getInt(++i));
			dividend.setspillStatus(rs.getString(++i));
			dividend.setconstTicker(rs.getString(++i));
			dividend.setdivSource(rs.getString(++i));
			dividend.setcomment(rs.getString(++i));
			dividend.settype(rs.getString(++i)); 
			return dividend;
		
		}
	
	public GetActionSpillQueueResponse getActionSpillQueue (GetActionSpillQueueRequest request) throws Exception {
	
		GetActionSpillQueueResponse response = new GetActionSpillQueueResponse();
		logger.info("Inside ActionSpillQUEUE...");
		  int location = request.getlocation();
		  switch (location) {

			    case ESSLocation.UNKNOWN:
			    	location = ESSLocation.LONDON;
			    // Fall thru using london as a default.
			    case ESSLocation.LONDON:
			    	getActionSpillQueue(request, ESSLocation.LONDON,response);
			    break;
			    case ESSLocation.NEWYORK:
			    	getActionSpillQueue(request, ESSLocation.NEWYORK,response);
			    break;
			    case ESSLocation.GLOBAL:
			    	getActionSpillQueue(request, ESSLocation.LONDON,response);
			    	getActionSpillQueue(request, ESSLocation.NEWYORK,response);
			    break;
			    default:
			    	throw new Exception("Invalid or Unknown location.");

		  }
	return response;

		}
	
	public GetActionSpillQueueResponse getActionSpillQueue(GetActionSpillQueueRequest request,
			int location,GetActionSpillQueueResponse response) throws Exception {
			Connection conn = null;
			CallableStatement cstmt = null;
			String query = createActionSpillQueryString(request);
			long time = System.currentTimeMillis(); 
			try {
					conn = SQLUtils.getConnection(location);
					cstmt = conn.prepareCall(query);
					setActionSpillParameters(request, cstmt);
					cstmt.execute();
					logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
				} catch (Exception ex) {
				logger.error("Failed to execute query: ", ex); 
				throw new Exception(ex);
			} finally {
				SQLUtils.closeResources(conn, cstmt, null);
				}
			return response;

		}
	
	private String createActionSpillQueryString(GetActionSpillQueueRequest request) {
		StringBuilder qb = new StringBuilder(ACTION_SPILL_QUEUE);
		StringBuilder log = new StringBuilder(ACTION_SPILL_QUEUE);
		
		qb.append("@FeedId = ?");
		log.append("@FeedId = " + request.getfeedId());
		if (request.getunderlyingId() >= 0) {
			qb.append(", @UnderlyingId = ?");
			log.append(", @UnderlyingId = " + request.getunderlyingId());
		}
		qb.append(", @IndexId = ?");
		log.append(", @IndexId = " + request.getindexId());
		qb.append(", @DivId= ?");
		log.append(", @DivId = " + request.getdivId());
		qb.append(", @Comment= ?");
		log.append(", @Comment = " + request.getcomment());
		if (request.getaction() != null && request.getaction().length()>0){
			qb.append(", @Action = ?");
			log.append(", @Action = " + request.getaction());
		}
		
		if(request.getstatus()>=0){ // status 1 means new Not Applied dividend spill
			qb.append(", @Status = ?");
			log.append(", @Status = " + request.getstatus());
			qb.append(", @ExDate = ?");
			log.append(", @ExDate = " + request.getexDate());
			qb.append(", @RecDate = ?");
			log.append(", @RecDate = " + request.getrecDate());
			qb.append(", @PaymentDate = ?");
			log.append(", @PaymentDate = " + request.getpayDate());
			qb.append(", @Currency = ?");
			log.append(", @Currency = " + request.getcurrency());
		}
		
		qb.append(", @GrossAmt = ?");
		log.append(", @GrossAmt = " + request.getgrossAmount());
		qb.append(", @NetAmt = ?");
		log.append(", @NetAmt = " + request.getnetAmount());
		
		qb.append(", @UserId = ?");
		log.append(", @UserId = " + request.getuserId());
		
		if (request.getspillStatus() != null && request.getspillStatus().length()>0){
			qb.append(", @SpillStatus = ?");
			log.append(", @SpillStatus = " + request.getspillStatus());
		}
		
		qb.append(") } ");
		log.append(") } ");
		logger.info("Executing query:"+log.toString());
		return qb.toString();
		
	}
	private void setActionSpillParameters(GetActionSpillQueueRequest request,
			CallableStatement cstmt) throws SQLException {
			int i = 0;
			
			//modified the following condition to consider single stock dividends whose feedID is null(SYN-2638)
			if((request.getfeedId()== null)||((request.getfeedId().length())==0) || request.getfeedId().isEmpty()){
			     cstmt.setString(++i,null);
			   }else{
			     cstmt.setString(++i, request.getfeedId());
			   }

			if (request.getunderlyingId()>=0)
				cstmt.setInt(++i, request.getunderlyingId());
			cstmt.setInt(++i, request.getindexId());
			cstmt.setInt(++i, request.getdivId());
			cstmt.setString(++i, request.getcomment());
			if (request.getaction()!= null && request.getaction().length() > 0)
				cstmt.setString(++i, request.getaction());
			if (request.getstatus() >= 0){
				cstmt.setInt(++i, request.getstatus());
				cstmt.setDate(++i, new java.sql.Date(request.getexDate().getTime()));
				cstmt.setDate(++i, new java.sql.Date(request.getrecDate().getTime()));
				cstmt.setDate(++i, new java.sql.Date(request.getpayDate().getTime()));
				cstmt.setString(++i, request.getcurrency());
			}
			
			cstmt.setFloat(++i,request.getgrossAmount());
			cstmt.setFloat(++i,request.getnetAmount());
			cstmt.setInt(++i,request.getuserId());
			
			if (request.getspillStatus() != null && request.getspillStatus().length()>0){
				cstmt.setString(++i,request.getspillStatus());
			}
			
	}
	public CreateDividendResponse createDividend(CreateDividendRequest request)
		throws Exception {
		CreateDividendResponse response = new CreateDividendResponse();
	  logger.info("Inside CreateDividend... ");
	  int location = request.getlocation();
	  switch (location) {

		    case ESSLocation.UNKNOWN:
		    	location = ESSLocation.LONDON;
		    // Fall thru using london as a default.
		    case ESSLocation.LONDON:
		    	createDividend(request, ESSLocation.LONDON,response);
		    break;
		    case ESSLocation.NEWYORK:
		    	createDividend(request, ESSLocation.NEWYORK,response);
		    break;
		    case ESSLocation.GLOBAL:
		    	createDividend(request, ESSLocation.LONDON,response);
		    	createDividend(request, ESSLocation.NEWYORK,response);
		    break;
		    default:
		    	throw new Exception("Invalid or Unknown location.");

	  }
	  return response;
	
	}
	
		public CreateDividendResponse createDividend(CreateDividendRequest request,
			int location,CreateDividendResponse response) throws Exception {
			Connection conn = null;
			CallableStatement cstmt = null;
			String query = createDividendQueryString(request);
			long time = System.currentTimeMillis(); 
			try {
					conn = SQLUtils.getConnection(location);
					cstmt = conn.prepareCall(query);
					setCreateDividendParameters(request, cstmt);
					cstmt.execute();
					logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 

				} catch (Exception ex) {
					logger.error("Failed to execute query: ", ex); 
				throw new Exception(ex);
			} finally {
				SQLUtils.closeResources(conn, cstmt, null);
				}
			return response;
		}
	
	private String createDividendQueryString(CreateDividendRequest request) {
		StringBuilder qb = new StringBuilder(CREATE_DIVIDEND);
		StringBuilder log = new StringBuilder(CREATE_DIVIDEND);
			
		qb.append("@IndexTicker= ?");
		log.append("@IndexTicker = " + request.getindexTicker());
		
		if (request.getunderTicker() != null &&request.getunderTicker().length()>0){
			qb.append(", @UnderTicker= ?");
			log.append(", @UnderTicker = " + request.getunderTicker());
		}
		if (request.getexDate()!= null) {
			qb.append(", @ExDate  = ?");
			log.append(", @ExDate = " + request.getexDate());
		}
		if (request.getrecDate()!= null) {
			qb.append(", @RecDate  = ?");
			log.append(", @RecDate = " + request.getrecDate());
		}
		if (request.getpaymentDate()!= null) {
			qb.append(", @PaymentDate  = ?");
			log.append(", @PaymentDate = " + request.getpaymentDate());
		}
		if (request.getcurrency() != null &&request.getcurrency().length()>0){
			qb.append(", @Currency= ?");
			log.append(", @Currency = " + request.getcurrency());
		}
		if (request.getnetAmount() != 0) {
			qb.append(", @NetAmount = ?");
			log.append(", @NetAmount = " + request.getnetAmount());
		}
		if (request.getgrossAmount() != 0) {
			qb.append(", @GrossAmount = ?");
			log.append(", @GrossAmount = " + request.getgrossAmount());
		}
		
		qb.append(", @TaxRate = ?");
		log.append(", @TaxRate = " + request.gettaxRate());
	
		qb.append(", @Comment= ?");
		log.append(", @Comment = " + request.getcomment());
				
		qb.append(", @FeedId = ?");
		log.append(", @FeedId = " + request.getfeedId());
		
		qb.append(", @UserId = ?");
		log.append(", @UserId = " + request.getuserId());
		
		qb.append(") } ");
		log.append(") } ");
		logger.info("Executing query:"+log.toString());
		return qb.toString();
		
	}
	private void setCreateDividendParameters(CreateDividendRequest request,
			CallableStatement cstmt) throws SQLException {
			int i = 0;
			
			if(((request.getindexTicker().length())==0) || request.getindexTicker().isEmpty()){
				cstmt.setString(++i, null);
			}else{
				cstmt.setString(++i, request.getindexTicker());
			}
			if (request.getunderTicker()!= null && request.getunderTicker().length() > 0)
				cstmt.setString(++i, request.getunderTicker());
			if (request.getexDate() != null)
				cstmt.setDate(++i, new java.sql.Date(request.getexDate()
						.getTime()));
			if (request.getrecDate() != null)
				cstmt.setDate(++i, new java.sql.Date(request.getrecDate()
						.getTime()));
			if (request.getpaymentDate() != null)
				cstmt.setDate(++i, new java.sql.Date(request.getpaymentDate()
						.getTime()));
			if (request.getcurrency()!= null && request.getcurrency().length() > 0)
				cstmt.setString(++i, request.getcurrency());
			
			if (request.getnetAmount()!=0)
				cstmt.setDouble(++i, request.getnetAmount());
			if (request.getgrossAmount()!=0)
				cstmt.setDouble(++i, request.getgrossAmount());
			
			cstmt.setDouble(++i, request.gettaxRate());
			cstmt.setString(++i, request.getcomment());
			cstmt.setString(++i, request.getfeedId());
			cstmt.setInt(++i, request.getuserId());
			
		}

	public GetImpactedSwapResponse getImpactedSwap (GetImpactedSwapRequest request) throws Exception {

		GetImpactedSwapResponse response = new GetImpactedSwapResponse();
		logger.info("Inside getImpactedSwap...");
		  int location = request.getlocation();
		  switch (location) {

			    case ESSLocation.UNKNOWN:
			    	location = ESSLocation.LONDON;
			    // Fall thru using london as a default.
			    case ESSLocation.LONDON:
			    	getImpactedSwap(request, ESSLocation.LONDON, response);
			    break;
			    case ESSLocation.NEWYORK:
			    	getImpactedSwap(request, ESSLocation.NEWYORK, response);
			    break;
			    case ESSLocation.GLOBAL:
			    	getImpactedSwap(request, ESSLocation.LONDON, response);
			    	getImpactedSwap(request, ESSLocation.NEWYORK, response);
			    break;
			    default:
			    	throw new Exception("Invalid or Unknown location.");

		  }
		 return response;

		}
	
	public GetImpactedSwapResponse getImpactedSwap(GetImpactedSwapRequest request,
			int location, GetImpactedSwapResponse response) throws Exception {
			Connection conn = null;
			CallableStatement cstmt = null;
			ResultSet rs = null;
			String query = createImpactedSwapQueryString(request);
			long time = System.currentTimeMillis(); 
			try {
					conn = SQLUtils.getConnection(location);
					cstmt = conn.prepareCall(query);
					setImpactedSwapParameters(request, cstmt);
					rs = cstmt.executeQuery();
					logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
					int count = 0;
					while (rs.next()) {
						if (count > SynthesisConstants.MAX_RESULT_SET_SIZE) {
							response.setreturnResponse(getResultSetOverFlowResponse());
							break;
						}
					    ImpactedSwap impactedSwap = populateImpactedSwaps(rs,location);
					    response.setimpactedSwap(impactedSwap);
					    count++;
					}

			logger.info("Found ["+count+"] ImpactedSwaps For the given search criteria.");
			} catch (Exception ex) {
				logger.error("Failed to execute query: ", ex); 
				throw new Exception(ex);
			} finally {
				SQLUtils.closeResources(conn, cstmt, rs);
				}
		  return response;

		}
	
	
	private String createImpactedSwapQueryString(GetImpactedSwapRequest request) {
		return "";
		
	
	}
	
	private void setImpactedSwapParameters(GetImpactedSwapRequest request,
			CallableStatement cstmt) throws SQLException {
			
		}

	
	private ImpactedSwap populateImpactedSwaps(ResultSet rs, int location) throws SQLException {
		
	return null;
		
		}
	
	public GetDeletedDividendResponse getDeletedDividends (GetDeletedDividendRequest request) throws Exception {

		GetDeletedDividendResponse response = new GetDeletedDividendResponse();
		logger.info("Inside getDeletedDividend...");
		  int location = request.getlocation();
		  switch (location) {

			    case ESSLocation.UNKNOWN:
			    	location = ESSLocation.LONDON;
			    // Fall thru using london as a default.
			    case ESSLocation.LONDON:
			    	getDeletedDividends(request, ESSLocation.LONDON, response);
			    break;
			    case ESSLocation.NEWYORK:
			    	getDeletedDividends(request, ESSLocation.NEWYORK, response);
			    break;
			    case ESSLocation.GLOBAL:
			    	getDeletedDividends(request, ESSLocation.LONDON, response);
			    	getDeletedDividends(request, ESSLocation.NEWYORK, response);
			    break;
			    default:
			    	throw new Exception("Invalid or Unknown location.");

		  }
		 return response;

		}
	
	public GetDeletedDividendResponse getDeletedDividends(GetDeletedDividendRequest request,
			int location, GetDeletedDividendResponse response) throws Exception {
			Connection conn = null;
			CallableStatement cstmt = null;
			ResultSet rs = null;
			String query = createDeletedDividendsQueryString(request);
			long time = System.currentTimeMillis(); 
			try {
					conn = SQLUtils.getConnection(location);
					cstmt = conn.prepareCall(query);
					setDeletedDividendsParameters(request, cstmt);
					rs = cstmt.executeQuery();
					logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
					int count = 0;
					while (rs.next()) {
						if (count > SynthesisConstants.MAX_RESULT_SET_SIZE) {
							response.setreturnResponse(getResultSetOverFlowResponse());
							break;
						}
					    DeletedDividend deletedDividend = populateDeletedDividends(rs, location);
					    response.setdeletedDividends(deletedDividend);
					    count++;
					}

			logger.info("Found ["+count+"] DividendSpill For the given search criteria.");
			} catch (Exception ex) {
				logger.error("Failed to execute query: ", ex); 
				throw new Exception(ex);
			} finally {
				SQLUtils.closeResources(conn, cstmt, rs);
				}
		  return response;

		}
	private String createDeletedDividendsQueryString(GetDeletedDividendRequest request) {
		StringBuilder qb = new StringBuilder(GET_DELETED_DIVIDEND);
		StringBuilder log = new StringBuilder(GET_DELETED_DIVIDEND);
		boolean paramSet = false;
			qb.append("@FeedId = ?");
			log.append("@FeedId = " + request.getfeedId());
			paramSet = true;
	
			if (paramSet) {
				qb.append(", @UnderlyingId = ?");
				log.append(", @UnderlyingId = " + request.getunderlyingId());
			}
			else{
				qb.append("@UnderlyingId = ?");
				log.append("@UnderlyingId = " + request.getunderlyingId());
				paramSet = true;
			}
	
			if (paramSet){
				qb.append(", @InstrId = ?");
				log.append(", @InstrId = " + request.getinstrId());
			}
			else{
				qb.append("@InstrId = ?");
				log.append("@InstrId = " + request.getinstrId());
				paramSet = true;
			}
			
				qb.append(", @ExDate = ?");
				log.append(", @ExDate = " + request.getexDate());
				
				qb.append(", @RecDate = ?");
				log.append(", @RecDate = " + request.getrecDate());
				
				qb.append(", @PaymentDate = ?");
				log.append(", @PaymentDate = " + request.getpayDate());
				
				qb.append(", @AnnounceDate = ?");
				log.append(", @AnnounceDate = " + request.getannDate());
				
				qb.append(", @DivSource = ?");
				log.append(", @DivSource = " + request.getdivSource());
				
				qb.append(", @Currency = ?");
				log.append(", @Currency = " + request.getcurrency());
				
				qb.append(", @UnderDivRate = ?");
				log.append(", @UnderDivRate = " + request.getunderDivRate());
				
				qb.append(", @TaxRate = ?");
				log.append(", @TaxRate = " + request.gettaxRate());
				
				qb.append(", @Comment = ?");
				log.append(", @Comment = " + request.getcomment());
			
		qb.append(") } ");
		log.append(") } ");
		logger.info("Executing query"+log.toString());
		return qb.toString();
	}
	
	private void setDeletedDividendsParameters(GetDeletedDividendRequest request,
			CallableStatement cstmt) throws SQLException {
			int i = 0;
				if(((request.getfeedId().length())==0 )|| request.getfeedId().isEmpty()){
					cstmt.setString(++i, null);
				}else{
					cstmt.setString(++i, request.getfeedId());
				}
				cstmt.setInt(++i, request.getunderlyingId());
				cstmt.setInt(++i, request.getinstrId());
				cstmt.setDate(++i, new java.sql.Date(request.getexDate().getTime()));
				cstmt.setDate(++i, new java.sql.Date(request.getrecDate().getTime()));
				cstmt.setDate(++i, new java.sql.Date(request.getpayDate().getTime()));
				cstmt.setDate(++i, new java.sql.Date(request.getannDate().getYear()<1900?System.currentTimeMillis():request.getannDate().getTime()));
				cstmt.setString(++i, request.getdivSource());
				cstmt.setString(++i, request.getcurrency());
				cstmt.setFloat(++i, request.getunderDivRate());
				cstmt.setString(++i, request.gettaxRate());
				cstmt.setString(++i, request.getcomment());
		}

	
	private DeletedDividend populateDeletedDividends(ResultSet rs, int location) throws SQLException {
		
		
			DeletedDividend deletedDividends = new DeletedDividend();
			int i = 0;
			deletedDividends.setticker(rs.getString(++i));
			deletedDividends.setconstTicker(rs.getString(++i));
			deletedDividends.setdivSource(rs.getString(++i));
			deletedDividends.setcurrency(rs.getString(++i));
			
			Timestamp exDateTimeStamp = rs.getTimestamp(++i);
			Date exDate = exDateTimeStamp == null ? null : new Date(
			exDateTimeStamp.getTime());
			deletedDividends.setexDate(exDate);
			
			Timestamp recDateTimeStamp = rs.getTimestamp(++i);
			Date recDate = recDateTimeStamp == null ? null : new Date(
			recDateTimeStamp.getTime());
			deletedDividends.setrecDate(recDate);
			
			Timestamp payDateTimeStamp = rs.getTimestamp(++i);
			Date payDate = payDateTimeStamp == null ? null : new Date(
			payDateTimeStamp.getTime());
			deletedDividends.setpayDate(payDate);
			
			Timestamp annDateTimeStamp = rs.getTimestamp(++i);
			Date annDate = annDateTimeStamp == null ? null : new Date(
			annDateTimeStamp.getTime());
			deletedDividends.setannDate(annDate); 
			
			deletedDividends.setgross(rs.getFloat(++i));
			deletedDividends.setdivAmount(rs.getFloat(++i));
			deletedDividends.setuserComment(rs.getString(++i));
			deletedDividends.setcomment(rs.getString(++i));
			deletedDividends.setunderlyingId(rs.getInt(++i));
			deletedDividends.setfiId(rs.getInt(++i));
			deletedDividends.setfeedId(rs.getString(++i));
			deletedDividends.setunderDivRate(rs.getFloat(++i));
			deletedDividends.settaxRate(rs.getString(++i));
			return deletedDividends;
		
		}
	
	public GetUpdateImpactedSwapResponse getUpdateImpactedSwap (GetUpdateImpactedSwapRequest request) throws Exception {

		GetUpdateImpactedSwapResponse response = new GetUpdateImpactedSwapResponse();
		logger.info("Inside getUpdateImpactedSwap...");
		  int location = request.getlocation();
		  switch (location) {

			    case ESSLocation.UNKNOWN:
			    	location = ESSLocation.LONDON;
			    // Fall thru using london as a default.
			    case ESSLocation.LONDON:
			    	getUpdateImpactedSwap(request, ESSLocation.LONDON, response);
			    break;
			    case ESSLocation.NEWYORK:
			    	getUpdateImpactedSwap(request, ESSLocation.NEWYORK, response);
			    break;
			    case ESSLocation.GLOBAL:
			    	getUpdateImpactedSwap(request, ESSLocation.LONDON, response);
			    	getUpdateImpactedSwap(request, ESSLocation.NEWYORK, response);
			    break;
			    default:
			    	throw new Exception("Invalid or Unknown location.");

		  }
		 return response;

		}
	public GetUpdateImpactedSwapResponse getUpdateImpactedSwap(GetUpdateImpactedSwapRequest request,
			int location, GetUpdateImpactedSwapResponse response) throws Exception {
			Connection conn = null;
			CallableStatement cstmt = null;
			String query = createUpdateImpactedSwapQuery(request);
			long time = System.currentTimeMillis(); 
			try {
				conn = SQLUtils.getConnection(location);
				cstmt = conn.prepareCall(query);
				setUpdateImpactedSwapParameters(request, cstmt);
				cstmt.execute();
				logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			} catch (Exception ex) {
				logger.error("Failed to execute query: ", ex); 
				throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, null);
			}
		return response;

	}
	
	
	private String createUpdateImpactedSwapQuery(GetUpdateImpactedSwapRequest request) {
		StringBuilder qb = new StringBuilder(GET_UPDATE_IMPACTED_SWAP);
		StringBuilder log = new StringBuilder(GET_UPDATE_IMPACTED_SWAP);
		
		if (request.getswapDetail()!= null && request.getswapDetail().length()>0) {
			qb.append("@SwapDetail = ?");
			log.append("@SwapDetail = " + request.getswapDetail());
		}
		
		qb.append(") } ");
		log.append(") } ");
		logger.info("Executing query:"+log.toString());
		return qb.toString();
	}
	
	private void setUpdateImpactedSwapParameters(GetUpdateImpactedSwapRequest request,
			CallableStatement cstmt) throws SQLException {
			int i = 0;
			if (request.getswapDetail()!= null && request.getswapDetail().length() > 0)
				cstmt.setString(++i, request.getswapDetail());
	}
	
	
	
	
	public GetDividendAuditResponse getDividendAudit (GetDividendAuditRequest request) throws Exception {
		
		GetDividendAuditResponse response = new GetDividendAuditResponse();
		int location = request.getlocation();
		switch (location) {
 	        case ESSLocation.UNKNOWN:
			    location = ESSLocation.LONDON;
			case ESSLocation.LONDON:
				getDividendAudit(request, ESSLocation.LONDON, response);
			    break;
		    case ESSLocation.NEWYORK:
		    	getDividendAudit(request, ESSLocation.NEWYORK, response);
		        break;
			case ESSLocation.GLOBAL:
				getDividendAudit(request, ESSLocation.LONDON, response);
				getDividendAudit(request, ESSLocation.NEWYORK, response);
			    break;
			default:
			    throw new Exception("Invalid or Unknown location.");
		}
	    return response;
	}
	
	public GetDividendAuditResponse getDividendAudit(GetDividendAuditRequest request,
			int location, GetDividendAuditResponse response) throws Exception {
			Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			String query = createDividendAuditQuery(request);
			stmt = conn.prepareStatement(query);
			if(request.getbyDefault())
			{
				stmt.setString(1, request.getticker());
				if(request.getexDate()!=null)
					stmt.setDate(2, new java.sql.Date(request.getexDate().getTime()));
				else
					stmt.setDate(2, null);
				stmt.setString(3,request.getfeedId());
				if(request.getunderTicker()!=null && !request.getunderTicker().isEmpty())
					stmt.setString(4,request.getunderTicker());
			}
			else if(request.getbyTicker())
			{
				stmt.setString(1, request.getticker());
			}
			else if (request.getbyDate())
			{
				stmt.setDate(1, new java.sql.Date(request.getauditFromDate().getTime()));
				stmt.setDate(2, new java.sql.Date(request.getauditToDate().getTime()));
			}
			
			rs = stmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			int count = 0;
			while (rs.next()) {
				if (count > SynthesisConstants.MAX_RESULT_SET_SIZE) {
					response.setreturnResponse(getResultSetOverFlowResponse());
					break;
				}
			    DividendAudit da = new DividendAudit();
			    da.setactionType(rs.getString(1));
			    da.setchangeDescription(rs.getString(2));
			    da.setunderlyingTicker(rs.getString(3));
			    da.setticker(rs.getString(4));
			    da.setdivCurrency(rs.getString(5));
			    da.setdivGrossRate(rs.getFloat(6));
			    da.setdivNetRate(rs.getFloat(7));
			    
			    Timestamp exDateTimeStamp = rs.getTimestamp(8);
				Date exDate = exDateTimeStamp == null ? null : new Date(exDateTimeStamp.getTime());
			    da.setexDate(exDate);
			    
			    Timestamp recordDateTimeStamp = rs.getTimestamp(9);
				Date recordDate = recordDateTimeStamp == null ? null : new Date(recordDateTimeStamp.getTime());
			    da.setrecordDate(recordDate);
			    
			    Timestamp paymentDate = rs.getTimestamp(10);
				Date payDate = paymentDate == null ? null : new Date(paymentDate.getTime());
			    da.setpaymentDate(payDate);
			    
			    da.setdivSource(rs.getString(11));
			    da.setfeedId(rs.getString(12));
			    			    
			    da.setuserComments(rs.getString(13));
			    da.setuserIdApply(rs.getString(14));
			    
			    Timestamp applyDateTime = rs.getTimestamp(15);
				Date applyDate = applyDateTime == null ? null : new Date(applyDateTime.getTime());
			    da.setchangeApplyTime(applyDate);
			    
			    da.setfeedVendorComment(rs.getString(16));
			    
			    response.setdividendAudits(da);
			    count++;
			}
			
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
 	    } finally {
			SQLUtils.closeResources(conn, stmt, null);
		}
		return response;
	}

	private String createDividendAuditQuery(GetDividendAuditRequest request) {
		StringBuilder qb = new StringBuilder(GET_DIVIDEND_AUDIT);
		StringBuilder log = new StringBuilder(GET_DIVIDEND_AUDIT);
		
		if(request.getbyDefault())
		{
			qb.append(" ticker=? ");
			log.append(" ticker = " + request.getticker());
			qb.append(" and exDate=? ");
			log.append(" and exDate = " + request.getexDate());
			qb.append(" and feedId= ? ");
			log.append(" and feedId = " + request.getfeedId());
			if(request.getunderTicker()!=null && !request.getunderTicker().isEmpty()) {
				qb.append(" and underlyingTicker= ? ");
				log.append(" and underlyingTicker = " + request.getunderTicker());
			}
		}
		else if(request.getbyTicker())
		{
			qb.append(" ticker=? ");
			log.append(" ticker = " + request.getticker());
		}
		else if(request.getbyDate() && request.getauditFromDate()!=null && request.getauditToDate()!=null)
		{
			qb.append(" changeApplyTime>=? and changeApplyTime<?");
			log.append(" changeApplyTime <= " + request.getauditFromDate());
			log.append("and  changeApplyTime > " + request.getauditToDate());
		}
		
		qb.append(" order by changeApplyTime asc ");
		log.append(" order by changeApplyTime asc ");
		
		logger.info("Executing query:"+log.toString());
		return qb.toString();
	}
	
}




