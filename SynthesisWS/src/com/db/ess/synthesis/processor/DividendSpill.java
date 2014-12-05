package com.db.ess.synthesis.processor;

import org.adaptinet.sdk.processoragent.Processor;
import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.DividendSpillDAO;
import com.db.ess.synthesis.dvo.GetDividendSpillRequest;
import com.db.ess.synthesis.dvo.GetDividendSpillResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;

public class DividendSpill extends Processor {

	private static final Logger logger = Logger.getLogger(DividendSpill.class.getName());
	public GetDividendSpillResponse GetDividendSpill(GetDividendSpillRequest request) {
		GetDividendSpillResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
				logger.info("Inside GetDividendSpill..");
				response = new DividendSpillDAO().getDividendSpill(request);
				returnResponse.setreturnCode(200);
				response.setreturnResponse(returnResponse);
			} catch(Exception e) {
				response = new GetDividendSpillResponse();
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


	