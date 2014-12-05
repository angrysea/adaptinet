package com.db.ess.synthesis.validation;

import java.util.Date;

import com.db.ess.synthesis.constants.IConstants;

public abstract class BaseValidator implements IValidator
{
	
	public abstract boolean validate();
	public static final int noValue = IConstants.noValue;
	protected boolean validate(int i,int location)
	{
		return false;
		
	}
	
	protected boolean validate(String str,int location)
	{
		return false;
		
	}
	
	protected boolean validate(float f,int location)
	{
		return false;
		
	}
	
	protected boolean validate(Date dt,int location)
	{
		return false;
		
	}
	
	protected boolean validate(double db,int location)
	{
		return false;
		
	}
}