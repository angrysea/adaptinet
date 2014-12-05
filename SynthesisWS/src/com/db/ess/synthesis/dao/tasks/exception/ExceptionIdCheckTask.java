package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dao.tasks.divspills.GetAltDivCcyTask;
import com.db.ess.synthesis.dvo.GetAltDivCcyAuditRequest;
/**
 * This task validates only for Pending exception Id's ,down the line we might check for approved as well
 * hence the name of the file is simply Id check task.
 * @author kumarhem
 *
 */
public class ExceptionIdCheckTask extends BaseQueryDaoTask<Integer> 
{
	private static final Logger logger = Logger.getLogger(ExceptionIdCheckTask.class.getName());
	private int exceptId;
	private String type;
	private String tableName;
	private boolean exceptIdPresent = false;

	public ExceptionIdCheckTask(int loc) {
		super(loc);
	}
	public ExceptionIdCheckTask(int loc,int exceptId) {
		super(loc);
		this.exceptId = exceptId;
	}
	public ExceptionIdCheckTask(int loc,int exceptId,String type) {
		super(loc);
		this.exceptId = exceptId;
		this.type = type;
	}
	
	public boolean isExceptIdPresent() {
		return exceptIdPresent;
	}
	public void setExceptIdPresent(boolean exceptIdPresent) {
		this.exceptIdPresent = exceptIdPresent;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getExceptId() {
		return exceptId;
	}
	public void setExceptId(int exceptId) {
		this.exceptId = exceptId;
	}

	public static final String SELECT_SPREAD_EXCEPTID = 
		"select 1,'SpreadExceptionPending' from SpreadExceptionPending where exceptId=? ";
	
	public static final String SELECT_DIVIDEND_EXCEPTID = 
			"select 1,'DividendExceptionPending' from DividendExceptionPending where exceptId=?";
	
	
	@Override
	protected Statement createStatement(Connection c) throws SQLException {
		logger.info(">>> type of the Exception..."+type);
		String query = type!=null?type.contains("ivi")?SELECT_DIVIDEND_EXCEPTID:SELECT_SPREAD_EXCEPTID:SELECT_SPREAD_EXCEPTID;
		PreparedStatement stmt = c.prepareStatement(query);
		stmt.setInt(1, getExceptId());
    	return stmt;
	}

	@Override
	protected ResultSet runStatement(Statement stmt) throws SQLException {
		return ((PreparedStatement)stmt).executeQuery();
	}

	@Override
	protected int processResult(Integer res, ResultSet rs) throws Exception {
		int count = 0;
    	logger.info(">>> process data result for ExceptionIdCheckTask....");
		while (rs.next()) {
			setExceptIdPresent((rs.getInt(1)>0));
			setTableName(rs.getString(2));
		}
		return count;
	}

}
