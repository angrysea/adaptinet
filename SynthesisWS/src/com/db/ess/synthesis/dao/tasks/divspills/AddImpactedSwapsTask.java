package com.db.ess.synthesis.dao.tasks.divspills;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.AddImpactedSwapDivCcyRequest;
import com.db.ess.synthesis.dvo.AddImpactedSwapDivCcyResponse;
import com.db.ess.synthesis.dvo.ImpactedSwapDivCcy;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class AddImpactedSwapsTask extends BaseExecuteDaoTask<AddImpactedSwapDivCcyResponse> {
	
	private static final Logger logger = Logger.getLogger(AddImpactedSwapsTask.class.getName());
	private AddImpactedSwapDivCcyRequest request;
    
	public static final String INSERT_CCY_EXCEPTION =
		"INSERT INTO DivPayCcyException (swapId,dividendId,payCcyId) values (?, ?, ?)";
	public static final String INSERT_IMPCT_SWAP_AUD =
			"INSERT INTO SYN_ImpactedSwapAudit (swapNum,divId,book,institution,legalEntity," +
			"ticker,preChngDivCcy,preChngGrossRate,preChngNetRate,currDivCcy,currDivGrossRate," +
			"currDivNetRate,userId,applyTime) " +
			" values (?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, getDate())";
    public AddImpactedSwapsTask(AddImpactedSwapDivCcyRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
    
    @Override
    protected PreparedStatement[] createStatement(Connection c) throws SQLException {
    	
    	List<PreparedStatement> list = new ArrayList<PreparedStatement>();
    	
    	Iterator iter = request.getimpactedSwapDivCcysIterator();
    	while(iter.hasNext()) {
    		ImpactedSwapDivCcy s = (ImpactedSwapDivCcy)iter.next();
    		PreparedStatement stmt = c.prepareStatement(INSERT_CCY_EXCEPTION);
    		PreparedStatement audStmt = c.prepareStatement(INSERT_IMPCT_SWAP_AUD);
    		stmt.setInt(1, s.getswapId());
    		stmt.setInt(2, s.getdivId());
    		CachedCurrency cur = RefCache.getCurrency(s.getpayCcy(), request.getlocation());
    		stmt.setInt(3, cur.getCcyId());
    		// set up params for audit table
    		audStmt.setInt(1,s.getswapNumber());
    		audStmt.setInt(2,s.getdivId());
    		audStmt.setString(3, s.getbook());
    		audStmt.setString(4, s.getinstitution());
    		audStmt.setString(5, s.getlegalEntity());
    		audStmt.setString(6, s.getticker());
    		audStmt.setString(7, s.getpreChngDivCcy());
    		audStmt.setDouble(8, s.getpreChngGrsDivRate());
    		audStmt.setDouble(9, s.getpreChngNetDivRate());
    		audStmt.setString(10, s.getcurrDivCcy());
    		audStmt.setDouble(11, s.getcurrGrsDivRate());
    		audStmt.setDouble(12, s.getcurrNetDivRate());
    		audStmt.setString(13, s.getuserName());
    		
    		list.add(stmt);
    		list.add(audStmt);
    	}
        return (PreparedStatement[])list.toArray(new PreparedStatement[list.size()]);
    }
}
