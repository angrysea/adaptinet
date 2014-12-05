package com.db.ess.synthesis.processor;

import org.adaptinet.sdk.processoragent.Processor;
import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.SwapDAO;
import com.db.ess.synthesis.dvo.GetSwapDatesRequest;
import com.db.ess.synthesis.dvo.GetSwapDatesResponse;
import com.db.ess.synthesis.dvo.GetSwapExceptionsRequest;
import com.db.ess.synthesis.dvo.GetSwapExceptionsResponse;
import com.db.ess.synthesis.dvo.GetSwapRequest;
import com.db.ess.synthesis.dvo.GetSwapResponse;
import com.db.ess.synthesis.dvo.GetSwapTransactionArgRequest;
import com.db.ess.synthesis.dvo.GetSwapTransactionArgResponse;
import com.db.ess.synthesis.dvo.GetSwapTransactionRequest;
import com.db.ess.synthesis.dvo.GetSwapTransactionResponse;
import com.db.ess.synthesis.dvo.GetSwapTypeRequest;
import com.db.ess.synthesis.dvo.GetSwapTypeResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.util.ResourceMonitor;

public class Swap extends Processor {
	
	private static final Logger logger = Logger.getLogger(Swap.class.getName());
		
	public GetSwapResponse GetSwaps(GetSwapRequest request) {

		GetSwapResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
			response = new SwapDAO().getSwaps(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch(Exception e) {
			response = new GetSwapResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		} finally {
			ResourceMonitor.memoryUsage();
		}
		return response;
	}

	public GetSwapResponse GetSwap(String swapNum) {

		GetSwapRequest request = new GetSwapRequest();
		request.setswapNum(swapNum);
		request.setuserId(1);
		request.setlocation(1);
		return GetSwaps(request);
	}
	
	public GetSwapDatesResponse GetSwapDates(GetSwapDatesRequest request){
		logger.info("Inside GetSwapDates()..");
		GetSwapDatesResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
			response = new SwapDAO().getSwapDates(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch(Exception e) {
			e.printStackTrace();
			response = new GetSwapDatesResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}		
		return response;
	}
	
	public GetSwapExceptionsResponse GetSwapExceptions(GetSwapExceptionsRequest request)
	{
		logger.info("Inside GetExceptions()..");
		GetSwapExceptionsResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
			response = new SwapDAO().getSwapExceptions(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch(Exception e) {
			e.printStackTrace();
			response = new GetSwapExceptionsResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}		
		return response;
	}
	
	public GetSwapTransactionResponse GetSwapTransactions(
			GetSwapTransactionRequest request) {
		logger.info("Inside GetSwapTransactions()..");
		GetSwapTransactionResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new SwapDAO().getSwapTransactions(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			e.printStackTrace();
			response = new GetSwapTransactionResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}
	
	public GetSwapTransactionArgResponse GetSwapTransactionArgs(GetSwapTransactionArgRequest request){
		logger.info("Inside GetSwapTransactions()..");
		GetSwapTransactionArgResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new SwapDAO().getSwapTransactionArgs(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			e.printStackTrace();
			response = new GetSwapTransactionArgResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}
	
	
	public GetSwapTypeResponse GetSwapTypes(GetSwapTypeRequest request){
		logger.info("Inside GetSwapTypes()..");
		GetSwapTypeResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new SwapDAO().getSwapTypes(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			e.printStackTrace();
			response = new GetSwapTypeResponse();
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
