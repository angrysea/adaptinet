package com.db.ess.synthesis.statement;

import com.db.ess.synthesis.constants.IConstants;
import com.db.ess.synthesis.dvo.SynthesisException;

public class UpdatePendingExceptionQueryWrapper 
{
	public  String UPDATE_DIV_EXCEP_PENDING_PART1="UPDATE " 
		+ "DividendExceptionPending SET ";
	public  String UPDATE_DIV_EXCEP_PENDING_PART2 =  " tradeRate = ? , settleRate = ? ,";
	
	public String UPDATE_SPREAD_EXCEP_PENDING_PART1=" UPDATE " 
		+ "SpreadExceptionPending SET " ;
	public String UPDATE_SPREAD_EXCEP_PENDING_PART2 = " spread = ? , rate = ? , ";
	
	public  String COMMON_PART = " startDate = ? , endDate = ? , "
		+ " enterTime = GETDATE(), "
		+ " manual = 1 , endDateInput = ? , longShort = ? , status = 0 , "
		+ " bookSeriesId = ? , "
		+ " reasonId = ? , "
		+ " reasonComment = substring(ltrim(reasonComment + ' Updt To ' + ?), 1, 60), " 
		+ "actionedByUserId = ? "
		+ "WHERE exceptId = ? ";
	
	public String getQuery(SynthesisException se)
	{
		String retVal = null;
		StringBuilder movingVars = new StringBuilder();
		if(se.getinstitutionId()!=0 && se.getinstitutionId()!=IConstants.noValue)
		{
			movingVars.append(" institutionId= ? ,");
		}
			
		if(se.getswapId()!=0 && se.getswapId()!=IConstants.noValue)
		{
			movingVars.append(" swapId = ? ,");
		}
		if(se.getinstrId()!=0 && se.getinstrId()!=IConstants.noValue)
		{
			movingVars.append(" instrId = ? ,");
		}
		if(se.getcustId()!=0 && se.getcustId()!=IConstants.noValue)
		{
			movingVars.append(" custId = ? , ");
		}
		if(se.getcountryId()!=0 && se.getcountryId()!=IConstants.noValue)
		{
			movingVars.append(" countryId = ? ,");
		}
		
		if(se.gettype().equalsIgnoreCase("Dividend"))
			retVal = UPDATE_DIV_EXCEP_PENDING_PART1 + movingVars.toString() + UPDATE_DIV_EXCEP_PENDING_PART2
			+ COMMON_PART;
		else
			retVal = UPDATE_SPREAD_EXCEP_PENDING_PART1 + movingVars.toString() + UPDATE_SPREAD_EXCEP_PENDING_PART2
			+ COMMON_PART;
		
		return retVal;
	}

}
