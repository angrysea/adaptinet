package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;

public class UserTask extends BaseQueryDaoTask<Integer> 
{
	private static final Logger logger = Logger.getLogger(UserTask.class.getName());
	private int userId;
	private String userEmail;
	private boolean isUserPresent;
	
	
	public UserTask(int loc) {
		super(loc);
	}
	public UserTask(int loc,String userEmail) {
		super(loc);
		this.userEmail = userEmail;
	}


	public static final String SELECT_USER = 
		"select userId from  ETS_Entitlement..EM_User emu where emu.emailAddress=? ";	
	
	
	@Override
	protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement(SELECT_USER);
		stmt.setString(1, getUserEmail());
    	return stmt;
	}

	@Override
	protected ResultSet runStatement(Statement stmt) throws SQLException {
		return ((PreparedStatement)stmt).executeQuery();
	}

	@Override
	protected int processResult(Integer res, ResultSet rs) throws Exception {
		int count = 0;
    	logger.info(">>> process data result for UserTask....");
		while (rs.next()) {
			setUserId(rs.getInt(1));
			setUserPresent((getUserId()>0));
		}
		return count;
	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public boolean isUserPresent() {
		return isUserPresent;
	}

	public void setUserPresent(boolean isUserPresent) {
		this.isUserPresent = isUserPresent;
	}
	
}

