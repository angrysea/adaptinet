package com.db.ess.synthesis.validation;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.exception.SwapNumCheckTask;
import com.db.ess.synthesis.dvo.Adjustment;


public class AdjustmentSwapValidator extends BaseValidator 
{
	private static final Logger logger = Logger.getLogger(SwapNumValidator.class.getName());
	public int swapNum;
	public int location;
	public Adjustment ad;
	public AdjustmentSwapValidator(int swapNum,int location)
	{
		this.swapNum = swapNum;
		this.location = location;
	}
	
	public AdjustmentSwapValidator(int location,Adjustment ad)
	{
		this.swapNum = ad.getSwapNumber();
		this.location = location;
		this.ad = ad;
	}
	
	@Override
	public boolean validate() {
		boolean retValue = false;
		if(ad.getAction().startsWith("Del"))
			return true;
		if(swapNum==0 || swapNum==noValue) 
			return false;
		SwapNumCheckTask chkTask = new SwapNumCheckTask(location,swapNum);
		try
		{
			chkTask.run(1);
			retValue = !chkTask.isSwapNumPresent();
			if(ad!=null)
			{
				ad.setSwapId(chkTask.getSwapId());
				ad.setSwapNumberHasError(retValue);
			}
			//logger.info("Swap Id found : "+ad.getSwapId());	
		}
		catch(Exception e)
		{
			logger.error("Exception occurred while validating swapNum" + e);
		}
		return retValue;
	}

}
