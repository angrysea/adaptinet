package com.db.ess.synthesis.validation;

import com.db.ess.synthesis.util.cache.RefCache;

public class CustomerIdValidator extends BaseValidator
{

	int customerId;
	int location;
	public CustomerIdValidator(int custId,int location)
	{
		this.customerId = custId;
		this.location = location;
	}
	@Override
	public boolean validate() {
		if(customerId==0 || customerId==noValue) return false;
		if (RefCache.getLegalEntity(customerId, location)==null)
			return true;
		else 
			return false;
	}
	
}
