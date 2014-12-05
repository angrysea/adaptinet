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

public class GetAltDivCcyAuditTask extends BaseQueryDaoTask<GetAltDivCcyAuditResponse> {
	
	private static final Logger logger = Logger.getLogger(GetAltDivCcyTask.class.getName());
	private GetAltDivCcyAuditRequest request;
    
	public static final String SELECT_ALT_DIV_CCYS_AUDIT = 
			"SELECT * FROM SYN_AltDivCcyAudit WHERE divId=?";
	
    public GetAltDivCcyAuditTask(GetAltDivCcyAuditRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
	
	@Override
    protected Statement createStatement(Connection c) throws SQLException {
		
		PreparedStatement stmt = c.prepareStatement(SELECT_ALT_DIV_CCYS_AUDIT);
		stmt.setInt(1, request.getdivId());
    	return stmt;
    }
    
	@Override
    protected ResultSet runStatement(Statement stmt) throws SQLException {
		
    	return ((PreparedStatement)stmt).executeQuery();
    }
    
	@Override
    protected int processResult(GetAltDivCcyAuditResponse res, ResultSet rs) throws SQLException {
    	int count = 0;
    	logger.info(">>> process data result for ALTDivCcys....");
		while (rs.next()) {
            AltDivCcyAudit cAudit = new AltDivCcyAudit();
            cAudit.setunderlyingTicker(rs.getString(1));
            cAudit.setticker(rs.getString(2));
            
            Timestamp exDateTimeStamp = rs.getTimestamp(3);
			Date exDate = exDateTimeStamp == null ? null : new Date(
			exDateTimeStamp.getTime());
			cAudit.setexDate(exDate);
			
			Timestamp recDateTimeStamp = rs.getTimestamp(4);
			Date recDate = recDateTimeStamp == null ? null : new Date(
			recDateTimeStamp.getTime());
			cAudit.setrecordDate(recDate);
			
			Timestamp payDateTimeStamp = rs.getTimestamp(5);
			Date payDate = payDateTimeStamp == null ? null : new Date(
			payDateTimeStamp.getTime());
			cAudit.setpaymentDate(payDate);
            
			cAudit.setdivCcy(rs.getString(6));
			cAudit.setdivCcyGrossRate(rs.getFloat(7));
			cAudit.setdivCcyNetRate(rs.getFloat(8));
			
			cAudit.setaltDivCcy(rs.getString(9));
			cAudit.setaltDivCcyGrossRate(rs.getFloat(10));
			cAudit.setaltDivCcyNetRate(rs.getFloat(11));
			
			cAudit.setuserIdCreate(rs.getString(12));
			Timestamp createDateTimeStamp = rs.getTimestamp(13);
			Date createDate = createDateTimeStamp == null ? null : new Date(
					createDateTimeStamp.getTime());
			cAudit.setchangeCreateTime(createDate);
			
			cAudit.setuserIdApply(rs.getString(14));
			Timestamp applyDateTimeStamp = rs.getTimestamp(15);
			Date applyDate = applyDateTimeStamp == null ? null : new Date(
					applyDateTimeStamp.getTime());
			cAudit.setchangeApplyTime(applyDate);

			res.setaltDivCcyAudits(cAudit);
			count++;
		}
		return count;
    }
}
