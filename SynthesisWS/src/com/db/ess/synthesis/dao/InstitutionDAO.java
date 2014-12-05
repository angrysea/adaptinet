package com.db.ess.synthesis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.TaskHelper;
import com.db.ess.synthesis.dvo.GetInstitutionRequest;
import com.db.ess.synthesis.dvo.GetInstitutionResponse;
import com.db.ess.synthesis.dvo.GetInstitutionSwapBooksRequest;
import com.db.ess.synthesis.dvo.GetInstitutionSwapBooksResponse;
import com.db.ess.synthesis.dvo.Institution;
import com.db.ess.synthesis.dvo.InstitutionSwapBook;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;
import com.db.ess.synthesis.util.SynthesisConstants;

public class InstitutionDAO extends SynthesisBaseDAO{

	private static Logger logger = Logger.getLogger(InstitutionDAO.class
			.getName());

	private static final String GET_INSTITUTIONS = "{ call SYN_GetInstitutions (";
	
	private static final String GET_SWAPBOOKS ="{ call dbo.SYN_GetSwapBooks (";

	public GetInstitutionResponse getInstitutions(GetInstitutionRequest request)
			throws Exception {
		logger.info("Inside getInstitutions(), request received: "+request);
		GetInstitutionResponse response = new GetInstitutionResponse();

		int location = request.getlocation();

		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;
		
		// Fall thru using London as a default.
		case ESSLocation.LONDON:
			getInstitutions(request, location, response);
			break;
			
		case ESSLocation.NEWYORK:
			getInstitutions(request, location, response);
			break;

		case ESSLocation.GLOBAL:
			getInstitutions(request, ESSLocation.LONDON, response);
			getInstitutions(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}
	
	public void getInstitutions(GetInstitutionRequest request, int location, GetInstitutionResponse response)
			throws Exception {
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
				response.setinstitutions(populateInstitution(rs));
				count++;
			}
			logger.info("Found ["+count+"] Institutions For the given search criteria.");
			
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			ex.printStackTrace();
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
	}
	
	private void setParameters(GetInstitutionRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		if (request.getinstitution()!= null){
			String institution = request.getinstitution();
			String institutionQuery = TaskHelper.convert(institution,"upper(a.name)",false);
			logger.info("institutionQuery from getWildCardSearchString : "+institutionQuery);
			cstmt.setString(++i, institutionQuery);
		}
		if (request.getlegalEntity()!= null){
			String legalEntity = request.getlegalEntity();
			String legalEntityQuery = TaskHelper.convert(legalEntity,"upper(b.name)",false);
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

	private String createQueryString(GetInstitutionRequest request) {
		StringBuilder qb = new StringBuilder(GET_INSTITUTIONS);
		StringBuilder log = new StringBuilder(GET_INSTITUTIONS);

		boolean paramSet = false;
		if (request.getinstitution() != null) {
			qb.append("@Institution = ?");
			String institution = request.getinstitution();
			String institutionQuery = getWildCardSearchString(institution,"upper(a.name)");
			log.append("@Institution = " + institutionQuery);
			paramSet = true;
		}
		if (request.getlegalEntity() != null) {
			if (paramSet) {
				qb.append(", @LegalEntity = ?");
				String legalEntity = request.getlegalEntity();
				String legalEntityQuery = getWildCardSearchString(legalEntity,"upper(b.name)");
				log.append(", @LegalEntity = " + legalEntityQuery);

			} else {
				qb.append("@LegalEntity = ?");
				String legalEntity = request.getlegalEntity();
				String legalEntityQuery = getWildCardSearchString(legalEntity,"upper(b.name)");
				log.append("@LegalEntity = " + legalEntityQuery);
				paramSet = true;
			}
		}
		if (request.getmnemonic() != null) {
			if (paramSet) {
				qb.append(", @Mnemonic = ?");
				String mnemonic = request.getmnemonic();
				String mnemonicQuery = getWildCardSearchString(mnemonic,"upper(a.mnemonic)");
				log.append(", @Mnemonic = " + mnemonicQuery);
			} else {
				qb.append("@Mnemonic = ?");
				String mnemonic = request.getmnemonic();
				String mnemonicQuery = getWildCardSearchString(mnemonic,"upper(a.mnemonic)");
				log.append(" @Mnemonic = " + mnemonicQuery);
				paramSet = true;
			}
		}
		qb.append(")}");
		log.append(") } ");
		logger.info("Executing query:"+log.toString());
		return qb.toString();
	}
	
	private Institution populateInstitution(ResultSet rs) throws SQLException {
		int i = 0;
		Institution institution = new Institution();

		institution.setinstitutionId(rs.getInt(++i));
		institution.setinstitutionName(rs.getString(++i));
		institution.setlegalEntityName(rs.getString(++i));
		institution.setinstitutionFullName(rs.getString(++i));
		institution.setmnemonic(rs.getString(++i));
		institution.setpbgoInvestmentManagerId(rs.getString(++i));
		institution.setparagonId(rs.getInt(++i));
		institution.setcportParentId(rs.getString(++i));
		institution.setdbagEntityFlag(rs.getBoolean(++i));
		institution.setbrokerDealerFlag(rs.getBoolean(++i));
		institution.setprimaryDealerFlag(rs.getBoolean(++i));
		institution.setpbgoInstitutionFlag(rs.getBoolean(++i));
		rs.getString(++i);
		//institution.settotalReturnSwapClientFlag(Integer.getInteger("1"));
		institution.setcomment(rs.getString(++i));
		institution.setcontactPerson(rs.getString(++i));
		institution.settelephone(rs.getString(++i));
		institution.setfax(rs.getString(++i));
		institution.setaddress(rs.getString(++i));
		institution.setaddress2(rs.getString(++i));
		institution.setcity(rs.getString(++i));
		institution.setstate(rs.getString(++i));
		institution.setcountry(rs.getString(++i));
		institution.setpostalCode(rs.getString(++i));
		institution.settelex(rs.getString(++i));
		institution.setcisNumber(rs.getString(++i));
		//institution.setpriceDigits();
		//institution.setemailAddress(rs.getString(++i));
		//institution.setessLocation(rs.getString(++i));
		
		return institution;
	}
	

	public GetInstitutionSwapBooksResponse getInstitutionSwapBooks(GetInstitutionSwapBooksRequest request)
			throws Exception {
		logger.info("Inside, getInstitutionSwapBooks(), request recieved: "+request);
		GetInstitutionSwapBooksResponse response = new GetInstitutionSwapBooksResponse();

		int location = request.getlocation();

		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getInstitutionSwapBooks(request, location, response);
			break;
			
		case ESSLocation.NEWYORK:
			getInstitutionSwapBooks(request, location, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}
	
	public void getInstitutionSwapBooks(GetInstitutionSwapBooksRequest request, int location, GetInstitutionSwapBooksResponse response)
	throws Exception {
		
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = createSwapBookQueryString(request);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setSwapBookParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 

			while (rs.next()) {
				InstitutionSwapBook swapBook = populateSwapBook(rs);
				response.setinstitutionSwapBooks(swapBook);
			}
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
		
	}

	private String createSwapBookQueryString(GetInstitutionSwapBooksRequest request) {
		StringBuilder qb = new StringBuilder(GET_SWAPBOOKS);
		StringBuilder log = new StringBuilder(GET_SWAPBOOKS);

		qb.append("? )}");
		log.append("@InstitutionId = " + request.getinstitutionId());
		log.append(") } ");

		logger.info("Executing query:"+log.toString());
		return qb.toString();
	}

	private void setSwapBookParameters(GetInstitutionSwapBooksRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		cstmt.setInt(++i, request.getinstitutionId());
	}

	private InstitutionSwapBook populateSwapBook(ResultSet rs) throws SQLException {
		InstitutionSwapBook swapBook = new InstitutionSwapBook();

		int i = 0;
		swapBook.setaccountName(rs.getString(++i));
		swapBook.setbookName(rs.getString(++i));
		swapBook.setbookId(rs.getInt(++i));
		return swapBook;
	}

}
