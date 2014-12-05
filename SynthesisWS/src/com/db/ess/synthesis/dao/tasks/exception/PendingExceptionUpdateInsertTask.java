package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.db.ess.synthesis.constants.IConstants;
import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.SynthesisException;

public class PendingExceptionUpdateInsertTask extends BaseExecuteDaoTask<Integer> 
{
	List<SynthesisException> seList;
	public PendingExceptionUpdateInsertTask(int loc) {
		super(loc);
	}
	
	public PendingExceptionUpdateInsertTask(int loc, List<SynthesisException> seList) {
		super(loc);
		this.seList = seList;
	}
	public static final String ACTION_UPDATE="UPDATE";
	public static final String ACTION_INSERT="INSERT";
	
	public static final String UPDATE_DIV_EXCEP_PENDING="UPDATE " 
		+ "DividendExceptionPending SET "
		+ " institutionId= ? , swapId = ? , instrId = ? , custId = ? , "
		+ " tradeRate = ? , settleRate = ? , startDate = ? , endDate = ? , "
//		+ " userId = ?, " 
		+ " enterTime = GETDATE(), "
		+ " manual = 1 , endDateInput = ? , longShort = ? , status = 0 , "
		+ " bookSeriesId = ? , "
		+ " reasonComment = substring(ltrim(reasonComment + ' Updt To ' + ?), 1, 60), " 
		+ " countryId = ? , "
		+ "actionedByUserId = ?, "
		+ "WHERE exceptId = ? ";
	public static final String INSERT_DIV_EXCEP_PENDING=" INSERT INTO " 
		+ "DividendExceptionPending (institutionId, swapId, instrId, custId, " 
		+ "tradeRate, settleRate, startDate, endDate, userId, enterTime, exceptId, manual, "
		+ "endDateInput, longShort, "
		+ "status, "
		+ "bookSeriesId, "
		+ "requestor, "
		+ "reasonId, " 
		+ "reasonComment, countryId, actionedByUserId) values" 
		+ " (?,?,?,?,?,?,?,?,?,GETDATE(),?,1,?,?,0,?,?,?,?,?,?)";
	public static final String UPDATE_SPREAD_EXCEP_PENDING=" UPDATE " 
		+ "SpreadExceptionPending SET " 
		+ " institutionId= ? , swapId = ? , instrId = ? , custId = ? , "
		+ " spread = ? , rate = ? , startDate = ? , endDate = ? , "
//		+ " userId = ?, " 
		+ " enterTime = GETDATE(), "
		+ " manual = 1 , endDateInput = ? , longShort = ? , status = 0 , "
		+ " bookSeriesId = ? , "
		+ " reasonComment = substring(ltrim(reasonComment + ' Updt To ' + ?), 1, 60), " 
		+ " countryId = ? ,"
		+ "actionedByUserId = ?, "
+ "WHERE exceptId = ? ";
	public static final String INSERT_SPREAD_EXCEP_PENDING=" INSERT INTO " 
		+ "SpreadExceptionPending (institutionId, swapId, instrId, custId,  " 
		+ "spread, rate, startDate, endDate, userId, enterTime, exceptId, manual, "
		+ "endDateInput, longShort, "
		+ "status, "
		+ "bookSeriesId, "
		+ "requestor, "
		+ "reasonId, " 
		+ "reasonComment, countryId, actionedByUserId) values " 
		+ " (?,?,?,?,?,?,?,?,?,GETDATE(),?,1,?,?,0,?,?,?,?,?,?)";
	
	@Override
	protected PreparedStatement[] createStatement(Connection c)
			throws SQLException {
		if(seList==null)
			return null;
		List<PreparedStatement> list = new ArrayList<PreparedStatement>();
		PreparedStatement stmt = null;
		for (SynthesisException se : seList)
		{
			stmt = getPreparedStatement(se,c);
			
			
		}
		return null;
	}
	
	public PreparedStatement getPreparedStatement(SynthesisException se,Connection c) throws SQLException
	
	{
		if(se.getexceptUpdateTblName()!=null && se.getexceptUpdateTblName().contains("Pend")
				&& se.gettype().equalsIgnoreCase("Dividend"))
			return setParamsForStatement(se,c.prepareStatement(UPDATE_DIV_EXCEP_PENDING),ACTION_UPDATE);
		else if(se.getexceptUpdateTblName()!=null && se.getexceptUpdateTblName().contains("Pend")
				&& !se.gettype().equalsIgnoreCase("Dividend"))
			return setParamsForStatement(se,c.prepareStatement(UPDATE_SPREAD_EXCEP_PENDING),ACTION_UPDATE);
		else if ((se.getexceptUpdateTblName()==null || se.getexceptUpdateTblName().length()<1)
				&& se.gettype().equalsIgnoreCase("Dividend"))
			return setParamsForStatement(se,c.prepareStatement(INSERT_DIV_EXCEP_PENDING),ACTION_INSERT);
		else if ((se.getexceptUpdateTblName()==null || se.getexceptUpdateTblName().length()<1)
				&& !se.gettype().equalsIgnoreCase("Dividend"))
			return setParamsForStatement(se,c.prepareStatement(INSERT_SPREAD_EXCEP_PENDING),ACTION_INSERT);
		else
			return null;
	}
	
	public PreparedStatement setParamsForStatement(SynthesisException se, PreparedStatement stmt, 
			String action) throws SQLException
	{
		if(action.equalsIgnoreCase(ACTION_UPDATE))
		{
			
			stmt.setInt(1, getIntValueFromReq(se.getinstitutionId()));
			stmt.setInt(2, getIntValueFromReq(se.getswapId()));
			stmt.setInt(3, getIntValueFromReq(se.getinstrId()));
			stmt.setInt(4, getIntValueFromReq(se.getcustId()));
			if(se.gettype().equalsIgnoreCase("Dividend"))
			{
				stmt.setDouble(5, getDoubleValueFromReq(se.gettradeRate()));
				stmt.setDouble(6, getDoubleValueFromReq(se.getsettleRate()));
			}
			else
			{
				stmt.setDouble(5, getDoubleValueFromReq(se.getspread()));
				stmt.setDouble(6, getDoubleValueFromReq(se.getrate()));
			}
			stmt.setDate(7, getDateValueFromReq(se.getstartDate())==null?null:
				new java.sql.Date(se.getstartDate().getTime()));
			stmt.setDate(8, getDateValueFromReq(se.getendDate())==null?null:
				new java.sql.Date(se.getendDate().getTime()));
			//stmt.setInt(10, getIntValueFromReq(se.getuserId()));
			stmt.setDate(9, getDateValueFromReq(se.getendDate())==null?null:
				new java.sql.Date(se.getendDate().getTime()));
			stmt.setInt(10, se.getlongShort()); // verify the numbers
			stmt.setInt(11, se.getbookSeriesId());
			stmt.setString(12, getStringValueFromReq(se.getreasonComment()));
			stmt.setInt(13, getIntValueFromReq(se.getcountryId()));
			stmt.setInt(14, se.getuserId());
			stmt.setInt(15, se.getexceptId()); //Update will always have an exceptId
			
		}
		else if (action.equalsIgnoreCase(ACTION_INSERT))
		{
			stmt.setInt(1, getIntValueFromReq(se.getinstitutionId()));
			stmt.setInt(2, getIntValueFromReq(se.getswapId()));
			stmt.setInt(3, getIntValueFromReq(se.getinstrId()));
			stmt.setInt(4, getIntValueFromReq(se.getcustId()));
			if(se.gettype().equalsIgnoreCase("Dividend"))
			{
				stmt.setDouble(5, getDoubleValueFromReq(se.gettradeRate()));
				stmt.setDouble(6, getDoubleValueFromReq(se.getsettleRate()));
			}
			else
			{
				stmt.setDouble(5, getDoubleValueFromReq(se.getspread()));
				stmt.setDouble(6, getDoubleValueFromReq(se.getrate()));
			}
			stmt.setDate(7, getDateValueFromReq(se.getstartDate())==null?null:
				new java.sql.Date(se.getstartDate().getTime()));
			stmt.setDate(8, getDateValueFromReq(se.getendDate())==null?null:
				new java.sql.Date(se.getendDate().getTime()));
			stmt.setInt(9, getIntValueFromReq(se.getuserId()));
			stmt.setInt(10, getIntValueFromReq(se.getexceptId()));
			
			stmt.setDate(11, getDateValueFromReq(se.getendDate())==null?null:
				new java.sql.Date(se.getendDate().getTime()));
			stmt.setInt(12, getIntValueFromReq(se.getlongShort()));
			stmt.setInt(13, getIntValueFromReq(se.getbookSeriesId()));
			stmt.setString(14, getStringValueFromReq(se.getuserName()));
			stmt.setInt(15, getIntValueFromReq(se.getreasonId()));
			stmt.setString(16, getStringValueFromReq(se.getreasonComment()));
			stmt.setInt(17, getIntValueFromReq(se.getcountryId()));
			stmt.setInt(18, getIntValueFromReq(se.getuserId()));
			
		}	
		
		
		return stmt;
	}
	
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
	
	public Date getDateValueFromReq(Date value)
	{
		Date retVal = null;
		if(value==null)
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
}
