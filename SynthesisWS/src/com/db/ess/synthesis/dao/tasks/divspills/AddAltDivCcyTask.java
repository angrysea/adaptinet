package com.db.ess.synthesis.dao.tasks.divspills;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.AddAltDivCcyRequest;
import com.db.ess.synthesis.dvo.AddAltDivCcyResponse;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class AddAltDivCcyTask extends BaseExecuteDaoTask<AddAltDivCcyResponse> {

	private static final Logger logger = Logger.getLogger(AddAltDivCcyTask.class.getName());
	private AddAltDivCcyRequest request;
    
	public static final String INSERT_ALT_DIV_CCYS = 
		"INSERT INTO DivOptionalPayCcy (dividendId,optionCcyId,optionAmount,optionGross) VALUES (?,?,?,?)";
	public static final String INSERT_ALT_DIV_CCYS_AUDIT = 
			"INSERT INTO SYN_AltDivCcyAudit (constituentTicker,ticker,exDate,recordDate,paymentDate," +
			"divCcy,divCcyGrossRate,divCcyNetRate,altDivCcy,altDivGrossRate,altDivNetRate,userIdCreate," +
			"createTime,userIdApply,applyTime,comments,divId,altDivCcyId ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,getDate(),?,getDate(),?,?,?)";
	
    public AddAltDivCcyTask(AddAltDivCcyRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
    
    @Override
    protected PreparedStatement[] createStatement(Connection c) throws SQLException {
    	
    	List<PreparedStatement> list = new ArrayList<PreparedStatement>();
    	PreparedStatement stmt = c.prepareStatement(INSERT_ALT_DIV_CCYS);
    	PreparedStatement audstmt = c.prepareStatement(INSERT_ALT_DIV_CCYS_AUDIT);
    	
    	// set up params for insert
    	stmt.setInt(1, request.getdividendId());
    	CachedCurrency cur = RefCache.getCurrency(request.getaltCcyCode(), request.getlocation());
    	stmt.setInt(2, cur.getCcyId()); 
    	stmt.setDouble(3, request.getaltNetRate());
    	stmt.setDouble(4, request.getaltGrossRate());
    	
    	//set up params for audit
    	audstmt.setString(1, request.getticker()==null?null:request.getunderlyingTicker());
    	audstmt.setString(2, request.getticker()==null?request.getunderlyingTicker():request.getticker());
    	audstmt.setDate(3, new java.sql.Date(new Date(request.getexDate()).getTime()));
    	audstmt.setDate(4, new java.sql.Date(new Date(request.getrecordDate()).getTime()));
    	audstmt.setDate(5, new java.sql.Date(new Date(request.getpaymentDate()).getTime()));
    	audstmt.setString(6, request.getdivCcy());
    	audstmt.setDouble(7, request.getdivCcyGrossRate());
    	audstmt.setDouble(8, request.getdivCcyNetRate());
    	audstmt.setString(9, request.getaltCcyCode());
    	audstmt.setDouble(10, request.getaltGrossRate());
    	audstmt.setDouble(11, request.getaltNetRate());
    	audstmt.setString(12, request.getuserName());
    	audstmt.setString(13, request.getuserName());
    	audstmt.setString(14, request.getcomment());
    	audstmt.setInt(15, request.getdividendId());
    	audstmt.setInt(16, cur.getCcyId());
    	
   	    list.add(stmt);
   	    list.add(audstmt);
    	return (PreparedStatement[])list.toArray(new PreparedStatement[list.size()]);
    }
    
}
