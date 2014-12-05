package com.db.ess.synthesis.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.util.dao.ConnectionHelper;

public class SQLUtils {

	private static final Logger logger = Logger.getLogger(SQLUtils.class
			.getName());
	public static int countTryNumber = 2;

	public static Connection getConnection() {
		Connection conn = null;
		int connCount = 1;

		while (connCount <= countTryNumber) {
			try {
				conn = ConnectionHelper.getConnection();
				break;
			} catch (Exception dare) {
				if (connCount != countTryNumber)
					logger.info(" DS LookUp error.. retry to connect in "
							+ connCount + "/" + countTryNumber + " attempts");
				else
					logger.error(" DS LookUp error.. failed to connect in "
							+ connCount + "/" + countTryNumber + " attempts",
							dare);
			}
			connCount++;
		}
		return conn;
	}

	public static Connection getConnection(int location) {
		Connection conn = null;
		int connCount = 1;

		while (connCount <= countTryNumber) {
			try {
				conn = ConnectionHelper.getConnection(location);
				break;
			} catch (Exception dare) {
				if (connCount != countTryNumber)
					logger.info(" DS LookUp error.. retry to connect in "
							+ connCount + "/" + countTryNumber + " attempts");
				else
					logger.error(" DS LookUp error.. failed to connect in "
							+ connCount + "/" + countTryNumber + " attempts",
							dare);
			}
			connCount++;
		}
		return conn;
	}

	public static final void closeResources(Connection conn, Statement stmt,
			ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			logger.error("Closing JDBC ResultSet", e);
		}

		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			logger.error("Closing JDBC Statement", e);
		}

		ConnectionHelper.releaseConnection(conn);
	}
	
	public static final void closeResources(Connection conn, Statement[] stmts,
			ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			logger.error("Closing JDBC ResultSet", e);
		}

		for(Statement s : stmts) {
			try {
				s.close();
			} catch (Exception e) {
				logger.error("Closing JDBC Statement", e);
			}
		}

		ConnectionHelper.releaseConnection(conn);
	}
}
