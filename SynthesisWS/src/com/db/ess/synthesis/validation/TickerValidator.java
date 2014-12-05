package com.db.ess.synthesis.validation;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.exception.InstrumentTask;
import com.db.ess.synthesis.dvo.SynthesisException;


public class TickerValidator extends BaseValidator 
{
	private static final Logger logger = Logger.getLogger(TickerValidator.class.getName());
	public String ticker;
	public int location;
	public int instrId;
	public SynthesisException se;
	
	public TickerValidator(String ticker,int location)
	{
		this.ticker = ticker;
		this.location = location;
	}
	public TickerValidator(int location,SynthesisException se)
	{
		this.ticker = se.getticker();
		this.location = location;
		this.se = se;
	}
	@Override
	public boolean validate() {
		boolean retValue = true;
		if(ticker==null || ticker.trim().length()<1 )
			return false;
		/*List<CacheEntry> tickerList =RefCache.getSecurityList(location, "Ticker"); 
		for(CacheEntry ce : tickerList)
		{
			if(ce.getvalue().equalsIgnoreCase(ticker))
			{
				retValue = false;
				break;
			}
		}
			return retValue;*/
		InstrumentTask instrTask= new InstrumentTask(location,ticker);
		try
		{
			instrTask.run(1);
			retValue = !instrTask.isInstrPresent();
			if(se!=null)
			{
				se.setinstrId(instrTask.getInstrId());
				se.settickerHasError(retValue);
			}
				
		}
		catch(Exception e)
		{
			logger.error(" Exception occurred while validating ticker...."+e);
		}
		return retValue;
	}
}
