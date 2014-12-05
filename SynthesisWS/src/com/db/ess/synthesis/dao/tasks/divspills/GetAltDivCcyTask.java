package com.db.ess.synthesis.dao.tasks.divspills;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dvo.AltDivCcy;
import com.db.ess.synthesis.dvo.GetAltDivCcyRequest;
import com.db.ess.synthesis.dvo.GetAltDivCcyResponse;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class GetAltDivCcyTask extends BaseQueryDaoTask<GetAltDivCcyResponse> {
	
	private static final Logger logger = Logger.getLogger(GetAltDivCcyTask.class.getName());
	private GetAltDivCcyRequest request;
    
	public static final String SELECT_ALT_DIV_CCYS ="{ call dbo.SYN_GetAltDivCcy( @DivId=?)}"; 
    public GetAltDivCcyTask(GetAltDivCcyRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
	
	@Override
    protected Statement createStatement(Connection c) throws SQLException {
    	
    	CallableStatement cstmt = c.prepareCall(SELECT_ALT_DIV_CCYS);
    	cstmt.setInt(1, request.getdividendId());
    	
    	return cstmt;
    }
    
	@Override
    protected ResultSet runStatement(Statement stmt) throws SQLException {
		logger.info(">>> running " +
		String.format(
		    "SELECT * FROM DivOptionalPayCcy WHERE dividendId=%s)", 
		    request.getdividendId()
		));
    	return ((PreparedStatement)stmt).executeQuery();
    }
    
	@Override
    protected int processResult(GetAltDivCcyResponse res, ResultSet rs) throws SQLException {
    	int count = 0;
    	logger.info(">>> process data result for ALTDivCcys....");
		while (rs.next()) {
            AltDivCcy c = new AltDivCcy();
            c.setid(rs.getInt(1));
            c.setdivId(rs.getInt(2));
            int ccyId = rs.getInt(3);
            CachedCurrency ccy = RefCache.getCurrency(ccyId, location);
            c.setccyId(ccyId);
            c.setccyCode(ccy.getSwiftCode());
            c.setnetRate(rs.getFloat(4));
			c.setgrossRate(rs.getFloat(5));
			c.setcomment(rs.getString(6));
			res.setaltDivCcys(c);
			count++;
		}
		return count;
    }
}
