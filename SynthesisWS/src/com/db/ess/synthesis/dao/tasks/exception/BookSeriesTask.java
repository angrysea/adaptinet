package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;

public class BookSeriesTask extends BaseQueryDaoTask<Integer> 
{
	private static final Logger logger = Logger.getLogger(BookSeriesTask.class.getName());
	private String bookSeries;
	private int bookSeriesId;

	private boolean bookIsValid;


	public BookSeriesTask(int loc) {
		super(loc);
	}
	public BookSeriesTask(int loc,String bookSeries) {
		super(loc);
		this.bookSeries = bookSeries;
	}


	public static final String SELECT_BOOK_SERIES_VALID = 
		" select bookSeriesId from BookSeriesDomain bsd where bsd.description=? ";
	
	
	@Override
	protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement(SELECT_BOOK_SERIES_VALID);
		stmt.setString(1, getBookSeries());
    	return stmt;
	}

	
	@Override
	protected ResultSet runStatement(Statement stmt) throws SQLException {
		return ((PreparedStatement)stmt).executeQuery();
	}

	@Override
	protected int processResult(Integer res, ResultSet rs) throws Exception {
		int count = 0;
    	logger.info(">>> process data result for BookSeriesTask....");
		while (rs.next()) {
			setBookSeriesId(rs.getInt(1));
			setBookIsValid(getBookSeriesId()>0);
		}
		return count;
	}
	
	public String getBookSeries() {
		return bookSeries;
	}
	public void setBookSeries(String bookSeries) {
		this.bookSeries = bookSeries;
	}
	public boolean getBookIsValid() {
		return bookIsValid;
	}
	public void setBookIsValid(boolean bookIsValid) {
		this.bookIsValid = bookIsValid;
	}
	public int getBookSeriesId() {
		return bookSeriesId;
	}
	public void setBookSeriesId(int bookSeriesId) {
		this.bookSeriesId = bookSeriesId;
	}


}

