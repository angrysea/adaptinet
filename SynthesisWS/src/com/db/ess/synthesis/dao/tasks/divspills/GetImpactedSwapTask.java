package com.db.ess.synthesis.dao.tasks.divspills;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dvo.GetImpactedSwapRequest;
import com.db.ess.synthesis.dvo.GetImpactedSwapResponse;
import com.db.ess.synthesis.dvo.ImpactedSwap;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class GetImpactedSwapTask extends BaseQueryDaoTask<GetImpactedSwapResponse> {

	private static final Logger logger = Logger.getLogger(GetImpactedSwapTask.class.getName());
	private GetImpactedSwapRequest request;
    
	public static final String GET_IMPACTED_SWAPS="{ call dbo.SYN_GetImpactedSwaps (@DivId=?) }";
	
	
    public GetImpactedSwapTask(GetImpactedSwapRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
	
	@Override
    protected Statement createStatement(Connection c) throws SQLException {
    	CallableStatement stmt = c.prepareCall(GET_IMPACTED_SWAPS);
    	stmt.setInt(1, request.getdivId());
    	return stmt;
    }
    
	@Override
    protected ResultSet runStatement(Statement stmt) throws SQLException {
		logger.info(">>> running " +
		String.format(
		    "{ call dbo.SYN_GetImpactedSwaps (@DivId=%s) }", 
			request.getdivId()
		));
    	return ((CallableStatement)stmt).executeQuery();
    }
    
	@Override
    protected int processResult(GetImpactedSwapResponse res, ResultSet rs) throws SQLException {
    	int count = 0;
    	logger.info(">>> process data result for ImpactedSwaps....");
		while (rs.next()) {
            ImpactedSwap c = new ImpactedSwap();
            c.setinstitution(rs.getString(1));
            c.setbook(rs.getString(2));
            c.setlegalEntity(rs.getString(3));
            c.setswapNumber(rs.getInt(4));
            c.setdivCcy(rs.getString(5));
            c.setcashFlow(rs.getFloat(6));
            c.setquantity(rs.getFloat(7)); 
            c.setticker(rs.getString(8));
            c.setaltDivCcy(rs.getString(9));
            c.setdivId(rs.getInt(10));
            c.setswapId(rs.getInt(11));
            c.setlocation(request.getlocation());
            res.setimpactedSwap(c);
			count++;
		}
		return count;
    }

}
