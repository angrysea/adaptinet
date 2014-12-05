package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;

public class ApproverBookSeriesTask extends BaseQueryDaoTask<Integer> 
{
	private static final Logger logger = Logger.getLogger(ApproverBookSeriesTask.class.getName());
	private String bookSeries;
	private String userEmailAddress;
	private boolean userHasBookAccess;


	public ApproverBookSeriesTask(int loc) {
		super(loc);
	}
	public ApproverBookSeriesTask(int loc,String bookSeries,String userEmailAddress) {
		super(loc);
		this.bookSeries = bookSeries;
		this.userEmailAddress = userEmailAddress;
	}


	public static final String SELECT_BOOK_APPROVER_ACCESS = 
		" select 1 from ApproverBookSeries abs,BookSeriesDomain bsd, ETS_Entitlement..EM_User emu " +
		" where bsd.description=? and bsd.bookSeriesId=abs.bookSeriesId and abs.userId = emu.userId " +
		"and emu.emailAddress=? ";
	
	
	@Override
	protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement(SELECT_BOOK_APPROVER_ACCESS);
		stmt.setString(1, getBookSeries());
		stmt.setString(2, getUserEmailAddress());
    	return stmt;
	}

	
	@Override
	protected ResultSet runStatement(Statement stmt) throws SQLException {
		return ((PreparedStatement)stmt).executeQuery();
	}

	@Override
	protected int processResult(Integer res, ResultSet rs) throws Exception {
		int count = 0;
    	logger.info(">>> process data result for ApproverBookSeriesTask....");
		while (rs.next()) {
			setUserHasBookAccess((rs.getInt(1)>0));
		}
		return count;
	}
	
	public String getBookSeries() {
		return bookSeries;
	}
	public void setBookSeries(String bookSeries) {
		this.bookSeries = bookSeries;
	}
	public String getUserEmailAddress() {
		return userEmailAddress;
	}
	public void setUserEmailAddress(String userEmailAddress) {
		this.userEmailAddress = userEmailAddress;
	}
	public boolean getUserHasBookAccess() {
		return userHasBookAccess;
	}
	public void setUserHasBookAccess(boolean userHasBookAccess) {
		this.userHasBookAccess = userHasBookAccess;
	}

}

