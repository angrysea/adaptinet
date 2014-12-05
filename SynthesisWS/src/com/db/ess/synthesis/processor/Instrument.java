package com.db.ess.synthesis.processor;

import org.adaptinet.sdk.processoragent.Processor;

import com.db.ess.synthesis.dao.InstrumentDAO;
import com.db.ess.synthesis.dvo.GetInstrumentAuditRequest;
import com.db.ess.synthesis.dvo.GetInstrumentAuditResponse;
import com.db.ess.synthesis.dvo.GetInstrumentRequest;
import com.db.ess.synthesis.dvo.GetInstrumentResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;

public class Instrument extends Processor {

	public GetInstrumentAuditResponse GetInstrumentAudit(GetInstrumentAuditRequest request) {

		GetInstrumentAuditResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new InstrumentDAO().getAudit(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new GetInstrumentAuditResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}
	
	public GetInstrumentResponse GetInstruments(GetInstrumentRequest request){
		GetInstrumentResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
			response = new InstrumentDAO().getInstruments(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch(Exception e) {
			response = new GetInstrumentResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}		
		return response;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
}
