package com.db.ess.synthesis.processor;

import org.adaptinet.sdk.processoragent.Processor;
import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.TradeMatchesDAO;
import com.db.ess.synthesis.dvo.GetEligibleOpeningTradesRequest;
import com.db.ess.synthesis.dvo.GetEligibleOpeningTradesResponse;
import com.db.ess.synthesis.dvo.GetTradeMatchesRequest;
import com.db.ess.synthesis.dvo.GetTradeMatchesResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;

public class TradeMatch extends Processor {

	private static final Logger logger = Logger.getLogger(TradeMatch.class.getName());
	
	public GetTradeMatchesResponse GetTradeMatches(GetTradeMatchesRequest request) {
		GetTradeMatchesResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
				logger.info("Inside GetTradeMatches..");
				response = new TradeMatchesDAO().getTradeMaches(request);
				returnResponse.setreturnCode(200);
				response.setreturnResponse(returnResponse);
			} catch(Exception e) {
				response = new GetTradeMatchesResponse();
				returnResponse.setreturnCode(404);
				returnResponse.setmessage(e.getMessage());
				response.setreturnResponse(returnResponse);
		}		
		return response;
	}
	
	public GetEligibleOpeningTradesResponse GetEligibleOpeningTrades(
			GetEligibleOpeningTradesRequest request) {
		GetEligibleOpeningTradesResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
				logger.info("Inside GetTradeMatches..");
				response = new TradeMatchesDAO().getEligibleOpeningTrades(request);
				returnResponse.setreturnCode(200);
				response.setreturnResponse(returnResponse);
			} catch(Exception e) {
				response = new GetEligibleOpeningTradesResponse();
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





