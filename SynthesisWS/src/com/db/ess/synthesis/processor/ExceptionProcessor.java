package com.db.ess.synthesis.processor;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.CreateExceptionRequest;
import com.db.ess.synthesis.dvo.CreateExceptionResponse;
import com.db.ess.synthesis.dvo.ExceptionActionRequest;
import com.db.ess.synthesis.dvo.ExceptionActionResponse;
import com.db.ess.synthesis.dvo.ExceptionReRateRequest;
import com.db.ess.synthesis.dvo.ExceptionReRateResponse;
import com.db.ess.synthesis.dvo.GetAffectedPositionRequest;
import com.db.ess.synthesis.dvo.GetAffectedPositionResponse;
import com.db.ess.synthesis.dvo.GetPendingExceptionCountRequest;
import com.db.ess.synthesis.dvo.GetPendingExceptionCountResponse;
import com.db.ess.synthesis.dvo.GetSynthesisExceptionRequest;
import com.db.ess.synthesis.dvo.GetSynthesisExceptionResponse;

public class ExceptionProcessor extends SynthesisProcessor {
	
	private static final Logger logger = Logger.getLogger(Exception.class.getName());
	
	public GetSynthesisExceptionResponse GetExceptions(GetSynthesisExceptionRequest request) {
		return this.getResponse(request, GetSynthesisExceptionResponse.class); 
	}

	public GetAffectedPositionResponse GetAffectedPositions(GetAffectedPositionRequest request) {
		return this.getResponse(request, GetAffectedPositionResponse.class); 
	}
	
	public ExceptionActionResponse ActionException(ExceptionActionRequest request) {
		return this.getResponse(request, ExceptionActionResponse.class); 
	}
	
	public CreateExceptionResponse CreateException(CreateExceptionRequest request) {
		logger.info("Inside Controller "); 
		return this.getResponse(request, CreateExceptionResponse.class); 
	}
	
	public ExceptionReRateResponse ReRateException(ExceptionReRateRequest request) {
		return this.getResponse(request, ExceptionReRateResponse.class); 
	}
	
	public GetPendingExceptionCountResponse PendingExceptionCount(GetPendingExceptionCountRequest request) {
		return this.getResponse(request, GetPendingExceptionCountResponse.class); 
	}
}
