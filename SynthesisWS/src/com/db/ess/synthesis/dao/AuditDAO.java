package com.db.ess.synthesis.dao;

import java.util.Date;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.Audit;
import com.db.ess.synthesis.dvo.GetAuditRequest;
import com.db.ess.synthesis.dvo.GetAuditResponse;

public class AuditDAO {

	private static Logger logger = Logger.getLogger(AuditDAO.class.getName());

	private static final int SWAP_AUDIT = 1;
	private static final int POSITION_AUDIT = 2;
	private static final int TRADE_AUDIT = 3;
	private static final int INSTRUMENT_AUDIT = 4;
	private static final int INSTITUTION_AUDIT = 5;
	private static final int LEGALENTITY_AUDIT = 6;

	public GetAuditResponse getAudits(GetAuditRequest request) {
		GetAuditResponse response = new GetAuditResponse();

		int auditTargetIntVal = getAuditTargetIntVal(request.getauditTraget());

		switch (auditTargetIntVal) {
		case SWAP_AUDIT:
			// Exceute Swap Audit Sql
			break;
		case POSITION_AUDIT:
			// Exceute Swap Audit Sql
			break;
		case TRADE_AUDIT:
			// Exceute Swap Audit Sql
			break;
		case INSTRUMENT_AUDIT:
			// Exceute Swap Audit Sql
			break;
		case INSTITUTION_AUDIT:
			// Exceute Swap Audit Sql
			break;
		case LEGALENTITY_AUDIT:
			// Exceute Swap Audit Sql
			break;
		default:
			// Invalid Audit Target.
		}

		// Hradcoded Response for now.
		for (int i = 0; i < 5; i++) {
			Audit audit = new Audit();
			audit.setfieldName("tbd");
			audit.setbefore("tbd");
			audit.setafter("tbd");
			audit.setdateAndTime(new Date());
			audit.setuserName("tbd");
			response.setaudits(audit);
		}

		return response;
	}

	private int getAuditTargetIntVal(String auditTarget) {
		if ("Swap".equals(auditTarget))
			return SWAP_AUDIT;
		if ("Position".equals(auditTarget))
			return POSITION_AUDIT;
		if ("Trade".equals(TRADE_AUDIT))
			return TRADE_AUDIT;
		if ("Instrument".equals(auditTarget))
			return INSTRUMENT_AUDIT;
		if ("Institution".equals(auditTarget))
			return INSTITUTION_AUDIT;
		if ("LegalEntity".equals(auditTarget))
			return LEGALENTITY_AUDIT;

		return 0;
	}
}
