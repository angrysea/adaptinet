package com.db.ess.synthesis.processor;

import org.adaptinet.sdk.processoragent.Processor;
import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.LegalEntityDAO;
import com.db.ess.synthesis.dvo.GetLegalEntityAuditRequest;
import com.db.ess.synthesis.dvo.GetLegalEntityAuditResponse;
import com.db.ess.synthesis.dvo.GetLegalEntityRequest;
import com.db.ess.synthesis.dvo.GetLegalEntityResponse;
import com.db.ess.synthesis.dvo.GetLESettlementInstructionRequest;
import com.db.ess.synthesis.dvo.GetLESettlementInstructionResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;

public class LegalEntity extends Processor {

	private static final Logger logger = Logger
			.getLogger(LegalEntity.class);

	public GetLegalEntityAuditResponse GetLegalEntityAudit(
			GetLegalEntityAuditRequest request) {

		GetLegalEntityAuditResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new LegalEntityDAO().getAudit(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new GetLegalEntityAuditResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}

	public GetLegalEntityResponse GetLegalEntity(GetLegalEntityRequest request) {

		GetLegalEntityResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new LegalEntityDAO().getLegalEntity(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new GetLegalEntityResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}

	public GetLESettlementInstructionResponse GetLESettlementInstruction(
			GetLESettlementInstructionRequest request) {
		logger.info("Inside GetLESettlementInstruction()..");
		GetLESettlementInstructionResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new LegalEntityDAO().getLESettlementInstruction(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new GetLESettlementInstructionResponse();
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
