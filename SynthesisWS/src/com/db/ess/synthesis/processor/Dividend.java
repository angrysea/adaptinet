package com.db.ess.synthesis.processor;


import org.adaptinet.sdk.processoragent.Processor;
import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.DividendDAO;
import com.db.ess.synthesis.dvo.CreateDividendRequest;
import com.db.ess.synthesis.dvo.CreateDividendResponse;
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
import com.db.ess.synthesis.dvo.ReturnResponse;

public class Dividend extends Processor {

	private static final Logger logger = Logger.getLogger(Dividend.class.getName());
	public GetDividendResponse GetDividend(GetDividendRequest request) {
		GetDividendResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
				logger.info("Inside GetDividend..");
				response = new DividendDAO().getDividend(request);
				returnResponse.setreturnCode(200);
				response.setreturnResponse(returnResponse);
			} catch(Exception e) {
				response = new GetDividendResponse();
				returnResponse.setreturnCode(404);
				returnResponse.setmessage(e.getMessage());
				response.setreturnResponse(returnResponse);
		}		
		return response;
	}
	public GetActionSpillQueueResponse GetActionSpillQueue(GetActionSpillQueueRequest request) {
		
		GetActionSpillQueueResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
				logger.info("Inside GetActionspillQueue....");
				response=new DividendDAO().getActionSpillQueue(request);
				returnResponse.setreturnCode(200);
				response.setreturnResponse(returnResponse);
				
			} catch(Exception e) {
				response = new GetActionSpillQueueResponse();
				returnResponse.setreturnCode(404);
				returnResponse.setmessage(e.getMessage());
				response.setreturnResponse(returnResponse);
				
		}		
		return response;
	}
	
	public CreateDividendResponse CreateDividend(CreateDividendRequest request) {

		CreateDividendResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			logger.info("Inside CreateDividend....");
			response=new DividendDAO().createDividend(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new CreateDividendResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}

	public GetImpactedSwapResponse GetImpactedSwap(GetImpactedSwapRequest request) {
		GetImpactedSwapResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
				logger.info("Inside GetImpactedSwap..");
				response = new DividendDAO().getImpactedSwap(request);
				returnResponse.setreturnCode(200);
				response.setreturnResponse(returnResponse);
			} catch(Exception e) {
				response = new GetImpactedSwapResponse();
				returnResponse.setreturnCode(404);
				returnResponse.setmessage(e.getMessage());
				response.setreturnResponse(returnResponse);
		}		
		return response;
	}
	
	public GetDeletedDividendResponse GetDeletedDividends(GetDeletedDividendRequest request) {
		GetDeletedDividendResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
				logger.info("Inside GetDeletedDividend..");
				response = new DividendDAO().getDeletedDividends(request);
				returnResponse.setreturnCode(200);
				response.setreturnResponse(returnResponse);
			} catch(Exception e) {
				response = new GetDeletedDividendResponse();
				returnResponse.setreturnCode(404);
				returnResponse.setmessage(e.getMessage());
				response.setreturnResponse(returnResponse);
		}		
		return response;
	}
	
	public GetUpdateImpactedSwapResponse GetUpdateImpactedSwap(GetUpdateImpactedSwapRequest request) {
		GetUpdateImpactedSwapResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
				logger.info("Inside GetUpdateImpactedSwap..");
				response = new DividendDAO().getUpdateImpactedSwap(request);
				returnResponse.setreturnCode(200);
				response.setreturnResponse(returnResponse);
			} catch(Exception e) {
				response = new GetUpdateImpactedSwapResponse();
				returnResponse.setreturnCode(404);
				returnResponse.setmessage(e.getMessage());
				response.setreturnResponse(returnResponse);
		}		
		return response;
	}
	
	public GetDividendAuditResponse GetDividendAudit(GetDividendAuditRequest request) {
		GetDividendAuditResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
				logger.info("Inside GetDividendAudit..");
				response = new DividendDAO().getDividendAudit(request);
				returnResponse.setreturnCode(200);
				response.setreturnResponse(returnResponse);
			} catch(Exception e) {
				response = new GetDividendAuditResponse();
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


	