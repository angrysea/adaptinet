package com.db.ess.synthesis.processor;

import com.db.ess.synthesis.dvo.GetSecurityIdMappingRequest;
import com.db.ess.synthesis.dvo.GetSecurityIdMappingResponse;

public class SecurityId extends SynthesisProcessor{

	public GetSecurityIdMappingResponse GetSecurityIdMapping(GetSecurityIdMappingRequest request) {
		return this.getResponse(request, GetSecurityIdMappingResponse.class); 
	}


}
