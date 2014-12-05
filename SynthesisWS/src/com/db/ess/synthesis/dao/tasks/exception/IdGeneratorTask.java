package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.util.SQLUtils;

import java.util.Collections;

public class IdGeneratorTask 
{
	private static final Logger logger = Logger.getLogger(IdGeneratorTask.class.getName());
	private int location;
	private int range;
	private int type;
	private int numReq;
	List<Integer> idList;
	//private int 
	
	public IdGeneratorTask(int loc,int range,int type,int numReq) {
		this.location = loc;
		this.range = range;
		this.type = type;
		this.numReq = numReq;
		idList = new ArrayList<Integer>(numReq);
	}


	public static final String SELECT_SEQ_ID =
		"{ call dbo.get_obj_id_lock( @range = ? , @category = ? ) }";
	
	public Statement createStatement(Connection c) throws SQLException {
		CallableStatement cStmt = c.prepareCall(SELECT_SEQ_ID);
		cStmt.setInt(1, range);
		cStmt.setInt(2, type);
    	return cStmt;
	}
	
	public  List<Integer> run() throws SQLException {
		if(numReq==0)
			return Collections.emptyList();
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			stmt = createStatement(conn);
			stmt.setQueryTimeout(300);
			while(numReq>0)
			{
				rs = ((CallableStatement)stmt).executeQuery();
				logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
				int count = processResult(rs);
			    logger.info("Found ["+count+"] rows in " + location);	
			    numReq--;
			}
			
		} catch (SQLException ex) {
			logger.error("Failed to execute query: ", ex); 
            throw ex;			
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		
		return idList;
	}

	public int processResult(ResultSet rs) throws SQLException {
		int count = 0;
    	logger.info(">>> process data result for IdGeneratorTask....");
		while (rs.next()) {
			Integer id = (rs.getInt(1));
			if(id!=null)
				idList.add(id);
		}
		return count;
	}

}

