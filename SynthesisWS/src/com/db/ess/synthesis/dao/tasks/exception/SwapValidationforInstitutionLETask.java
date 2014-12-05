package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.util.SQLUtils;

public class SwapValidationforInstitutionLETask
{
	private static final Logger logger = Logger.getLogger(SwapValidationforInstitutionLETask.class.getName());
	private int institutionId;
	private int legalEntityId;
	private int swapNum;
	private int location;

	private boolean swapValid = false;
	private String SELECT_SWAP_COMMON = " select 1 from Swap s,LegalEntity le where s.swapNum=?";

	public SwapValidationforInstitutionLETask(int loc, int institutionId, int legalEntityId, int swapNum) {
		this.location = loc;
		this.institutionId = institutionId;
		this.legalEntityId = legalEntityId;
		this.swapNum = swapNum;
	}
	
	public String constructQuery()
	{
		if(institutionId > 0 && legalEntityId > 0)
	 	{
			SELECT_SWAP_COMMON = SELECT_SWAP_COMMON + " and s.customerId=le.legalEntityId and le.legalEntityId=? and le.institutionId=? ";
	 	}
		else if(institutionId > 0)
	 	{
			SELECT_SWAP_COMMON = SELECT_SWAP_COMMON + " and s.customerId=le.legalEntityId and le.institutionId=? ";
	 	}
		else if(legalEntityId > 0)
	 	{
			SELECT_SWAP_COMMON = SELECT_SWAP_COMMON + " and s.customerId=? and s.customerId=le.legalEntityId ";
	 	}
		
	 	return SELECT_SWAP_COMMON;
	}
	
	
	protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement(constructQuery());
		//logger.info("Select Query for SwapValidate : "+SELECT_SWAP_COMMON);
		stmt.setInt(1, swapNum);
		if(institutionId > 0 && legalEntityId > 0)
		{
			stmt.setInt(2, legalEntityId);
			stmt.setInt(3, institutionId);
		}
		else if(institutionId > 0)
		{
			stmt.setInt(2, institutionId);
		}
		else if(legalEntityId > 0)
		{
			stmt.setInt(2, legalEntityId);
		}
		
    	return stmt;
	}


	protected boolean run() throws SQLException {

    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			stmt = createStatement(conn);
			stmt.setQueryTimeout(300);
			
				rs = ((PreparedStatement)stmt).executeQuery();
				logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
				int count = processResult(rs);
			    logger.info("Found ["+count+"] rows in " + location);	
			
		} catch (SQLException ex) {
			logger.error("Failed to execute query: ", ex); 
            throw ex;			
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		
		return getSwapIsValid();
	}

	protected int processResult(ResultSet rs) throws SQLException {
		int count = 0;
    	logger.info(">>> process data result for SwapValidationforInstitutionLETask....");
    	
		while (rs.next()) {
			setSwapIsValid(true);
			count++;
		}
		
		return count;
	}
	
	public boolean getSwapIsValid() {
		return swapValid;
	}
	public void setSwapIsValid(boolean swapValid) {
		this.swapValid = swapValid;
	}

}

