package com.db.ess.synthesis.processor;

import java.lang.reflect.Method;

import org.adaptinet.sdk.processoragent.Processor;
import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.SynthesisBaseDAO;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.exception.UserNotAuthenticatedException;

public class SynthesisProcessor  extends Processor  {
	
	private static final Logger logger = Logger.getLogger(SynthesisProcessor.class.getName());
	
	public <Req, Res> Res getResponse(Req request, Class<Res> responseClass) {
		Res response = null;
		ReturnResponse returnResponse = new ReturnResponse();
		try {
			Integer location = (Integer)invokeMethod(request, "getlocation");
			if (location == null) {
				response = new SynthesisBaseDAO().getResponse(request, responseClass);
			} else {
			    response = new SynthesisBaseDAO().getResponse(request, responseClass, location.intValue());
			}
			returnResponse.setreturnCode(200);
		} catch (UserNotAuthenticatedException unae) {
			try {
			    response = responseClass.newInstance();
			} catch (Exception ex) {
				logger.error(">>> failed to create response from responseClass..", ex);
			}
			returnResponse.setreturnCode(401);
			returnResponse.setmessage("InvalidUserIdPasswordException");
		} catch(Exception e) {
			try {
			    response = responseClass.newInstance();
			} catch (Exception ex) {
				logger.error(">>> failed to create response from responseClass..", ex);
			}
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
		} finally {
			invokeMethod(response, "setreturnResponse", returnResponse);
		}
		return response;
	}
	
	private Object invokeMethod (Object obj, String methodName, Object... params) {
	    Method[] allMethods = obj.getClass().getDeclaredMethods();
	    try { 
	    	for (Method m : allMethods) {
	    		String mname = m.getName();
	    		if (mname.equals(methodName)) {
	    			return m.invoke(obj, params);
	    		}
	    	}	
	    } catch (Exception e) {
	    	logger.error(">>> failed to invoke method: " + methodName, e);	
	    }
	    return null;
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
