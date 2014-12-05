package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.util.SQLUtils;

public class InstitutionLegalEntCheckTask
{
	private static final Logger logger = Logger.getLogger(InstitutionLegalEntCheckTask.class.getName());
	private int institutionId;
	private int legalEntityId;
	private int location;

	private boolean legalEntValid = false;

	public InstitutionLegalEntCheckTask(int loc, int institutionId, int legalEntityId) {
		this.location = loc;
		this.institutionId = institutionId;
		this.legalEntityId = legalEntityId;
	}


	public static final String SELECT_LEGALENTITY_VALID = 
		" select 1 from LegalEntity le where le.legalEntityId=? and le.institutionId=? ";
	
	
	protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement(SELECT_LEGALENTITY_VALID);
		stmt.setInt(1, legalEntityId);
		stmt.setInt(2, institutionId);
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
		
		return getLegalEntityIsValid();
	}

	protected int processResult(ResultSet rs) throws SQLException {
		int count = 0;
    	logger.info(">>> process data result for InstitutionLegalEntCheckTask....");
    	
		while (rs.next()) {
			setLegalEntityIsValid(true);
			count++;
		}
		
		return count;
	}
	
	public boolean getLegalEntityIsValid() {
		return legalEntValid;
	}
	public void setLegalEntityIsValid(boolean legalEntValid) {
		this.legalEntValid = legalEntValid;
	}

}

