package com.db.ess.synthesis.validation;

import com.db.ess.synthesis.util.cache.RefCache;

public class InstitutionIdValidator extends BaseValidator 
{
	int instititutionId;
	int location;
	public InstitutionIdValidator(int instId,int location)
	{
		this.instititutionId = instId;
		this.location = location;
	}
	@Override
	public boolean validate() {
		if(instititutionId==0 || instititutionId==noValue) return false;
		if (RefCache.getInstitution(instititutionId, location)==null)
			return true;
		else 
			return false;
	}
}
