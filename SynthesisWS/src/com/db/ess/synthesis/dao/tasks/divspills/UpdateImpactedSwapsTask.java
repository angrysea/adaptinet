package com.db.ess.synthesis.dao.tasks.divspills;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.ImpactedSwapDivCcy;
import com.db.ess.synthesis.dvo.UpdateImpactedSwapDivCcyRequest;
import com.db.ess.synthesis.dvo.UpdateImpactedSwapDivCcyResponse;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class UpdateImpactedSwapsTask extends BaseExecuteDaoTask<UpdateImpactedSwapDivCcyResponse> {
	
	private static final Logger logger = Logger.getLogger(UpdateImpactedSwapsTask.class.getName());
	private UpdateImpactedSwapDivCcyRequest request;
    
	public static final String UPDATE_CCY_EXCEPTION =
	    "UPDATE DivPayCcyException SET payCcyId = ? WHERE swapId = ? AND dividendId = ?";
	public static final String INSERT_IMPCT_SWAP_AUD =
			"INSERT INTO SYN_ImpactedSwapAudit (swapNum,divId,book,institution,legalEntity," +
			"ticker,preChngDivCcy,preChngGrossRate,preChngNetRate,currDivCcy,currDivGrossRate," +
			"currDivNetRate,userId,applyTime) " +
			" values (?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, getDate())";
    public UpdateImpactedSwapsTask(UpdateImpactedSwapDivCcyRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
    
    @Override
    protected PreparedStatement[] createStatement(Connection c) throws SQLException {
    	
    	List<PreparedStatement> list = new ArrayList<PreparedStatement>();
    	
    	Iterator iter = request.getimpactedSwapDivCcysIterator();
    	while(iter.hasNext()) {
    		ImpactedSwapDivCcy s = (ImpactedSwapDivCcy)iter.next();
    		PreparedStatement stmt = c.prepareStatement(UPDATE_CCY_EXCEPTION);
    		PreparedStatement audStmt = c.prepareStatement(INSERT_IMPCT_SWAP_AUD);
    		
    		CachedCurrency cur = RefCache.getCurrency(s.getpayCcy(), request.getlocation());
    		stmt.setInt(1, cur.getCcyId());
    		stmt.setInt(2, s.getswapId());
    		stmt.setInt(3, s.getdivId());
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
