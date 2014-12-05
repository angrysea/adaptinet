package com.db.ess.synthesis.dao.tasks.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.CreateExceptionRequest;
import com.db.ess.synthesis.dvo.CreateExceptionResponse;
import com.db.ess.synthesis.dvo.ResponseParams;
import com.db.ess.synthesis.dvo.TickerLevelParams;

public class CreateExceptionValidatorTask {
	private static final Logger logger = Logger.getLogger(CreateExceptionValidatorTask.class.getName());
	private CreateExceptionRequest inputRequest;
	public List<TickerLevelParams> inputTickerParamsList = null;
	private ResponseParams resParams;
	public CreateExceptionResponse outputResponse;
	private ResponseParams rParams;
	public int errorCount = 0;
	private int institutionId = 0;
	private int legalEntityId = 0;
	private String[] swapArr;
	private int location;
	
	
	public CreateExceptionValidatorTask(CreateExceptionRequest req, CreateExceptionResponse res) {
		this.inputRequest = req;
		this.outputResponse = res;
		this.inputTickerParamsList = getTickerParams(req);
		
	}
	
	public CreateExceptionResponse validate()
	{
		logger.info("Inside validate method for Create Exception");
		swapArr = inputRequest.getswapNumber()!=null?(inputRequest.getswapNumber().length()>0?inputRequest.getswapNumber().split(","):null):null;
		institutionId = inputRequest.getinstitution();
		legalEntityId = inputRequest.getlegalEntity();
		location = inputRequest.getlocation();
		try
		{
			for(int i=0; ((swapArr==null || swapArr.length==0)?i<1:i<swapArr.length); i++)
			{
				resParams = new ResponseParams();
				if(swapArr!=null && swapArr[i] != null)
				{
						List<Integer> btbValidateList = new BTBCheckTask(location,Integer.parseInt(swapArr[i])).run();
						if( (btbValidateList.size()>0?btbValidateList.get(0):0) == 1)
							resParams.setisBTB(true);
						resParams.setswapNumber(Integer.parseInt(swapArr[i]));
						if(institutionId > 0 || legalEntityId > 0)
							resParams.setswapNumberHasError(!(new SwapValidationforInstitutionLETask(location, institutionId, legalEntityId, Integer.parseInt(swapArr[i])).run()));
						//primSwap = btbValidateList.size()>1?btbValidateList.get(1):0;
				}
				resParams.setlegalEntity(legalEntityId);
				if(institutionId > 0 && legalEntityId > 0)
					resParams.setlegalEntityHasError(!(new InstitutionLegalEntCheckTask(location, institutionId, legalEntityId).run()));
				
				for(TickerLevelParams tParams : inputTickerParamsList)
				{
					rParams = new ResponseParams();
					
					rParams.setisBTB(resParams.getisBTB());
					rParams.setlegalEntity(resParams.getlegalEntity());
					rParams.setlegalEntityHasError(resParams.getlegalEntityHasError());
					rParams.setswapNumber(resParams.getswapNumber());
					rParams.setswapNumberHasError(resParams.getswapNumberHasError());
					
					if(tParams.getticker()!=null && !tParams.getticker().equals("All"))
					{
						InstrumentTask instObj = new InstrumentTask(location, tParams.getticker());
						instObj.run(1);
							if(instObj.isInstrPresent())
							{
								rParams.setticker(tParams.getticker());
								rParams.settickerHasError(false);
							}
							else
							{
								rParams.setticker(tParams.getticker());
								rParams.settickerHasError(true);
							}
					}
					
					if(rParams.getisBTB() || rParams.getswapNumberHasError() || rParams.getlegalEntityHasError() || rParams.gettickerHasError())
					{
						rParams.setisActionAccess(false);
						outputResponse.setresponseParam(rParams);
						errorCount++;
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.error("Error in validating Exception : ",e);
			
		}
		return outputResponse;
	}
	
	public int getErrorCount() {
		return errorCount;
	}
	
	public List<TickerLevelParams> getTickerParams(CreateExceptionRequest request)
	{
		List<TickerLevelParams> reqList = new ArrayList<TickerLevelParams>();
		TickerLevelParams se = null;
		if(request.gettickerLevelParamsIterator()!=null)
		{
			Iterator itr = request.gettickerLevelParamsIterator();
			while(itr.hasNext())
			{
				se = (TickerLevelParams)itr.next();
				reqList.add(se);
			}	
		}
		return reqList;
	}
	
}
