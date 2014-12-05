package com.db.ess.synthesis.validation;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.exception.CountryTask;
import com.db.ess.synthesis.dvo.SynthesisException;

public class CountryValidator extends BaseValidator 
{
	private static final Logger logger = Logger.getLogger(CountryValidator.class.getName());
	public int countryId;
	public int location;
	public String ccCode;
	public SynthesisException se;
	
	public CountryValidator(String ccCode,int location)
	{
		this.ccCode = ccCode;
		this.location = location;
	}
	
	public CountryValidator(int location,SynthesisException se)
	{
		this.ccCode = se.getcountryCode();
		this.location = location;
		this.se = se;
	}
	
	@Override
	public boolean validate() {
		boolean retValue = false;
		
		if(ccCode==null || ccCode.isEmpty()  || ccCode.length() <1) 
			return false;
		CountryTask cntryTask = new CountryTask(location,se.getcountryCode());
		try
		{
			cntryTask.run(1);
			retValue = !cntryTask.isCountryPresent();
			if(se!=null)
			{
				se.setcountryId(cntryTask.getCountryId());
				se.setcountryCodeHasError(retValue);
			}
				
		}
		catch(Exception e)
		{
			logger.error("Exception occurred while validating country Code" + e);
		}
		return retValue;
	}

}

