package com.db.ess.synthesis.processor;

import org.adaptinet.sdk.processoragent.Processor;
import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.AuditDAO;
import com.db.ess.synthesis.dvo.GetAuditRequest;
import com.db.ess.synthesis.dvo.GetAuditResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;

public class Audit extends Processor {

	private static final Logger logger = Logger.getLogger(DividendAltDivCcy.class.getName());
	
	 public GetAuditResponse GetAudits(GetAuditRequest request){
		 logger.info("Inside GetAudits()..");
		 GetAuditResponse response = new GetAuditResponse();
		 ReturnResponse returnResponse = new ReturnResponse();
			try {
				response = new AuditDAO().getAudits(request);
				returnResponse.setreturnCode(200);
				response.setreturnResponse(returnResponse);
			} catch (Exception e) {
				response = new GetAuditResponse();
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
