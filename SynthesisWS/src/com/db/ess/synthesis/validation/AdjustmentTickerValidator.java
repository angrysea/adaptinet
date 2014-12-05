package com.db.ess.synthesis.validation;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.exception.InstrumentTask;
import com.db.ess.synthesis.dvo.Adjustment;


public class AdjustmentTickerValidator extends BaseValidator 
{
	private static final Logger logger = Logger.getLogger(TickerValidator.class.getName());
	public String ticker;
	public int location;
	public int instrId;
	public Adjustment ad;
	
	public AdjustmentTickerValidator(String ticker,int location)
	{
		this.ticker = ticker;
		this.location = location;
	}
	public AdjustmentTickerValidator(int location,Adjustment ad)
	{
		this.ticker = ad.getTicker();
		this.location = location;
		this.ad = ad;
	}
	@Override
	public boolean validate() {
		boolean retValue = true;
		if(ad.getAction().startsWith("Del"))
			return true;
		if(ticker==null || ticker.trim().length()<1 )
			return false;
		InstrumentTask instrTask= new InstrumentTask(location,ticker);
		try
		{
			instrTask.run(1);
			retValue = !instrTask.isInstrPresent();
			if(ad!=null)
			{
				ad.setInstrumentId(instrTask.getInstrId());
				ad.setTickerHasError(retValue);
			}
				
		}
		catch(Exception e)
		{
			logger.error(" Exception occurred while validating ticker...."+e);
		}

		return retValue;
	}
}
