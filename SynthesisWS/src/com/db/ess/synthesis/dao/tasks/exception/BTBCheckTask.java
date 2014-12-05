package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.util.SQLUtils;

public class BTBCheckTask 
{
	private static final Logger logger = Logger.getLogger(BTBCheckTask.class.getName());
	private int location;
	private int swapNum;
	List<Integer> aList;
	//private int 
	
	public BTBCheckTask(int loc,int swapNum) {
		this.location = loc;
		this.swapNum = swapNum;
		aList = new ArrayList<Integer>();
	}


	public static final String SELECT_SEQ_ID =
		"select chainNum from SwapbtbstructChain where swapNum = ?";
	
	protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement pStmt = c.prepareStatement(SELECT_SEQ_ID);
		pStmt.setInt(1, swapNum);
    	return pStmt;
	}
	
	protected  List<Integer> run() throws SQLException {

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
		
		return aList;
	}

	protected int processResult(ResultSet rs) throws SQLException {
		int count = 0;
    	logger.info(">>> process data result for BTBCheckTask....");
    	Integer primarySwap = null;
		while (rs.next()) {
			primarySwap = (rs.getInt(1));
			if(primarySwap != null)
			{
				aList.add(1);
				aList.add(primarySwap.intValue());
			}
			count++;
		}
		if(primarySwap == null)
			aList.add(0);
		
		return count;
	}

}

