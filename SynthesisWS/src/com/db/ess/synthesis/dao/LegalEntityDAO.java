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
import com.db.ess.synthesis.dvo.GetLegalEntityAuditRequest;
import com.db.ess.synthesis.dvo.GetLegalEntityAuditResponse;
import com.db.ess.synthesis.dvo.GetLegalEntityRequest;
import com.db.ess.synthesis.dvo.GetLegalEntityResponse;
import com.db.ess.synthesis.dvo.GetLESettlementInstructionRequest;
import com.db.ess.synthesis.dvo.GetLESettlementInstructionResponse;
import com.db.ess.synthesis.dvo.LegalEntity;
import com.db.ess.synthesis.dvo.LegalEntityAudit;
import com.db.ess.synthesis.dvo.LESettlementInstruction;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;
import com.db.ess.synthesis.util.SynthesisConstants;

public class LegalEntityDAO extends SynthesisBaseDAO {

	private static Logger logger = Logger.getLogger(LegalEntityDAO.class
			.getName());
	static private final String GET_LEGALENTITYAUDIT = "select ea.AuditTime, ea.Comment, ea.UserName "
			+ "from EntityAudit ea where ea.LegalEntityId = ?";

	static private final String GET_LEGALENTITY = "{ call dbo.SYN_GetLegalEntity (";

	static private final String GET_SETTLEMENTINSTRUCTIONS = "{ call dbo.SYN_GetSettlementInstructions (";

	public GetLegalEntityResponse getLegalEntity(GetLegalEntityRequest request)
			throws Exception {
		logger.info("Inside getLegalEntity(), request received: " + request);
		GetLegalEntityResponse response = new GetLegalEntityResponse();

		int location = request.getlocation();

		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getLegalEntity(request, location, response);
			break;

		case ESSLocation.NEWYORK:
			getLegalEntity(request, location, response);
			break;

		case ESSLocation.GLOBAL:
			getLegalEntity(request, ESSLocation.LONDON, response);
			getLegalEntity(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public void getLegalEntity(GetLegalEntityRequest request, int location,
			GetLegalEntityResponse response) throws Exception {

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
				response.setlegalEntities(populateLegalEntity(rs));
				count++;
			}
			logger.info("Found [" + count+ "] LegalEntities For the given search criteria.");
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
	}

	public GetLESettlementInstructionResponse getLESettlementInstruction(
			GetLESettlementInstructionRequest request) throws Exception {
		logger.info("Inside getLESettlementInstruction(), request recieved: "
				+ request);
		GetLESettlementInstructionResponse response = new GetLESettlementInstructionResponse();

		int location = request.getlocation();

		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getLESettlementInstruction(request, location, response);
			break;

		case ESSLocation.NEWYORK:
			getLESettlementInstruction(request, location, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public void getLESettlementInstruction(
			GetLESettlementInstructionRequest request, int location,
			GetLESettlementInstructionResponse response) throws Exception {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = createSettlementInstructionQueryString(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setSettlementInstructionParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				LESettlementInstruction settlementInstruction = populateSettlementInstruction(rs);
				response.setsettlementInstructions(settlementInstruction);
			}
		} catch (Exception ex) {
		
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}

	}

	private String createSettlementInstructionQueryString(
			GetLESettlementInstructionRequest request) {
		StringBuilder qb = new StringBuilder(GET_SETTLEMENTINSTRUCTIONS);
		StringBuilder log = new StringBuilder(GET_SETTLEMENTINSTRUCTIONS);

		qb.append("? )}");
		log.append("@LegalEntityId = " + request.getlegalEntityId());
		log.append(") } ");
		logger.info("Executing query"+log.toString());
		return qb.toString();
	}

	private void setSettlementInstructionParameters(
			GetLESettlementInstructionRequest request, CallableStatement cstmt)
			throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getlegalEntityId());
	}

	private LESettlementInstruction populateSettlementInstruction(ResultSet rs)
			throws SQLException {

		LESettlementInstruction settlementInstruction = new LESettlementInstruction();

		int i = 0;
		settlementInstruction.setsettlementId(rs.getInt(++i));
		settlementInstruction.setlegalEntityId(rs.getInt(++i));
		settlementInstruction.setfiTypeCode(rs.getString(++i));
		settlementInstruction.setswiftCode(rs.getString(++i));
		settlementInstruction.setmedium(rs.getString(++i));
		settlementInstruction.setinstruct1(rs.getString(++i));
		settlementInstruction.setinstruct2(rs.getString(++i));
		settlementInstruction.setinstruct3(rs.getString(++i));
		settlementInstruction.setinstruct4(rs.getString(++i));
		settlementInstruction.setinstruct5(rs.getString(++i));
		settlementInstruction.setinstruct6(rs.getString(++i));
		settlementInstruction.setinstruct7(rs.getString(++i));
		settlementInstruction.setinstruct8(rs.getString(++i));
		settlementInstruction.setinstruct9(rs.getString(++i));
		settlementInstruction.setinstruct10(rs.getString(++i));
		settlementInstruction.setenterUserId(rs.getInt(++i));
		Timestamp enterTimeStamp = rs.getTimestamp(++i);
		Date enterTime = enterTimeStamp == null ? null : new Date(
				enterTimeStamp.getTime());
		settlementInstruction.setenterTime(enterTime);
		settlementInstruction.setlastActivityUserId(rs.getInt(++i));
		Timestamp lastActivityTimeStamp = rs.getTimestamp(++i);
		Date lastActivityTime = lastActivityTimeStamp == null ? null
				: new Date(lastActivityTimeStamp.getTime());
		settlementInstruction.setlastActivityTime(lastActivityTime);
		return settlementInstruction;
	}

	private void setParameters(GetLegalEntityRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		if (request.getinstitution()!= null){
			String institution = request.getinstitution();
			String institutionQuery = TaskHelper.convert(institution,"upper(b.name)",false);
			logger.info("institutionQuery from getWildCardSearchString : "+institutionQuery);
			cstmt.setString(++i, institutionQuery);
		}
		if (request.getlegalEntity()!= null){
			String legalEntity = request.getlegalEntity();
			String legalEntityQuery = TaskHelper.convert(legalEntity,"upper(a.name)",false);
			logger.info("legalEntityQuery from getWildCardSearchString : "+legalEntityQuery);
			cstmt.setString(++i, legalEntityQuery);
		}
		if (request.getmnemonic()!= null){
			String mnemonic = request.getmnemonic();
			String mnemonicQuery = getWildCardSearchString(mnemonic,"upper(a.mnemonic)");
			logger.info("mnemonicQuery from getWildCardSearchString : "+mnemonicQuery);
			cstmt.setString(++i, mnemonicQuery);
		}
	}

	private String createQueryString(GetLegalEntityRequest request) {
		StringBuilder qb = new StringBuilder(GET_LEGALENTITY);
		StringBuilder log = new StringBuilder(GET_LEGALENTITY);
		boolean paramSet = false;
		if (request.getinstitution() != null) {
			qb.append("@Instituion = ?");
			String institution = request.getinstitution();
			String institutionQuery = getWildCardSearchString(institution,"upper(b.name)");
			log.append("@Instituion  = " + institutionQuery);
			paramSet = true;
		}
		if (request.getlegalEntity() != null) {
			if (paramSet) {
				qb.append(", @LegalEntity = ?");
				String legalEntity = request.getlegalEntity();
				String legalEntityQuery = getWildCardSearchString(legalEntity,"upper(a.name)");
				log.append(",@LegalEntity  = " + legalEntityQuery);
			} else {
				qb.append("@LegalEntity = ?");
				String legalEntity = request.getlegalEntity();
				String legalEntityQuery = getWildCardSearchString(legalEntity,"upper(a.name)");
				log.append("@LegalEntity  = " +legalEntityQuery);
				paramSet = true;
			}
		}
		if (request.getmnemonic() != null) {
			if (paramSet) {
				qb.append(", @Mnemonic = ?");
				String mnemonic = request.getmnemonic();
				String mnemonicQuery = getWildCardSearchString(mnemonic,"upper(a.mnemonic)");
				log.append(",@Mnemonic  = " + mnemonicQuery);
			} else {
				qb.append("@Mnemonic = ?");
				String mnemonic = request.getmnemonic();
				String mnemonicQuery = getWildCardSearchString(mnemonic,"upper(a.mnemonic)");
				log.append("@Mnemonic  = " + mnemonicQuery);
			}
		}
		qb.append(")}");
		log.append(") } ");
		logger.info("Executing query:"+log.toString());
		return qb.toString();
	}

	public GetLegalEntityAuditResponse getAudit(
			GetLegalEntityAuditRequest request) throws Exception {
		logger.info("Inside getAudit(), request received: " + request);
		GetLegalEntityAuditResponse response = new GetLegalEntityAuditResponse();

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

	public void getAudit(GetLegalEntityAuditRequest request, int location,
			GetLegalEntityAuditResponse response) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_LEGALENTITYAUDIT);
			stmt.setInt(1, request.getlegalEntityId());
			long time = System.currentTimeMillis(); 
			rs = stmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				LegalEntityAudit audit = new LegalEntityAudit();
				int i = 0;
				audit.setlegalEntityId(request.getlegalEntityId());
				audit.setauditTime(rs.getDate(++i));
				audit.setcomment(rs.getString(++i));
				audit.setuserName(rs.getString(++i));
				response.setlegalEntityAudit(audit);
			}
		} catch (Exception ex) {
		
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}

	}

	private LegalEntity populateLegalEntity(ResultSet rs) throws SQLException {
		LegalEntity legalEntity = new LegalEntity();
		int i = 0;
		legalEntity.setlegalEntityId(rs.getInt(++i));
		legalEntity.setinstitutionId(rs.getInt(++i));
		legalEntity.setlegalEntityname(rs.getString(++i));
		legalEntity.setlegalEntityFullName(rs.getString(++i));
		legalEntity.setmnemonic(rs.getString(++i));
		legalEntity.setpbgoLegalEntityId(rs.getString(++i));
		legalEntity.setaddress(rs.getString(++i));
		legalEntity.setaddress2(rs.getString(++i));
		legalEntity.setcity(rs.getString(++i));
		legalEntity.setstate(rs.getString(++i));
		legalEntity.setpostalCode(rs.getString(++i));
		legalEntity.setcountry(rs.getString(++i));
		legalEntity.setcportLegalEntityId(rs.getString(++i));
		legalEntity.setparagonId(rs.getInt(++i));
		legalEntity.setcontactPerson(rs.getString(++i));
		legalEntity.setemailAddress(rs.getString(++i));
		legalEntity.settelephone(rs.getString(++i));
		legalEntity.setfax(rs.getString(++i));
		legalEntity.settelex(rs.getString(++i));
		Timestamp agreementDateTimeStamp = rs.getTimestamp(++i);
		Date agreementDate = agreementDateTimeStamp == null ? null : new Date(
				agreementDateTimeStamp.getTime());
		legalEntity.setagreementDate(agreementDate);
		legalEntity.setagreementText(rs.getString(++i));
		legalEntity.setsalesMan(rs.getString(++i));
		legalEntity.settierCode(rs.getString(++i));
		legalEntity.setdbClientTradingRef(rs.getInt(++i));
		legalEntity.setcisNumber(rs.getString(++i));
		legalEntity.setcomment(rs.getString(++i));
		legalEntity.setdbagEntityFlag(rs.getBoolean(++i));
		legalEntity.setpbgoLegalEntityFlag(rs.getBoolean(++i));
		legalEntity.setessTradePartyFlag(rs.getInt(++i));
		legalEntity.setPrimeBrokerageFlag(rs.getString(++i));
		legalEntity.setinstituionName(rs.getString(++i));
		legalEntity.setbrokerDealerFlag(rs.getBoolean(++i));
		legalEntity.setprimaryDealerFlag(rs.getBoolean(++i));
		legalEntity.setpriceDigits(rs.getInt(++i));
		legalEntity.settaxWithHoldingMethod(rs.getInt(++i));
		legalEntity.settotalReturnSwapClientFlag(rs.getInt(++i));
		legalEntity.setessLocation(rs.getString(++i));
		return legalEntity;
	}
}
