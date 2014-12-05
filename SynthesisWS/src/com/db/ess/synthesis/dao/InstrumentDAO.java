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
import com.db.ess.synthesis.dvo.GetInstrumentRequest;
import com.db.ess.synthesis.dvo.GetInstrumentResponse;
import com.db.ess.synthesis.dvo.Instrument;
import com.db.ess.synthesis.dvo.InstrumentAudit;
import com.db.ess.synthesis.dvo.GetInstrumentAuditRequest;
import com.db.ess.synthesis.dvo.GetInstrumentAuditResponse;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;
import com.db.ess.synthesis.util.SynthesisConstants;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;
import com.db.ess.synthesis.vo.Exchange;

public class InstrumentDAO extends SynthesisBaseDAO{

	private static Logger logger = Logger.getLogger(InstrumentDAO.class
			.getName());
	private static final String GET_INSTRUMENTAUDIT = "select ia.AuditTime, ia.Comment, "
			+ "ia.UserName from InstrumentAudit ia where ia.FiId = ?";
	
	private static final String GET_INSTRUMENTS = "{ call SYN_GetInstrument (";

	public GetInstrumentAuditResponse getAudit(GetInstrumentAuditRequest request) throws Exception {
		logger.info("Inside getAudit(), request received: "+request);
		GetInstrumentAuditResponse response = new GetInstrumentAuditResponse();

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
	
	public void getAudit(GetInstrumentAuditRequest request, int location, GetInstrumentAuditResponse response) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_INSTRUMENTAUDIT);
			stmt.setInt(1, request.getinstrumentId());
			long time = System.currentTimeMillis(); 
			rs = stmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				InstrumentAudit audit = new InstrumentAudit();
				int i = 0;
				audit.setinstrumentId(request.getinstrumentId());
				audit.setauditTime(rs.getDate(++i));
				audit.setcomment(rs.getString(++i));
				audit.setuserName(rs.getString(++i));
				response.setInstrumentAudit(audit);
			}

		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		
	}
	
	public GetInstrumentResponse getInstruments(GetInstrumentRequest request)  throws Exception{
		GetInstrumentResponse response = new GetInstrumentResponse();
		logger.info("Inside getInstruments(), request received: "+request);
		int location = request.getlocation();

		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getInstruments(request, ESSLocation.LONDON, response);
			break;
			
		case ESSLocation.NEWYORK:
			getInstruments(request, ESSLocation.NEWYORK, response);
			break;

		case ESSLocation.GLOBAL:
			getInstruments(request, ESSLocation.LONDON, response);
			getInstruments(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}
	public void getInstruments(GetInstrumentRequest request, int location, GetInstrumentResponse response)  throws Exception{
		
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
			while(rs.next()){
				if (count > SynthesisConstants.MAX_RESULT_SET_SIZE) {
					response.setreturnResponse(getResultSetOverFlowResponse());
					break;
				}
				response.setinstruments(populateInstrument(rs,location));
				count++;
			}
			logger.info("Found ["+count+"] Intruments For the given search criteria.");
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			ex.printStackTrace();
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
	}
	
	private String createQueryString(GetInstrumentRequest request) {
		StringBuilder qb = new StringBuilder(GET_INSTRUMENTS);
		StringBuilder log = new StringBuilder(GET_INSTRUMENTS);

		boolean paramSet = false;
		if (request.getisnstrumentName() != null) {
			qb.append("@InstrumentName = ?");
			String isnstrumentName = request.getisnstrumentName();
			String isnstrumentNameQuery = getWildCardSearchString(isnstrumentName,"upper(i.name)");
			log.append("@InstrumentName = " + isnstrumentNameQuery);
			paramSet = true;
		}
		if (request.getdescription() != null) {
			if (paramSet) {
				qb.append(", @Description = ?");
				String description = request.getdescription();
				String descriptionQuery = getWildCardSearchString(description,"upper(i.description)");
				log.append(", @Description = " + descriptionQuery);
			} else {
				qb.append("@Description = ?");
				String description = request.getdescription();
				String descriptionQuery = getWildCardSearchString(description,"upper(i.description)");
				log.append("@Description = " + descriptionQuery);
				paramSet = true;
			}
		}
		if (request.getissueCountry() != null) {
			if (paramSet) {
				qb.append(", @IssueCountry = ?");
				String issueCountry = request.getissueCountry();
				String issueCountryQuery = getWildCardSearchString(issueCountry,"upper(code)");
				log.append(", @IssueCountry = " + issueCountryQuery);
			} else {
				qb.append("@IssueCountry = ?");
				String issueCountry = request.getissueCountry();
				String issueCountryQuery = getWildCardSearchString(issueCountry,"upper(code)");
				log.append(" @IssueCountry = " + issueCountryQuery);
				paramSet = true;
			}
		}
		if (request.getextType() != null) {
			if (paramSet) {
				qb.append(", @ExtType = ?");
				log.append(", @ExtType = " + request.getextType());
			} else {
				qb.append("@ExtType = ?");
				log.append(" @ExtType = " + request.getextType());
				paramSet = true;
			}
		}
		if (request.getextValue() != null) {
			if (paramSet) {
				qb.append(", @ExtValue = ?");
				log.append(", @ExtValue = " + TaskHelper.getSecCode(request.getextValue()));
			} else {
				qb.append("@ExtValue = ?");
				log.append(" @ExtValue = " + TaskHelper.getSecCode(request.getextValue()));
			}
		}
		qb.append(")}");
		log.append(") } ");

		logger.info("Executing query:"+log.toString());

		return qb.toString();
	}
	
	private void setParameters(GetInstrumentRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		if (request.getisnstrumentName()!= null){
			String isnstrumentName = request.getisnstrumentName();
			String isnstrumentNameQuery = getWildCardSearchString(isnstrumentName,"upper(i.name)");
			logger.info("isnstrumentNameQuery from getWildCardSearchString : "+isnstrumentNameQuery);
			cstmt.setString(++i, isnstrumentNameQuery);
		}
		if (request.getdescription()!= null){
			String description = request.getdescription();
			String descriptionQuery = getWildCardSearchString(description,"upper(i.description)");
			logger.info("descriptionQuery from getWildCardSearchString : "+descriptionQuery);
			cstmt.setString(++i, descriptionQuery);
		}
		if (request.getissueCountry()!= null){
			String issueCountry = request.getissueCountry();
			String issueCountryQuery = getWildCardSearchString(issueCountry,"upper(code)");
			logger.info("issueCountryQuery from getWildCardSearchString : "+issueCountryQuery);
			cstmt.setString(++i, issueCountryQuery);
		}
		if (request.getextType() != null)
			cstmt.setString(++i, request.getextType());
		if (request.getextValue() != null
				&& request.getextValue().length() > 0)
		{
			String extValueQuery = TaskHelper.getSecCode(request.getextValue());
			logger.info("extValueQuery from TaskHelper.getSecCode : "+extValueQuery);
			cstmt.setString(++i, extValueQuery);
		}
	}
	
	private Instrument populateInstrument(ResultSet rs,int location) throws SQLException,Exception {
		Instrument instrument = new Instrument();
		instrument.setessLocation(location);
		int i = 0;
		instrument.setinstrumentName(rs.getString(++i));
		int indtrumentTypeId = rs.getInt(++i);
		instrument.setinstrumentType(RefCache.getFITypeDomain(indtrumentTypeId,location));
		instrument.setticker_RIC(rs.getString(++i));
		instrument.setRIC(rs.getString(++i));
		instrument.setCUSIP(rs.getString(++i));
		instrument.setISIN(rs.getString(++i));
		instrument.setSEDOL(rs.getString(++i));
		instrument.setlocalReference(rs.getString(++i));
		instrument.setdescription(rs.getString(++i));
		instrument.setissueCountry(rs.getString(++i));
		int primaryExchId = rs.getInt(++i);
		
		Exchange exchange = RefCache.getExchange(primaryExchId,location);
		if(exchange != null){
			instrument.setprimExchangeCode(exchange.getExchCode());
		}
		instrument.setmultiplier(rs.getDouble(++i));
		int statusId = rs.getInt(++i);
		instrument.setstatus(RefCache.getFIStatusDomain(statusId,location));
		instrument.setsicCode(rs.getString(++i));
		int priceFreqId = rs.getInt(++i);
		instrument.setpriceFrequency(RefCache.getPriceFreqDomain(priceFreqId,location));
		int priceTypeid = rs.getInt(++i);
		instrument.setpriceType(RefCache.getPriceTypeDomain(priceTypeid,location));
		instrument.setpriceSource(rs.getString(++i));
		int localCcyId = rs.getInt(++i);
		CachedCurrency locCur = null; 	
		if((locCur = RefCache.getCurrency(localCcyId,location))!=null)
			instrument.setlocalCcy(locCur.getSwiftCode());
		int tradeCcyId = rs.getInt(++i);
		CachedCurrency trdCur = null;
		if((trdCur = RefCache.getCurrency(tradeCcyId,location))!=null)
			instrument.settrdCcy(trdCur.getSwiftCode());
		int stlCcyId = rs.getInt(++i);
		CachedCurrency stlCur = null;
		if((stlCur = RefCache.getCurrency(stlCcyId,location))!=null)
			instrument.setstlCcy(stlCur.getSwiftCode());
		
		int tradeCtyId = rs.getInt(++i);
		instrument.settradeCountry(RefCache.getCountry(tradeCtyId,location));
		
		int issueCtyId = rs.getInt(++i);
		instrument.setissueCountry(RefCache.getCountry(issueCtyId,location));
		
		int lstActUsrId = rs.getInt(++i);
		instrument.setlastActivityUser(RefCache.getUserProfile(lstActUsrId,location));
		Timestamp lastActivityTimeStamp = rs.getTimestamp(++i);
		Date lastActivityDate = lastActivityTimeStamp == null ? null : new Date(
				lastActivityTimeStamp.getTime());
		instrument.setlastActivityTime(lastActivityDate);
		instrument.setliquiditySummary(rs.getString(++i));
		instrument.setinstrCcy(rs.getString(++i));
		instrument.setcountryCode(rs.getString(++i));
		instrument.setcountryName(rs.getString(++i));
		instrument.setsecurityType(rs.getString(++i));
		instrument.setissueCountryCode(rs.getString(++i));
		instrument.setissueCountryName(rs.getString(++i));
		instrument.setinstrId(rs.getInt(++i));
		
		return instrument;
	}
	
}
