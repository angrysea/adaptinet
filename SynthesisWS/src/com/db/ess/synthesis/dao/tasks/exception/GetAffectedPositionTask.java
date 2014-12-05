package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dvo.AffectedPosition;
import com.db.ess.synthesis.dvo.GetAffectedPositionRequest;
import com.db.ess.synthesis.dvo.GetAffectedPositionResponse;
import com.db.ess.synthesis.util.cache.CachedBook;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class GetAffectedPositionTask extends BaseQueryDaoTask<GetAffectedPositionResponse> {
	
	private static final Logger logger = Logger.getLogger(GetAffectedPositionTask.class.getName());
	private GetAffectedPositionRequest request;
    
	public static final String SELECT_AFFECTED_POSITIONS ="{ call dbo.SYN_GetAffectedPositions( " +
			" @UserId=?,  @ExceptionId=?,  @Date=? )}"; 
    
	public GetAffectedPositionTask(GetAffectedPositionRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
	
	@Override
    protected Statement createStatement(Connection c) throws SQLException {
		
    	CallableStatement cstmt = c.prepareCall(SELECT_AFFECTED_POSITIONS);
    	cstmt.setInt(1, request.getuserId());
    	cstmt.setInt(2, request.getexceptionId());
    	cstmt.setString(3, request.getdate());
    	return cstmt;
    }
    
	@Override
    protected ResultSet runStatement(Statement stmt) throws SQLException {
		logger.info(">>> running " +
		String.format(
		    "call dbo.SYN_GetAffectedPositions( " +
			" @UserId=%d,  @ExceptionId=%d,  @Date=%s " 
			,  request.getuserId()
	    	,  request.getexceptionId()
	    	,  request.getdate()
		));
    	return ((PreparedStatement)stmt).executeQuery();
    }
    
	@Override
    protected int processResult(GetAffectedPositionResponse res, ResultSet rs) throws SQLException {
    	int count = 0;
    	logger.info(">>> process data result for AffectedPositions....");
		while (rs.next()) {
			int i = 1;
            AffectedPosition ap = new AffectedPosition();
            ap.setswapNumber(rs.getInt(i++));
            
            CachedBook book = RefCache.getBook(rs.getInt(i++), location);
            ap.setbookName(book != null ? book.getName() : "");
            ap.setinstitutionName(rs.getString(i++));
            ap.setcustomerName(rs.getString(i++));
            ap.setticker(rs.getString(i++));
            ap.setinstrDescription(rs.getString(i++));
            
            CachedCurrency instrCcy = RefCache.getCurrency(rs.getInt(i++), location);
            CachedCurrency payCcy = RefCache.getCurrency(rs.getInt(i++), location);
            
            ap.setinstrCcy(instrCcy != null ? instrCcy.getSwiftCode() : "");
            ap.setpayCcy(payCcy != null ? payCcy.getSwiftCode() : "");
            
            ap.setcountryCode(RefCache.getCountry(rs.getInt(i++), location));
            ap.setqty(rs.getFloat(i++));
            ap.setmarketPrice(rs.getFloat(i++));
            ap.setavgCostBase(rs.getFloat(i++));
            ap.setavgCostPay(rs.getFloat(i++));
            ap.setfxRate(rs.getFloat(i++));
            ap.setisin(rs.getString(i++));
            ap.setcusip(rs.getString(i++));
            ap.setstrategy(rs.getString(i++));
            res.setaffectedPositions(ap);            
			count++;
		}
		return count;
    }
	
	private Date convert(Timestamp t) {
		return (t == null ? null : new Date(t.getTime()));
	}
}
