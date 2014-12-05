package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;

public class InstrumentTask extends BaseQueryDaoTask<Integer> 
{
	private static final Logger logger = Logger.getLogger(InstrumentTask.class.getName());
	private int instrId;
	private String ticker;
	private boolean isInstrPresent;
	
	
	public InstrumentTask(int loc) {
		super(loc);
	}
	public InstrumentTask(int loc,String ticker) {
		super(loc);
		this.ticker = ticker;
	}


	public static final String SELECT_TICKER = 
		"select fiId from Instrument where ticker=? ";	
	
	
	@Override
	protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement(SELECT_TICKER);
		stmt.setString(1, getTicker());
    	return stmt;
	}

	@Override
	protected ResultSet runStatement(Statement stmt) throws SQLException {
		return ((PreparedStatement)stmt).executeQuery();
	}

	@Override
	protected int processResult(Integer res, ResultSet rs) throws Exception {
		int count = 0;
    	logger.info(">>> process data result for InstrumentTask....");
		while (rs.next()) {
			setInstrId(rs.getInt(1));
			setInstrPresent((getInstrId()>0));
		}
		return count;
	}
	public int getInstrId() {
		return instrId;
	}
	public void setInstrId(int instrId) {
		this.instrId = instrId;
	}
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public boolean isInstrPresent() {
		return isInstrPresent;
	}
	public void setInstrPresent(boolean isInstrPresent) {
		this.isInstrPresent = isInstrPresent;
	}

}
