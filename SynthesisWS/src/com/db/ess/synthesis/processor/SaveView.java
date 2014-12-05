package com.db.ess.synthesis.processor;

import org.adaptinet.sdk.processoragent.Processor;

import com.db.ess.synthesis.dao.SaveViewDAO;
import com.db.ess.synthesis.dvo.DeleteSavedViewRequest;
import com.db.ess.synthesis.dvo.GetSavedViewRequest;
import com.db.ess.synthesis.dvo.GetSavedViewResponse;
import com.db.ess.synthesis.dvo.InsertSavedViewRequest;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.dvo.UpdateSavedViewRequest;

public class SaveView extends Processor {
	
	public GetSavedViewResponse GetSavedViews(GetSavedViewRequest request) {

		GetSavedViewResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		
		try {
			response = new SaveViewDAO().getSavedView(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
			
		} catch(Exception e) {
			response = new GetSavedViewResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}		
		return response;
	}
	
	public ReturnResponse UpdateSavedViews(UpdateSavedViewRequest request) {

		ReturnResponse response = new ReturnResponse();

		try {
			new SaveViewDAO().updateLayout(request, request.getdefaultLocation());
			response.setreturnCode(200);
		} catch (Exception e) {
			response.setreturnCode(404);
			response.setmessage(e.getMessage());
		}
		return response;
	}
	
	public ReturnResponse InsertSavedViews(InsertSavedViewRequest request) {

		ReturnResponse response = new ReturnResponse();

		try {
			new SaveViewDAO().insertLayout(request, request.getdefaultLocation());
			response.setreturnCode(200);
		} catch (Exception e) {
			response.setreturnCode(404);
			response.setmessage(e.getMessage());
		}
		return response;
	}
	
	public ReturnResponse DeleteSavedViews(DeleteSavedViewRequest request) {

		ReturnResponse response = new ReturnResponse();

		try {
			new SaveViewDAO().deleteLayout(request, request.getdefaultLocation());
			response.setreturnCode(200);
		} catch (Exception e) {
			response.setreturnCode(404);
			response.setmessage(e.getMessage());
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
