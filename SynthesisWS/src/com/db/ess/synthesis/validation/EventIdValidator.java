package com.db.ess.synthesis.validation;

import com.db.ess.synthesis.dao.tasks.adjustment.EventIdCheckTask;
import com.db.ess.synthesis.dao.tasks.exception.ExceptionIdCheckTask;
import com.db.ess.synthesis.dvo.Adjustment;
import com.db.ess.synthesis.util.cache.RefCache;

public class EventIdValidator extends BaseValidator
{

	int eventId;
	int location;
	String tableName;
	Adjustment ad;
	public EventIdValidator(int eventId,int location)
	{
		this.eventId = eventId;
		this.location = location;
	}
	public EventIdValidator(Adjustment ad ,int location)
	{
		this.ad = ad;
		this.eventId = ad.getEventId();
		this.location = location;
	}
	@Override
	public boolean validate() {
		boolean retValue = false;
		
		if(ad.getAction().startsWith("New"))
			return true;
		if(eventId==0 || eventId==noValue) 
			return false;
		EventIdCheckTask chkTask = new EventIdCheckTask(location,eventId);
		try
		{
			chkTask.run(1);
			retValue = !chkTask.isEventIdPresent();
		}
		catch(Exception e)
		{
			//Put some logging
		}
		ad.setEventIdHasError(retValue);
		return retValue;
	}
	
}