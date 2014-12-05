package com.db.ess.synthesis.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

import com.db.ess.synthesis.constants.IConstants;
import com.db.ess.synthesis.dvo.SynthesisException;

public abstract class BaseCreateStatement implements ICreateStatement 
{
	Connection conn = null;
	
	public BaseCreateStatement(Connection conn) 
	{
		this.conn = conn;
	}
	
	public abstract List<PreparedStatement> createStatement() throws Exception ;
	
	public Integer getIntValueFromReq(int value)
	{
		Integer retVal = null;
		if(value==0 || value==IConstants.noValue)
			retVal = null;
		else
			retVal =  value;
		
		return retVal;
	}
	
	public String getStringValueFromReq(String value)
	{
		String retVal = null;
		if(value==null || value.length()<1)
			retVal = null;
		else
			retVal =  value;
		
		return retVal;
	}
	
	public Float getFloatValueFromReq(Float value)
	{
		Float retVal = null;
		if(value==0 || value==IConstants.noValue)
			retVal = null;
		else
			retVal =  value;
		
		return retVal;
	}
	
	public Double getDoubleValueFromReq(Double value)
	{
		Double retVal = null;
		if(value==0 || value==IConstants.noValue)
			retVal = null;
		else
			retVal =  value;
		
		return retVal;
	}
	
	public Date getDateValueFromReq(Date value)
	{
		Date retVal = null;
		if(value==null)
			retVal = null;
		else
			retVal =  value;
		
		return retVal;
	}
	
	public int getSpreadCode(SynthesisException se)
	{
		if(se.gettype().equalsIgnoreCase("Rate"))
			return 0;
		else if(se.gettype().equalsIgnoreCase("Finance"))
			return 1;
		else
			return 2;
	}
	
	public double getRateColumnValue(SynthesisException se,int spreadCode)
	{
		if(spreadCode == 0)
			return se.getrate();
		else if (spreadCode == 1)
			return se.getspread();
		else
			return se.getborrowCost();
	}
	
}
