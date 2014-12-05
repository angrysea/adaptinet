package com.db.ess.synthesis.processor;

import com.db.ess.synthesis.dvo.AddAltDivCcyRequest;
import com.db.ess.synthesis.dvo.AddAltDivCcyResponse;
import com.db.ess.synthesis.dvo.AddImpactedSwapDivCcyRequest;
import com.db.ess.synthesis.dvo.AddImpactedSwapDivCcyResponse;
import com.db.ess.synthesis.dvo.DeleteAltDivCcyRequest;
import com.db.ess.synthesis.dvo.DeleteAltDivCcyResponse;
import com.db.ess.synthesis.dvo.GetAltDivCcyAuditRequest;
import com.db.ess.synthesis.dvo.GetAltDivCcyAuditResponse;
import com.db.ess.synthesis.dvo.GetAltDivCcyRequest;
import com.db.ess.synthesis.dvo.GetAltDivCcyResponse;
import com.db.ess.synthesis.dvo.GetImpactedSwapAuditRequest;
import com.db.ess.synthesis.dvo.GetImpactedSwapAuditResponse;
import com.db.ess.synthesis.dvo.GetImpactedSwapRequest;
import com.db.ess.synthesis.dvo.GetImpactedSwapResponse;
import com.db.ess.synthesis.dvo.UpdateAltDivCcyRequest;
import com.db.ess.synthesis.dvo.UpdateAltDivCcyResponse;
import com.db.ess.synthesis.dvo.UpdateImpactedSwapDivCcyRequest;
import com.db.ess.synthesis.dvo.UpdateImpactedSwapDivCcyResponse;

public class DividendAltDivCcy extends SynthesisProcessor {

	public GetAltDivCcyResponse GetAltDivCcy(GetAltDivCcyRequest request) {
		return this.getResponse(request, GetAltDivCcyResponse.class); 
	}
	
	public AddAltDivCcyResponse AddAltDivCcy(AddAltDivCcyRequest request) {
		return this.getResponse(request, AddAltDivCcyResponse.class); 
	}
	
	public DeleteAltDivCcyResponse DeleteAltDivCcy(DeleteAltDivCcyRequest request) {
		return this.getResponse(request, DeleteAltDivCcyResponse.class); 
	}
	
	public UpdateAltDivCcyResponse UpdateAltDivCcy(UpdateAltDivCcyRequest request) {
		return this.getResponse(request, UpdateAltDivCcyResponse.class); 
	}
	
	public GetImpactedSwapResponse GetImpactedSwap(GetImpactedSwapRequest request) {
		return this.getResponse(request, GetImpactedSwapResponse.class);
	}
	
	public AddImpactedSwapDivCcyResponse AddImpactedSwapDivCcy(AddImpactedSwapDivCcyRequest request) {
		return this.getResponse(request, AddImpactedSwapDivCcyResponse.class);
	}
	
	public UpdateImpactedSwapDivCcyResponse UpdateImpactedSwapDivCcy(UpdateImpactedSwapDivCcyRequest request) {
		return this.getResponse(request, UpdateImpactedSwapDivCcyResponse.class);
	}
	
	public GetAltDivCcyAuditResponse GetAltDivCcyAudit(GetAltDivCcyAuditRequest request) {
		return this.getResponse(request, GetAltDivCcyAuditResponse.class); 
	}
	public GetImpactedSwapAuditResponse GetImpactedSwapAudit(GetImpactedSwapAuditRequest request) {
		return this.getResponse(request, GetImpactedSwapAuditResponse.class); 
	}
	
}
