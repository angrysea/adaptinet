package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dao.tasks.divspills.GetAltDivCcyTask;

public class SwapNumCheckTask extends BaseQueryDaoTask<Integer> 
{
	private static final Logger logger = Logger.getLogger(SwapNumCheckTask.class.getName());
	private int swapNum;
	private int swapId;
	
	public int getSwapId() {
		return swapId;
	}
	public void setSwapId(int swapId) {
		this.swapId = swapId;
	}
	public int getSwapNum() {
		return swapNum;
	}
	public void setSwapNum(int swapNum) {
		this.swapNum = swapNum;
	}
	public boolean isSwapNumPresent() {
		return swapNumPresent;
	}
	public void setSwapNumPresent(boolean swapNumPresent) {
		this.swapNumPresent = swapNumPresent;
	}


	private boolean swapNumPresent = false;

	public SwapNumCheckTask(int loc) {
		super(loc);
	}
	public SwapNumCheckTask(int loc,int swapNum) {
		super(loc);
		this.swapNum = swapNum;
	}


	public static final String SELECT_SWAP_NUM = 
		"select fiId from Swap  where swapNum=? ";	
	
	
	@Override
	protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement(SELECT_SWAP_NUM);
		stmt.setInt(1, getSwapNum());
    	return stmt;
	}

	@Override
	protected ResultSet runStatement(Statement stmt) throws SQLException {
		return ((PreparedStatement)stmt).executeQuery();
	}

	@Override
	protected int processResult(Integer res, ResultSet rs) throws Exception {
		int count = 0;
    	logger.info(">>> process data result for SwapNumCheckTask....");
		while (rs.next()) {
			setSwapId(rs.getInt(1));
			setSwapNumPresent(getSwapId()>0);
		}
		return count;
	}

}
