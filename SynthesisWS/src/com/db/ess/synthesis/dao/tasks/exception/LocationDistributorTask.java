package com.db.ess.synthesis.dao.tasks.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.constants.IConstants;
import com.db.ess.synthesis.dvo.ExceptionActionRequest;
import com.db.ess.synthesis.dvo.ExceptionActionResponse;
import com.db.ess.synthesis.dvo.SynthesisException;
import com.db.ess.synthesis.processor.tasks.NonDaoTask;
import com.db.ess.synthesis.util.ESSLocation;

public class LocationDistributorTask implements NonDaoTask<ExceptionActionResponse>{

private ExceptionActionRequest request;
private static final Logger logger = Logger.getLogger(LocationDistributorTask.class.getName());
	
	public LocationDistributorTask(ExceptionActionRequest request) {
		this.request = request;
	}
	
	@Override
	public void run(ExceptionActionResponse response) throws Exception 
	{
		// Create another task for this or something.
		logger.info(" inside LocationDistributorTask");
		
		List<SynthesisException> reqLDNList = new ArrayList<SynthesisException>();
		List<SynthesisException> reqNYList = new ArrayList<SynthesisException>();
		SynthesisException se = null;
		String action = request.getaction();
				if(request.getexceptionsIterator()!=null)
				{
					Iterator itr = request.getexceptionsIterator();
					while(itr.hasNext())
					{
						se = (SynthesisException)itr.next();
						if(se!=null && se.getlocation()==ESSLocation.LONDON)
							reqLDNList.add(se);
						else if(se!=null && se.getlocation()==ESSLocation.NEWYORK)
							reqNYList.add(se);
					}	
					
				}
				/*
				if(!reqLDNList.isEmpty())
					new ExceptionActionTask(reqLDNList, ESSLocation.LONDON,action,request.getisTrader(),request.getuserId(),request.getexpCloseTime(),request.getexpComments()).run(response);
				if(!reqNYList.isEmpty())
					new ExceptionActionTask(reqNYList, ESSLocation.NEWYORK,action,request.getisTrader(),request.getuserId(),request.getexpCloseTime(),request.getexpComments()).run(response);
					*/
		
	}

}
