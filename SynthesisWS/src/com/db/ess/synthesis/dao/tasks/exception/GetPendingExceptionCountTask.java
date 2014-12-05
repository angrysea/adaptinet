package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.GetPendingExceptionCountRequest;
import com.db.ess.synthesis.dvo.GetPendingExceptionCountResponse;
import com.db.ess.synthesis.dvo.PendingExceptionAlert;
import com.db.ess.synthesis.processor.tasks.NonDaoTask;
import com.db.ess.synthesis.util.SQLUtils;

public class GetPendingExceptionCountTask implements NonDaoTask<GetPendingExceptionCountResponse>
{
	private static final Logger logger = Logger.getLogger(GetPendingExceptionCountTask.class.getName());
	public static final String GET_PENDING_COUNT = 
		"select sum(agg.pendingCount) from (select count(1) pendingCount from SpreadExceptionPending sep "
										+	" join ApproverBookSeries a on a.bookSeriesId = sep.bookSeriesId and a.userId=? "
										+	" where sep.status=0 and sep.spread in (0,1) "
										+   " union all " 
										+	" select count(1) pendingCount from DividendExceptionPending dep "
										+ 	" join ApproverBookSeries a on a.bookSeriesId = dep.bookSeriesId and a.userId=? "
										+	" where dep.status=0) agg ";
	GetPendingExceptionCountRequest request;
	PendingExceptionAlert exceptionCountAlert;
	int ldnUserId = 0;
	int nyUserId = 0;
	int pendingCntLdn = 0;
	int pendingCntNY = 0;

	public GetPendingExceptionCountTask(GetPendingExceptionCountRequest req) {
		this.request = req;
	}	
	
	protected PreparedStatement createStatement(Connection c, int qryLocation) throws SQLException 
	{
		PreparedStatement cstmt = null;
		
		String query = GET_PENDING_COUNT;
		cstmt = c.prepareStatement(query);
		setParameters(cstmt,qryLocation);
		
		return  cstmt;
	}
	
	public void run(GetPendingExceptionCountResponse res) throws Exception {
		logger.info("Ellapsed time: inside run method"); 
		int dbLocation = 0;
		
		ldnUserId = request.getuserIdLdn();
		nyUserId = request.getuserIdNY();
		if(ldnUserId > 0 && nyUserId <= 0)
			dbLocation = 1;
		else if(ldnUserId <= 0 && nyUserId > 0)
			dbLocation = 2;
		else
			dbLocation = 3;
		
		if(dbLocation == 1 || dbLocation == 2)
			executeQuery(dbLocation);
		else
		{
			executeQuery(1);
			executeQuery(2);
		}
		
		processResult(res);
		
	}
	
	public void executeQuery (int locn) throws Exception 
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		long time = System.currentTimeMillis(); 
		try {
				conn = SQLUtils.getConnection(locn);
				stmt = createStatement(conn,locn);
				logger.info("Executing Statement : "+stmt+" for location : "+locn);
				rs = stmt.executeQuery();
					
					while(rs.next())
					{
						if(locn==1)
							setPendingCntLdn(rs.getInt(1));
						else
							setPendingCntNY(rs.getInt(1));
					}

				logger.info("Ellapsed time: " + (System.currentTimeMillis() - time));
		
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			conn.rollback();		
		} finally {
			SQLUtils.closeResources(conn, stmt, null);
		}
	}
	
	public void setParameters(PreparedStatement cstmt, int queryLocation) throws SQLException
	{
		//int i =0;
		if(queryLocation == 1)
		{
			cstmt.setInt(1, ldnUserId);
			cstmt.setInt(2, ldnUserId);
		}
		else if(queryLocation == 2)
		{
			cstmt.setInt(1, nyUserId);
			cstmt.setInt(2, nyUserId);
		}
	}
	
	public void processResult(GetPendingExceptionCountResponse response)
	{
		exceptionCountAlert = new PendingExceptionAlert();
		
		exceptionCountAlert.setPendingLndCount(getPendingCntLdn());
		exceptionCountAlert.setPendingNYCount(getPendingCntNY());
		exceptionCountAlert.setTotalPendingCount(getPendingCntLdn() + getPendingCntNY());
		
		response.setpendingExceptionAlert(exceptionCountAlert);
		logger.info("Pending London Count :: "+getPendingCntLdn()+" And Pending NY Count :: "+getPendingCntNY());
	}
	
	public int getPendingCntLdn() {
		return pendingCntLdn;
	}

	public void setPendingCntLdn(int pendingCntLdn) {
		this.pendingCntLdn = pendingCntLdn;
	}

	public int getPendingCntNY() {
		return pendingCntNY;
	}

	public void setPendingCntNY(int pendingCntNY) {
		this.pendingCntNY = pendingCntNY;
	}
	
}
