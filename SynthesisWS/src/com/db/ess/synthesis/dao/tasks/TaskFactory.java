package com.db.ess.synthesis.dao.tasks;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.adjustment.UploadAdjustmentTask;
import com.db.ess.synthesis.dao.tasks.divspills.AddAltDivCcyTask;
import com.db.ess.synthesis.dao.tasks.divspills.AddImpactedSwapsTask;
import com.db.ess.synthesis.dao.tasks.divspills.DeleteAltDivCcyTask;
import com.db.ess.synthesis.dao.tasks.divspills.GetAltDivCcyAuditTask;
import com.db.ess.synthesis.dao.tasks.divspills.GetAltDivCcyTask;
import com.db.ess.synthesis.dao.tasks.divspills.GetImpactedSwapAuditTask;
import com.db.ess.synthesis.dao.tasks.divspills.GetImpactedSwapTask;
import com.db.ess.synthesis.dao.tasks.divspills.UpdateAltDivCcyTask;
import com.db.ess.synthesis.dao.tasks.divspills.UpdateImpactedSwapsTask;
import com.db.ess.synthesis.dao.tasks.exception.CreateExceptionTask;
import com.db.ess.synthesis.dao.tasks.exception.ExceptionActionTask;
import com.db.ess.synthesis.dao.tasks.exception.ExceptionReRateTask;
import com.db.ess.synthesis.dao.tasks.exception.GetAffectedPositionTask;
import com.db.ess.synthesis.dao.tasks.exception.GetPendingExceptionCountTask;
import com.db.ess.synthesis.dao.tasks.exception.LocationDistributorTask;
import com.db.ess.synthesis.dao.tasks.exception.UploadSynthesisExceptionTask;
import com.db.ess.synthesis.dao.tasks.exception.GetSynthesisExceptionTask;
import com.db.ess.synthesis.dao.tasks.securityId.GetSecurityIdMappingTask;
import com.db.ess.synthesis.dao.tasks.user.AddFunctionEntitlementTask;
import com.db.ess.synthesis.dao.tasks.user.GetFXRateListTask;
import com.db.ess.synthesis.dao.tasks.user.GetFunctionEntitlementTask;
import com.db.ess.synthesis.dao.tasks.user.GetUserProfileTask;
import com.db.ess.synthesis.dao.tasks.user.UpdateUserProfileTask;
import com.db.ess.synthesis.dvo.AddAltDivCcyRequest;
import com.db.ess.synthesis.dvo.AddFunctionEntitlementRequest;
import com.db.ess.synthesis.dvo.AddImpactedSwapDivCcyRequest;
import com.db.ess.synthesis.dvo.CreateExceptionRequest;
import com.db.ess.synthesis.dvo.DeleteAltDivCcyRequest;
import com.db.ess.synthesis.dvo.ExceptionActionRequest;
import com.db.ess.synthesis.dvo.ExceptionReRateRequest;
import com.db.ess.synthesis.dvo.ExceptionReRateResponse;
import com.db.ess.synthesis.dvo.GetAffectedPositionRequest;
import com.db.ess.synthesis.dvo.GetAltDivCcyAuditRequest;
import com.db.ess.synthesis.dvo.GetAltDivCcyRequest;
import com.db.ess.synthesis.dvo.GetFXRateListsRequest;
import com.db.ess.synthesis.dvo.GetFunctionEntitlementRequest;
import com.db.ess.synthesis.dvo.GetImpactedSwapAuditRequest;
import com.db.ess.synthesis.dvo.GetImpactedSwapRequest;
import com.db.ess.synthesis.dvo.GetPendingExceptionCountRequest;
import com.db.ess.synthesis.dvo.GetSecurityIdMappingRequest;
import com.db.ess.synthesis.dvo.GetSynthesisExceptionRequest;
import com.db.ess.synthesis.dvo.GetUserProfileRequest;
import com.db.ess.synthesis.dvo.PingRequest;
import com.db.ess.synthesis.dvo.PingResponse;
import com.db.ess.synthesis.dvo.UpdateAltDivCcyRequest;
import com.db.ess.synthesis.dvo.UpdateImpactedSwapDivCcyRequest;
import com.db.ess.synthesis.dvo.UpdateUserProfileRequest;
import com.db.ess.synthesis.dvo.UploadAdjustmentRequest;
import com.db.ess.synthesis.dvo.UploadExceptionRequest;
import com.db.ess.synthesis.processor.tasks.NonDaoTask;
import com.db.ess.synthesis.processor.tasks.ping.PingTask;

public class TaskFactory {
	
	private static final Logger logger = Logger.getLogger(TaskFactory.class.getName());
	
    public static <Req, Res> DaoTask create(Req request, int location) {
    	DaoTask task = null; 
    	try {
    		//--------------------
    		// tasks.divspills
    		//--------------------
    		if (request instanceof GetAltDivCcyRequest) {
    			task = new GetAltDivCcyTask((GetAltDivCcyRequest)request, location);
    		}
    		else if (request instanceof AddAltDivCcyRequest) {
    			task = new AddAltDivCcyTask((AddAltDivCcyRequest)request, location);
    		}
    		else if (request instanceof DeleteAltDivCcyRequest) {
    			task = new DeleteAltDivCcyTask((DeleteAltDivCcyRequest)request, location);
    		}
    		else if (request instanceof UpdateAltDivCcyRequest) {
    			task = new UpdateAltDivCcyTask((UpdateAltDivCcyRequest)request, location);
    		}
    		else if (request instanceof GetImpactedSwapRequest) {
    			task = new GetImpactedSwapTask((GetImpactedSwapRequest)request, location);
    		}
    		else if (request instanceof AddImpactedSwapDivCcyRequest) {
    			task = new AddImpactedSwapsTask((AddImpactedSwapDivCcyRequest)request, location);
    		}
    		else if (request instanceof UpdateImpactedSwapDivCcyRequest) {
    			task = new UpdateImpactedSwapsTask((UpdateImpactedSwapDivCcyRequest)request, location);
    		}
    		else if(request instanceof GetImpactedSwapAuditRequest) {
    			task = new GetImpactedSwapAuditTask((GetImpactedSwapAuditRequest)request, location);
    		}
    		//----------------
    		// tasks.user
    		//----------------
    		else if (request instanceof AddFunctionEntitlementRequest) {
    			task = new AddFunctionEntitlementTask((AddFunctionEntitlementRequest)request, location);
    		}
            else if (request instanceof GetAltDivCcyAuditRequest) {
    			task = new GetAltDivCcyAuditTask((GetAltDivCcyAuditRequest)request, location);
    		}    		else if (request instanceof GetUserProfileRequest) {
    			task = new GetUserProfileTask((GetUserProfileRequest)request, location);
    		}
    		else if (request instanceof GetFXRateListsRequest) {
    			task = new GetFXRateListTask((GetFXRateListsRequest)request, location);
    		}
    		else if (request instanceof GetFunctionEntitlementRequest) {
    			task = new GetFunctionEntitlementTask((GetFunctionEntitlementRequest)request, location);
    		}
    		else if (request instanceof UpdateUserProfileRequest) {
    			task = new UpdateUserProfileTask((UpdateUserProfileRequest)request, location);
    		}
    		//------------------
    		// tasks.exception
    		//------------------
    		else if (request instanceof GetSynthesisExceptionRequest) {
    			task = new GetSynthesisExceptionTask((GetSynthesisExceptionRequest)request, location);
    		}
    		else if (request instanceof GetAffectedPositionRequest) {
    			task = new GetAffectedPositionTask((GetAffectedPositionRequest)request, location);
    		}
    		/*
    		 * Upload Exceptions
    		 */
    		else if (request instanceof UploadExceptionRequest) {
    			task = new UploadSynthesisExceptionTask((UploadExceptionRequest)request, location);
    		}
    		/*
    		 * Upload Adjustments
    		 */
    		else if (request instanceof UploadAdjustmentRequest) {
    			task = new UploadAdjustmentTask((UploadAdjustmentRequest)request, location);
    		}
    		//------------------------------
    		// Close/Approve/Reject/ReRate Exceptions
    		//------------------------------
    		 if (request instanceof ExceptionActionRequest) {
    			task = new ExceptionActionTask((ExceptionActionRequest)request,location);
    		}
    		 
    		 else if (request instanceof ExceptionReRateRequest) {
     			task = new ExceptionReRateTask((ExceptionReRateRequest)request,location);
     		}
    		 /*
     		 * Create Exceptions
     		 */
     		else if (request instanceof CreateExceptionRequest) {
     			task = new CreateExceptionTask((CreateExceptionRequest)request, location);
     		}
    		
    		//------------------
    		// tasks.securityIdMapping
    		//------------------
    		else if (request instanceof GetSecurityIdMappingRequest) {
    			task = new GetSecurityIdMappingTask((GetSecurityIdMappingRequest)request, location);
    		}
    		//------------------------------
    		// add your task above
    		//------------------------------
    		else {
    			logger.warn(">>> Cannot create a Task for " + request.getClass().getName());
    		}

    	} catch (Exception e) {
    		logger.error(">>> failed to create an instance of DaoTask..", e);
    	}
    	
    	return task;
    }
    
    public static <Req, Res> NonDaoTask create(Req request) {
    	
    	NonDaoTask task = null;
    	try {
    		//------------------------
    		// controller.tasks.ping
    		//------------------------
    		if (request instanceof PingRequest) {
    			task = new PingTask((PingRequest)request);
    		}
    		//------------------------------
    		// Close/Approve/Reject Exceptions
    		//------------------------------
    		
    		//For getting pending count, location is determined by the inputs provided.
    		//Hence this particular request is considered under NonDAO.
    		if (request instanceof GetPendingExceptionCountRequest) {
    			task = new GetPendingExceptionCountTask((GetPendingExceptionCountRequest)request);
    		}
    		/*	if (request instanceof ExceptionActionRequest) {
    			task = new LocationDistributorTask((ExceptionActionRequest)request);
    		}
    		*/
    		//------------------------------
    		// add your task above
    		//------------------------------
    	} catch (Exception e) {
    		logger.error(">>> failed to create an instance of NonDaoTask..", e);
    	}
    	return task;
    }
}
