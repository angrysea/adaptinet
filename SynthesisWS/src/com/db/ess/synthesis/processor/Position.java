package com.db.ess.synthesis.processor;

import java.text.DateFormat;
import java.text.ParseException;

import org.adaptinet.sdk.processoragent.Processor;

import com.db.ess.synthesis.dao.PositionDAO;
import com.db.ess.synthesis.dvo.GetPositionRequest;
import com.db.ess.synthesis.dvo.GetPositionResponse;
import com.db.ess.synthesis.dvo.GetSwapExceptionsRequest;
import com.db.ess.synthesis.dvo.GetSwapExceptionsResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.util.ResourceMonitor;

public class Position extends Processor {
	
	public GetPositionResponse GetPositions(GetPositionRequest request) {

		GetPositionResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
			response = new PositionDAO().getPositions(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch(Exception e) {
			response = new GetPositionResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		} finally {
			ResourceMonitor.memoryUsage();
		}		
		return response;
	}

	public GetPositionResponse GetPosition(String bookId) {

		GetPositionRequest request = new GetPositionRequest();
		request.setswapNum(bookId);
		request.setpositionType("SETTLED");
		try {
			request.setasOfDate(DateFormat.getDateInstance(DateFormat.SHORT).parse("03/29/2011"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setuserId(1);
		request.setlocation(1);
		return GetPositions(request);
	}
	
	public GetSwapExceptionsResponse GetPositionExceptions(GetSwapExceptionsRequest request){
		GetSwapExceptionsResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
			response = new PositionDAO().getPositionExceptions(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch(Exception e) {
			response = new GetSwapExceptionsResponse();
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
