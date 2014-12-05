package com.db.ess.synthesis.processor;

import com.db.ess.synthesis.dvo.UploadAdjustmentRequest;
import com.db.ess.synthesis.dvo.UploadAdjustmentResponse;
import com.db.ess.synthesis.dvo.UploadExceptionRequest;
import com.db.ess.synthesis.dvo.UploadExceptionResponse;

public class Upload extends SynthesisProcessor {
	
	public UploadExceptionResponse UploadExceptions(UploadExceptionRequest request) {
		return this.getResponse(request, UploadExceptionResponse.class); 
	}
	
	public UploadAdjustmentResponse UploadAdjustments(UploadAdjustmentRequest request) {
		return this.getResponse(request, UploadAdjustmentResponse.class); 
	}
	
}
