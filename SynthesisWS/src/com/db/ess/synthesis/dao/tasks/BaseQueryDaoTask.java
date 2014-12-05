package com.db.ess.synthesis.dao.tasks;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.util.SQLUtils;

public abstract class BaseQueryDaoTask<T> implements DaoTask<T> {
	
    private static final Logger logger = Logger.getLogger(BaseQueryDaoTask.class.getName());
	
    protected int location;
    
    public BaseQueryDaoTask(int loc) {
        this.location = loc;
    }
    
    /**
     * Create a proper statement object using the given Connection 
     */
    protected abstract Statement createStatement(Connection c) throws SQLException;
    
    /**
     * Run the statement. Depending on the type, call executeQuery or executeUpdate. 
     */
    protected abstract ResultSet runStatement(Statement stmt) throws SQLException;
    
    /**
     * Do the following:
     *     - loop through the result set
     *     - create a target data object 
     *     - add the object to the response 
     *     - return the number of data objects 
     *     
     * In case of update/insert/delete, do nothing. Just return an arbitrary integer.
     */
    protected abstract int processResult(T res, ResultSet rs) throws Exception;
    
    public void run(T res) throws Exception {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			stmt = createStatement(conn);
			stmt.setQueryTimeout(300);
			rs = runStatement(stmt);
			
			
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			int count = processResult(res, rs);
		    logger.info("Found ["+count+"] rows in " + location);
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
            throw ex;			
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
    }
}
