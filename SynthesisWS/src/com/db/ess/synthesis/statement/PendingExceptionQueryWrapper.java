package com.db.ess.synthesis.statement;

import com.db.ess.synthesis.constants.IConstants;
import com.db.ess.synthesis.dvo.SynthesisException;

public class PendingExceptionQueryWrapper 
{
	public  String INSERT_DIV_EXCEP_PEND_PART1=" INSERT INTO " 
		+ "DividendExceptionPending ( ";
	public  String INSERT_DIV_EXCEP_PEND_PART2 =  " tradeRate, settleRate, ";
	
	public String INSERT_SPREAD_EXCEP_PENDING1=" INSERT INTO " 
		+ "SpreadExceptionPending ( ";
	public String INSERT_SPREAD_EXCEP_PENDING2 = " spread, rate, ";
	
	public  String COMMON_PART1 = " startDate, endDate, userId, enterTime, exceptId, manual, "
		+ " endDateInput, longShort, "
		+ " status, bookSeriesId, "
		+ " requestor,  reasonId, "
		+ " reasonComment, actionedByUserId) values" 
		+ " (";
		public  String COMMON_PART2 =  "?,?,?,?,?,GETDATE(),?,1," 
		+   "?,?," 
		+ 	"0,?," 
		+	"?,?," 
		+	"?,?)";
	
	public String getQuery(SynthesisException se)
	{
		String retVal = null;
		StringBuilder movingVars = new StringBuilder();
		StringBuilder movingParams = new StringBuilder();
		if(se.getinstitutionId()!=0 && se.getinstitutionId()!=IConstants.noValue)
		{
			movingVars.append(" institutionId,");
			movingParams.append(" ?, ");
		}
			
		if(se.getswapId()!=0 && se.getswapId()!=IConstants.noValue)
		{
			movingVars.append(" swapId,");
			movingParams.append(" ?, ");	
		}
		if(se.getinstrId()!=0 && se.getinstrId()!=IConstants.noValue)
		{
			movingVars.append(" instrId,");
			movingParams.append(" ?, ");	
		}
		if(se.getcustId()!=0 && se.getcustId()!=IConstants.noValue)
		{
			movingVars.append(" custId,");
			movingParams.append(" ?, ");
		}
		if(se.getcountryId()!=0 && se.getcountryId()!=IConstants.noValue)
		{
			movingVars.append(" countryId,");
			movingParams.append(" ?, ");
		}
		
		if(se.gettype().equalsIgnoreCase("Dividend"))
			retVal = INSERT_DIV_EXCEP_PEND_PART1 + movingVars.toString() + INSERT_DIV_EXCEP_PEND_PART2
			+ COMMON_PART1 + movingParams.toString() + COMMON_PART2;
		else
			retVal = INSERT_SPREAD_EXCEP_PENDING1 + movingVars.toString() + INSERT_SPREAD_EXCEP_PENDING2
			+ COMMON_PART1 + movingParams.toString() + COMMON_PART2;
		
		return retVal;
	}

}
