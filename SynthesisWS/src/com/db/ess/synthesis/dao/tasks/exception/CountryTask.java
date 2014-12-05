package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;

public class CountryTask  extends BaseQueryDaoTask<Integer> 
{
	private static final Logger logger = Logger.getLogger(CountryTask.class.getName());
	private int countryId;
	private String countryCode;
	private boolean isCountryPresent = false;
	
	public CountryTask(int loc) {
		super(loc);
	}
	public CountryTask(int loc,String ccCode) {
		super(loc);
		this.countryCode = ccCode;
	}

	public static final String SELECT_COUNTRY = 
		"select countryId from Country where code=? ";	
	
	
	@Override
	protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement(SELECT_COUNTRY);
		stmt.setString(1, getCountryCode());
    	return stmt;
	}

	@Override
	protected ResultSet runStatement(Statement stmt) throws SQLException {
		return ((PreparedStatement)stmt).executeQuery();
	}

	@Override
	protected int processResult(Integer res, ResultSet rs) throws Exception {
		int count = 0;
    	logger.info(">>> process data result for CountryTask....");
		while (rs.next()) {
			setCountryId(rs.getInt(1));
			setCountryPresent(true);
		}
		return count;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public boolean isCountryPresent() {
		return isCountryPresent;
	}
	public void setCountryPresent(boolean isCountryPresent) {
		this.isCountryPresent = isCountryPresent;
	}
}


