package com.db.ess.synthesis.dao.tasks.divspills;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dvo.AddAltDivCcyRequest;
import com.db.ess.synthesis.dvo.AddAltDivCcyResponse;
import com.db.ess.synthesis.dvo.DeleteAltDivCcyRequest;
import com.db.ess.synthesis.dvo.DeleteAltDivCcyResponse;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class DeleteAltDivCcyTask extends BaseExecuteDaoTask<DeleteAltDivCcyResponse> {

	private static final Logger logger = Logger.getLogger(DeleteAltDivCcyTask.class.getName());
	private DeleteAltDivCcyRequest request;
    
	public static final String DELETE_ALT_DIV_CCYS = 
		"DELETE DivOptionalPayCcy WHERE dividendId = ? AND optionCcyId = ? ";
	public static final String DELETE_ALT_DIV_CCYS_AUDIT = 
			"INSERT INTO SYN_AltDivCcyAudit (constituentTicker,ticker,exDate,recordDate,paymentDate," +
			"divCcy,divCcyGrossRate,divCcyNetRate,altDivCcy,altDivGrossRate,altDivNetRate,userIdCreate," +
			"createTime,userIdApply,applyTime,comments,divId,altDivCcyId ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,getDate(),?,getDate(),?,?,?)";
    
	public DeleteAltDivCcyTask(DeleteAltDivCcyRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
    
    @Override
    protected PreparedStatement[] createStatement(Connection c) throws SQLException {
    	PreparedStatement stmt = c.prepareStatement(DELETE_ALT_DIV_CCYS);
    	PreparedStatement audstmt = c.prepareStatement(DELETE_ALT_DIV_CCYS_AUDIT);
    	List<PreparedStatement> list = new ArrayList<PreparedStatement>();
    	stmt.setInt(1, request.getdividendId());
    	stmt.setInt(2, request.getccyId());
    	
    	//set up params for audit
    	audstmt.setString(1, request.getticker()==null?null:request.getunderlyingTicker());
    	audstmt.setString(2, request.getticker()==null?request.getunderlyingTicker():request.getticker());
    	audstmt.setDate(3, new java.sql.Date(new Date(request.getexDate()).getTime()));
    	audstmt.setDate(4, new java.sql.Date(new Date(request.getrecordDate()).getTime()));
    	audstmt.setDate(5, new java.sql.Date(new Date(request.getpaymentDate()).getTime()));
    	audstmt.setString(6, request.getdivCcy());
    	audstmt.setDouble(7, request.getdivCcyGrossRate());
    	audstmt.setDouble(8, request.getdivCcyNetRate());
    	CachedCurrency ccy = RefCache.getCurrency(request.getccyId(), location);
    	audstmt.setString(9, ccy!=null?ccy.getSwiftCode():"NA");
    	audstmt.setDouble(10, 0.0);
    	audstmt.setDouble(11, 0.0);
    	audstmt.setString(12, request.getuserName());
    	audstmt.setString(13, request.getuserName());
    	audstmt.setString(14, " ");
    	audstmt.setInt(15, request.getdividendId());
    	audstmt.setInt(16, request.getccyId());
    	list.add(stmt);
   	    list.add(audstmt);
   	    logger.info(">>> running " +
   	    	String.format(
   	    		"DELETE DivOptionalPayCcy WHERE dividendId = %s AND optionCcyId = %s(%s)", 
   	    	    request.getdividendId(),
   	    	    request.getccyId(),
   	    	    request.getccy()
   	    	    ));
   	    
   	 return (PreparedStatement[])list.toArray(new PreparedStatement[list.size()]);
    }
    

}
