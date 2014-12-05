package com.db.ess.synthesis.validation;

import org.apache.log4j.Logger;
import com.db.ess.synthesis.dao.tasks.exception.UserTask;
import com.db.ess.synthesis.dvo.SynthesisException;

public class UserValidator extends BaseValidator 
{
	private static final Logger logger = Logger.getLogger(UserValidator.class.getName());
	public String userEmail;
	public int userId;
	public int location;
	public SynthesisException se;
	
	public UserValidator(String userEmail,int location)
	{
		this.userEmail = userEmail;
		this.location = location;
	}
	
	public UserValidator(int location,SynthesisException se)
	{
		this.userEmail = se.getuserName();
		this.location = location;
		this.se = se;
	}
	
	@Override
	public boolean validate() {
		boolean retValue = false;
		
		if(userEmail==null || userEmail.isEmpty() || userEmail.length() <1) 
			return true;
		UserTask userTask= new UserTask(location,userEmail);
		try
		{
			userTask.run(1);
			retValue = !userTask.isUserPresent();
			if(se!=null)
			{
				se.setuserId(userTask.getUserId());
				se.setuserHasError(retValue);
			}
				
		}
		catch(Exception e)
		{
			logger.error(" Exception occurred while validating user...."+e);
		}
		return retValue;
	}

}
