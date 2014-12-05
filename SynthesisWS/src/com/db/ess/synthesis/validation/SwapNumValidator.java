package com.db.ess.synthesis.validation;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.exception.SwapNumCheckTask;
import com.db.ess.synthesis.dao.tasks.exception.UserTask;
import com.db.ess.synthesis.dvo.SynthesisException;


public class SwapNumValidator extends BaseValidator 
{
	private static final Logger logger = Logger.getLogger(SwapNumValidator.class.getName());
	public int swapNum;
	public int location;
	public SynthesisException se;
	public SwapNumValidator(int swapNum,int location)
	{
		this.swapNum = swapNum;
		this.location = location;
	}
	
	public SwapNumValidator(int location,SynthesisException se)
	{
		this.swapNum = se.getswapNumber();
		this.location = location;
		this.se = se;
	}
	
	@Override
	public boolean validate() {
		boolean retValue = false;
		
		if(swapNum==0 || swapNum==noValue) 
			return false;
		SwapNumCheckTask chkTask = new SwapNumCheckTask(location,swapNum);
		try
		{
			chkTask.run(1);
			retValue = !chkTask.isSwapNumPresent();
			if(se!=null)
			{
				se.setswapId(chkTask.getSwapId());
				se.setswapNumberHasError(retValue);
			}
				
		}
		catch(Exception e)
		{
			logger.error("Exception occurred while validating swapNum" + e);
		}
		return retValue;
	}

}
