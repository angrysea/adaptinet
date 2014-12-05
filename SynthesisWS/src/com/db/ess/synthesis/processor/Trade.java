package com.db.ess.synthesis.processor;

import org.adaptinet.sdk.processoragent.Processor;
import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.TradeDAO;
import com.db.ess.synthesis.dvo.GetTradeDividendsRequest;
import com.db.ess.synthesis.dvo.GetTradeDividendsResponse;
import com.db.ess.synthesis.dvo.GetTradeRequest;
import com.db.ess.synthesis.dvo.GetTradeResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.util.ResourceMonitor;

public class Trade extends Processor {
	
	private static final Logger logger = Logger.getLogger(Swap.class.getName());		

	public GetTradeResponse GetTrades(GetTradeRequest request) {

		GetTradeResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
			response = new TradeDAO().getTrades(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch(Exception e) {
			response = new GetTradeResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		} finally {
			ResourceMonitor.memoryUsage();
		}	
		return response;
	}

	public GetTradeResponse GetTrade(String bookId) {

		GetTradeRequest request = new GetTradeRequest();
		//request.setbookId(Integer.parseInt(bookId));
		request.setuserId(1);
		request.setlocation(1);
		return GetTrades(request);
	}
	
	public GetTradeDividendsResponse GetTradeDividends(GetTradeDividendsRequest request){
		GetTradeDividendsResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		logger.info("Inside GetTradeDividends()");
		try {
			response = new TradeDAO().getTradeDividends(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch(Exception e) {
			response = new GetTradeDividendsResponse();
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
