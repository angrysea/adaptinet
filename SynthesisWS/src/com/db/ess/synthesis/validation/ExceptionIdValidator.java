package com.db.ess.synthesis.validation;

import com.db.ess.synthesis.dao.tasks.exception.ExceptionIdCheckTask;
import com.db.ess.synthesis.dvo.SynthesisException;
import com.db.ess.synthesis.util.cache.RefCache;

public class ExceptionIdValidator extends BaseValidator
{

	int exceptionId;
	int location;
	String type;
	String tableName;
	SynthesisException se;
	public ExceptionIdValidator(int exceptionId,int location)
	{
		this.exceptionId = exceptionId;
		this.location = location;
	}
	public ExceptionIdValidator(SynthesisException se ,int location)
	{
		this.se = se;
		this.exceptionId = se.getexceptId();
		this.location = location;
		this.type = se.gettype();
	}
	@Override
	public boolean validate() {
		boolean retValue = false;
		
		if(exceptionId==0 || exceptionId==noValue) 
			return false;
		ExceptionIdCheckTask chkTask = new ExceptionIdCheckTask(location,exceptionId,type);
		try
		{
			chkTask.run(1);
			retValue = !chkTask.isExceptIdPresent();
			if(chkTask.isExceptIdPresent())
				se.setexceptUpdateTblName(chkTask.getTableName());
		}
		catch(Exception e)
		{
			//Put some logging
		}
		se.setexceptIdHasError(retValue);
		return retValue;
	}
	
}