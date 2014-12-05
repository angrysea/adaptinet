package com.db.ess.synthesis.dao.tasks.divspills;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dvo.AltDivCcyAudit;
import com.db.ess.synthesis.dvo.GetAltDivCcyAuditRequest;
import com.db.ess.synthesis.dvo.GetAltDivCcyAuditResponse;
import com.db.ess.synthesis.dvo.GetImpactedSwapAuditRequest;
import com.db.ess.synthesis.dvo.GetImpactedSwapAuditResponse;
import com.db.ess.synthesis.dvo.ImpactedSwapAudit;

public class GetImpactedSwapAuditTask extends BaseQueryDaoTask<GetImpactedSwapAuditResponse> {
	
	private static final Logger logger = Logger.getLogger(GetAltDivCcyTask.class.getName());
	private GetImpactedSwapAuditRequest request;
    
	public static final String SELECT_IMPCT_SWP_CCYS_AUDIT = 
		"SELECT swapNum,divId,book,institution,legalEntity," +
			"ticker,preChngDivCcy,preChngGrossRate,preChngNetRate,currDivCcy,currDivGrossRate," +
			"currDivNetRate,userId,applyTime " +
			" FROM SYN_ImpactedSwapAudit WHERE divId=?";
	
    public GetImpactedSwapAuditTask(GetImpactedSwapAuditRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
	
	@Override
    protected Statement createStatement(Connection c) throws SQLException {
    	PreparedStatement stmt = c.prepareStatement(SELECT_IMPCT_SWP_CCYS_AUDIT);
    	stmt.setInt(1, request.getdivId());
    	return stmt;
    }
    
	@Override
    protected ResultSet runStatement(Statement stmt) throws SQLException {
		
    	return ((PreparedStatement)stmt).executeQuery();
    }
    
	@Override
    protected int processResult(GetImpactedSwapAuditResponse res, ResultSet rs) throws SQLException {
    	int count = 0;
    	logger.info(">>> process data result for ImpactedSwapAudits....");
		while (rs.next()) {
			ImpactedSwapAudit impAudit = new ImpactedSwapAudit();
			impAudit.setswapNumber(rs.getInt(1));
			impAudit.setdivId(rs.getInt(2));
			impAudit.setbook(rs.getString(3));
			impAudit.setinstitution(rs.getString(4));
			impAudit.setlegalEntity(rs.getString(5));
			impAudit.setticker(rs.getString(6));
			impAudit.setpreChngDivCcy(rs.getString(7));
			impAudit.setpreChngGrsDivRate(rs.getFloat(8));
			impAudit.setpreChngNetDivRate(rs.getFloat(9));
			impAudit.setcurrDivCcy(rs.getString(10));
			impAudit.setcurrGrsDivRate(rs.getFloat(11));
			impAudit.setcurrNetDivRate(rs.getFloat(12));
			impAudit.setuserName(rs.getString(13));
			
			Timestamp applyDateTimeStamp = rs.getTimestamp(14);
			Date applyDate = applyDateTimeStamp == null ? null : new Date(
					applyDateTimeStamp.getTime());
			impAudit.setchangeApplyTime(applyDate);

			res.setimpactedSwap(impAudit);
			count++;
		}
		return count;
    }
}

