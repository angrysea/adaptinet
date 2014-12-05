package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.util.SQLUtils;

public class TaskHelper {
	
	private static final Logger logger = Logger.getLogger(TaskHelper.class.getName());
    private static final String SELECT_COUNT =	
        " SELECT count(*) " + 
        "   FROM DividendExceptionPending d, ApproverBookSeries a " +
        "  WHERE d.bookSeriesId = a.bookSeriesId " +
        "    AND a.userId = ? "; 
    
    public static int getPendingCount(int userId, int location) throws Exception{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(SELECT_COUNT);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if(rs.next()){
				return rs.getInt(1);
			}
		} catch (Exception ex) {
	        logger.error("Failed to execute query: ", ex); 
		    throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		return 0;
	}
}
