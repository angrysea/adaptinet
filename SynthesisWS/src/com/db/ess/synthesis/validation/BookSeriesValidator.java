package com.db.ess.synthesis.validation;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.exception.ApproverBookSeriesTask;
import com.db.ess.synthesis.dao.tasks.exception.BookSeriesTask;
import com.db.ess.synthesis.dvo.SynthesisException;

public class BookSeriesValidator extends BaseValidator 
{
	private static final Logger logger = Logger.getLogger(BookSeriesValidator.class.getName());
	String bookSeries;
	int location;
	String userEmail;
	String errorMsg;
	boolean hasTraderAccess;
	SynthesisException se;
	
	public BookSeriesValidator(String bookSeries,int location,String userEmail)
	{
		this.bookSeries = bookSeries;
		this.location = location;
		this.userEmail = userEmail;
	}
	
	public BookSeriesValidator(int location,boolean hasTraderAccess, SynthesisException se)
	{
		this.bookSeries = se.getbookSeries();
		this.location = location;
		this.userEmail = se.getuserName();
		this.se = se;
		this.hasTraderAccess = hasTraderAccess;
	}
	
	@Override
	public boolean validate() {
		boolean retValue = false;
		
		if(bookSeries==null || bookSeries.length()<1) 
			return false;
		try
		{
			boolean bookIsValid = validateBookSeries();
			boolean approverIsValid = validateApproverBookSeries();
			retValue = !bookIsValid || !approverIsValid;
			
			if(!bookIsValid && !approverIsValid)
				errorMsg = "Invalid Book series for the given location and User doesnt have access on the book series";
			else if(!bookIsValid)
				errorMsg = "Invalid Book series for the given location ";
			else if (!approverIsValid)
				errorMsg = "User doesnt have  access on the book series ";
			
			if(se!=null)
			{
				se.setbookSeriesHasError(retValue);
				se.setbookSeriesError(errorMsg);
			}
				
		}
		catch(Exception e)
		{
			logger.error("Exception Occurred while validating BookSeries"+e);
		}
		return retValue;
	}
	
	public boolean validateBookSeries()
	{
		boolean retVal = true;
		try
		{
			BookSeriesTask bkSeriesTask = new BookSeriesTask(location,bookSeries);
			bkSeriesTask.run(1);
			retVal = bkSeriesTask.getBookIsValid();	
			if(se!=null)
				se.setbookSeriesId(bkSeriesTask.getBookSeriesId());
		}
		catch(Exception e)
		{
			logger.error("Exception Occurred while validating BookSeries"+e);
		}
		
		return retVal;
		
	}
	
	
	public boolean validateApproverBookSeries()
	{
		boolean retVal = true;
		if(hasTraderAccess)
		{
			try
			{
				ApproverBookSeriesTask apprvTask = new ApproverBookSeriesTask(location,bookSeries,userEmail);
				apprvTask.run(1);	
				retVal = apprvTask.getUserHasBookAccess();
			}
			catch(Exception e)
			{
				logger.error("Exception Occurred while validating ApproverBookSeries"+e);
			}
			
		}
		
		return retVal;
		
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
