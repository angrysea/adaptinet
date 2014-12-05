package com.db.ess.synthesis.dao.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.util.SQLUtils;

public abstract class BaseExecuteDaoTask<T> implements DaoTask<T> {

    private static final Logger logger = Logger.getLogger(BaseExecuteDaoTask.class.getName());
		
	protected int location;
	    
	public BaseExecuteDaoTask(int loc) {
	    this.location = loc;
	}
	    
	/**
	 * Create proper statement objects using the given Connection 
	 */
	protected abstract PreparedStatement[] createStatement(Connection c) throws SQLException;

	public void run(T res) throws Exception {
		Connection conn = null;
		PreparedStatement[] stmts = null;
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			stmts = createStatement(conn);
            conn.setAutoCommit(true); // TODO: this should be subclass's responsibility
            for(PreparedStatement s : stmts) {
            	if(s!=null)
            		s.executeUpdate();
            }
			//conn.commit();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			logger.info(">>> Successfully INSERT/UPDATE/DELETE in " + location);
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw ex;			
		} finally {
			SQLUtils.closeResources(conn, stmts, null);
		}
	}

}
