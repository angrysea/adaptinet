package com.db.ess.synthesis.processor;

import org.adaptinet.sdk.processoragent.Processor;

import com.db.ess.synthesis.dao.InstitutionDAO;
import com.db.ess.synthesis.dvo.GetInstitutionRequest;
import com.db.ess.synthesis.dvo.GetInstitutionResponse;
import com.db.ess.synthesis.dvo.GetInstitutionSwapBooksRequest;
import com.db.ess.synthesis.dvo.GetInstitutionSwapBooksResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.util.dao.ConnectionHelper;

public class Institution extends Processor {

	public InstitutionDAO institutionDAO = new InstitutionDAO();

	public GetInstitutionResponse GetInstitutions(GetInstitutionRequest request) {

		GetInstitutionResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new InstitutionDAO().getInstitutions(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new GetInstitutionResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}

	public GetInstitutionSwapBooksResponse GetInstitutionSwapBooks(
			GetInstitutionSwapBooksRequest request) {

		GetInstitutionSwapBooksResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = institutionDAO.getInstitutionSwapBooks(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new GetInstitutionSwapBooksResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}

	static String connectionURL = "jdbc:jtds:sybase://esssybd1.uk.db.com:5000/ETS_ESS_TEST";
	static String driver = "net.sourceforge.jtds.jdbc.Driver";
	static String user = "ess_batch";
	static String password = "ess_batch01";

	@Override
	public void init() {
		try {
			ConnectionHelper.init(4, driver, connectionURL, user, password);
			GetInstitutionRequest request = new GetInstitutionRequest();
			request.setlocation(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cleanup() {

	}
}
