package com.db.ess.synthesis.dao.tasks.adjustment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;


public class EventIdCheckTask extends BaseQueryDaoTask<Integer> 
{
	private static final Logger logger = Logger.getLogger(EventIdCheckTask.class.getName());
	private int eventId;
	private boolean eventIdPresent = false;

	public EventIdCheckTask(int loc) {
		super(loc);
	}
	public EventIdCheckTask(int loc,int eventId) {
		super(loc);
		this.eventId = eventId;
	}
	
	public boolean isEventIdPresent() {
		return eventIdPresent;
	}
	public void setEventIdPresent(boolean eventIdPresent) {
		this.eventIdPresent = eventIdPresent;
	}
	
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public static final String SELECT_EVENTID = 
		"select 1 from BasketEvent where eventId=? " ;
	
	
	@Override
	protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement(SELECT_EVENTID);
		stmt.setInt(1, getEventId());
    	return stmt;
	}

	@Override
	protected ResultSet runStatement(Statement stmt) throws SQLException {
		return ((PreparedStatement)stmt).executeQuery();
	}

	@Override
	protected int processResult(Integer res, ResultSet rs) throws Exception {
		int count = 0;
    	logger.info(">>> process data result for EventIdCheckTask....");
		while (rs.next()) {
			setEventIdPresent((rs.getInt(1)>0));
		}
		return count;
	}

}
