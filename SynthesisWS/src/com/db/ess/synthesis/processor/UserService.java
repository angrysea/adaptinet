package com.db.ess.synthesis.processor;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.FunctionEntitlementDAO;
import com.db.ess.synthesis.dao.UserProfileDAO;
import com.db.ess.synthesis.dvo.AddFunctionEntitlementRequest;
import com.db.ess.synthesis.dvo.AddFunctionEntitlementResponse;
import com.db.ess.synthesis.dvo.FunctionEntitlement;
import com.db.ess.synthesis.dvo.GetBooksRequest;
import com.db.ess.synthesis.dvo.GetBooksResponse;
import com.db.ess.synthesis.dvo.GetFXRateListsRequest;
import com.db.ess.synthesis.dvo.GetFXRateListsResponse;
import com.db.ess.synthesis.dvo.GetFunctionEntitlementRequest;
import com.db.ess.synthesis.dvo.GetFunctionEntitlementResponse;
import com.db.ess.synthesis.dvo.GetUserProfileRequest;
import com.db.ess.synthesis.dvo.GetUserProfileResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.dvo.UpdatePreferredBooksRequest;
import com.db.ess.synthesis.dvo.UpdateUserProfileRequest;
import com.db.ess.synthesis.dvo.UserProfile;
import com.db.ess.synthesis.sessions.SynthesisUserSession;
import com.db.ess.synthesis.sessions.UserSession;
import com.db.ess.synthesis.sessions.UserSessions;

public class UserService extends SynthesisProcessor {

	private static final Logger logger = Logger
			.getLogger(CustomBasketMercuryValidation.class.getName());
	
	public GetBooksResponse GetBooks(
			GetBooksRequest request) {
		GetBooksResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			logger.info(">>> userId = " + request.getuserId());
			logger.info(">>> req type = " + request.getrequestType());
			response = new UserProfileDAO().getBooks(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			e.printStackTrace();
			response = new GetBooksResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}

	public ReturnResponse UpdatePreferredBooks(
			UpdatePreferredBooksRequest request) {

		ReturnResponse response = new ReturnResponse();

		try {
			new UserProfileDAO().updatePreferredBooks(request);
			response.setreturnCode(200);
		} catch (Exception e) {
			response.setreturnCode(404);
			response.setmessage(e.getMessage());
		}
		return response;
	}

	public GetUserProfileResponse WebGetUserProfile(String user) {

		GetUserProfileResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			GetUserProfileRequest request = new GetUserProfileRequest();
			request.setuser(user);
			response = new UserProfileDAO().getUserProfile(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new GetUserProfileResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}
	
	//---------------------------------------------------------------------
	// New approach - all the method above will be rewritten this way
	//---------------------------------------------------------------------
	
	public GetUserProfileResponse GetUserProfile(GetUserProfileRequest request) {
		
		GetUserProfileResponse userProfileResponse = 
			this.getResponse(request, GetUserProfileResponse.class); 
		
		// just return the response as UserNotAuthenticatedException is caught 
		if (userProfileResponse.getreturnResponse().getreturnCode() == 401) {
			return userProfileResponse;
		}
		
		UserProfile up = userProfileResponse.getuserProfile();

		// get functional entitlements at once - consider combine everything this way
		GetFunctionEntitlementRequest feReq = new GetFunctionEntitlementRequest();
		feReq.setuser(request.getuser());
		feReq.setlocation(up != null ? up.getavailableLocation() : 0);
		//GetFunctionEntitlementResponse feRes = GetFunctionEntitlement(feReq);
		FunctionEntitlementDAO entitlementDAO = new FunctionEntitlementDAO();
		GetFunctionEntitlementResponse feRes = new GetFunctionEntitlementResponse();
		try
		{	
			feRes = entitlementDAO.getEntitlements(feReq);
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.error("Exception in fetching entitlements");
		}
		for (Iterator<FunctionEntitlement> i = feRes.getentitlementsIterator(); i.hasNext(); )
		{
			up.setfunctionEntitlement(i.next());
		}

		// save user profile onto the central session map
		UserSession session = new SynthesisUserSession(up);
		UserSessions.addSession(up.getemail(), session);

		return userProfileResponse;
	}
	
	public GetFXRateListsResponse GetFXRateList(GetFXRateListsRequest request) {
		return this.getResponse(request, GetFXRateListsResponse.class); 
	}
	
	public GetFunctionEntitlementResponse GetFunctionEntitlement(GetFunctionEntitlementRequest request) {
		return this.getResponse(request, GetFunctionEntitlementResponse.class);
	}
	
	public AddFunctionEntitlementResponse AddFunctionEntitlement(AddFunctionEntitlementRequest request) {
		return this.getResponse(request, AddFunctionEntitlementResponse.class); 
	}
	
	public ReturnResponse SaveUserProfile(UpdateUserProfileRequest request) {
		ReturnResponse res = this.getResponse(request, ReturnResponse.class);
		UserSessions.removeSession(request.getname());
		return res;
	}
	
}
