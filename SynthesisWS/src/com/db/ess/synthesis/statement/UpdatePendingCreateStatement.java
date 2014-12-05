package com.db.ess.synthesis.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.SynthesisException;

public class UpdatePendingCreateStatement extends BaseCreateStatement 
{
	private static final Logger logger = Logger.getLogger(UpdatePendingCreateStatement.class.getName());
	List<SynthesisException> seList;
	
	public UpdatePendingCreateStatement(Connection conn, List<SynthesisException> seList)
	{
		super(conn);
		this.seList = seList;
	}
	
	@Override
	public List<PreparedStatement> createStatement() throws SQLException 
	{
		if(seList==null || seList.isEmpty() || conn==null)
			return Collections.EMPTY_LIST;
		List<PreparedStatement> listPS = new ArrayList<PreparedStatement>(); 
		PreparedStatement stmt = null;
		String query = null;
		UpdatePendingExceptionQueryWrapper queryWrp = new UpdatePendingExceptionQueryWrapper();
		for (SynthesisException se : seList)
		{
			query = queryWrp.getQuery(se);
			stmt = conn.prepareStatement(query);
			stmt = setParamsForStatement(se,stmt);
			listPS.add(stmt);
			
		}
		
		return listPS;
	
	}

	public PreparedStatement setParamsForStatement(SynthesisException se, PreparedStatement stmt) throws SQLException
	{
		if(stmt==null)
			return null;
		int i =1;
		 if(getIntValueFromReq(se.getinstitutionId())!=null)
			 stmt.setInt(i++, se.getinstitutionId());
		 if(getIntValueFromReq(se.getswapId())!=null)
			 stmt.setInt(i++, se.getswapId());
		 if(getIntValueFromReq(se.getinstrId())!=null)
			 stmt.setInt(i++, se.getinstrId());
		 if(getIntValueFromReq(se.getcustId())!=null)
			 stmt.setInt(i++, se.getcustId());
		 if(getIntValueFromReq(se.getcountryId())!=null)
			 stmt.setInt(i++, se.getcountryId());
		if(se.gettype().equalsIgnoreCase("Dividend"))
		{
			stmt.setDouble(i++, getDoubleValueFromReq(se.gettradeRate())==null?0:getDoubleValueFromReq(se.gettradeRate()));
			stmt.setDouble(i++, getDoubleValueFromReq(se.getsettleRate())==null?0:getDoubleValueFromReq(se.getsettleRate()));
		}
		else
		{
			// here the spread value determines whether its rate/spread/borrowCost exception 0,1,2
			stmt.setDouble(i++, getSpreadCode(se));
			stmt.setDouble(i++, getRateColumnValue(se,getSpreadCode(se)));
		}
		stmt.setDate(i++, getDateValueFromReq(se.getstartDate())==null?null:
			new java.sql.Date(se.getstartDate().getTime()));
		stmt.setDate(i++, getDateValueFromReq(se.getendDate())==null?null:
			new java.sql.Date(se.getendDate().getTime()));
		stmt.setDate(i++, getDateValueFromReq(se.getendDate())==null?null:
			new java.sql.Date(se.getendDate().getTime()));
		stmt.setInt(i++, se.getlongShort());
		stmt.setInt(i++, se.getbookSeriesId());
		stmt.setInt(i++, se.getreasonId());
		stmt.setString(i++, getStringValueFromReq(se.getreasonComment()));
		stmt.setInt(i++, se.getuserId());
		stmt.setInt(i++, se.getexceptId());		
		logger.info("Number of params set in UPDATE query >>> " + i);
		return stmt;

	}

}
