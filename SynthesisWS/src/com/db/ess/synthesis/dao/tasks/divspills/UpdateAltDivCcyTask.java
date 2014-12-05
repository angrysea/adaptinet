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
import com.db.ess.synthesis.dvo.UpdateAltDivCcyRequest;
import com.db.ess.synthesis.dvo.UpdateAltDivCcyResponse;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class UpdateAltDivCcyTask extends BaseExecuteDaoTask<UpdateAltDivCcyResponse> {

	private static final Logger logger = Logger.getLogger(UpdateAltDivCcyTask.class.getName());
	private UpdateAltDivCcyRequest request;
    
	public static final String UPDATE_ALT_DIV_CCYS =
		 "UPDATE DivOptionalPayCcy " +
		 "   SET optionCcyId = ?, optionAmount = ?, optionGross = ? " +
		 " WHERE dividendId = ? AND optionCcyId = ? ";
	public static final String UPDATE_ALT_DIV_CCYS_AUDIT = 
			"INSERT INTO SYN_AltDivCcyAudit (constituentTicker,ticker,exDate,recordDate,paymentDate," +
			"divCcy,divCcyGrossRate,divCcyNetRate,altDivCcy,altDivGrossRate,altDivNetRate,userIdCreate," +
			"createTime,userIdApply,applyTime,comments,divId,altDivCcyId  ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,getDate(),?,getDate(),?,?,?)";
	
    public UpdateAltDivCcyTask(UpdateAltDivCcyRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
    
    @Override
    protected PreparedStatement[] createStatement(Connection c) throws SQLException {
    	PreparedStatement stmt = c.prepareStatement(UPDATE_ALT_DIV_CCYS);
    	PreparedStatement audstmt = c.prepareStatement(UPDATE_ALT_DIV_CCYS_AUDIT);
    	List<PreparedStatement> list = new ArrayList<PreparedStatement>();
    	
    	stmt.setInt(1, request.getccyId());
    	stmt.setDouble(2, request.getnetRate());
    	stmt.setDouble(3, request.getgrossRate());
    	stmt.setInt(4, request.getdividendId());
    	stmt.setInt(5, request.getccyId());
    	
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
    	audstmt.setDouble(10, request.getgrossRate());
    	audstmt.setDouble(11, request.getnetRate());
    	audstmt.setString(12, request.getuserName());
    	audstmt.setString(13, request.getuserName());
    	audstmt.setString(14, request.getcomment());
    	audstmt.setInt(15, request.getdividendId());
    	audstmt.setInt(16, request.getccyId());
    	list.add(stmt);
   	    list.add(audstmt);
   	    
   	 logger.info(">>> running " +
   	    	String.format(
   	    	    "UPDATE DivOptionalPayCcy " +
   	    		"   SET optionCcyId = %s, optionAmount = %s, optionGross = %s " +
   	    		" WHERE dividendId = %s AND optionCcyId = %s ", 
   	    	    request.getccyId(),
   	    	    request.getnetRate(),
   	    	    request.getgrossRate(),
   	    	    request.getdividendId(),
   	    	    request.getccyId()
   	    	));
   	 
    	return (PreparedStatement[])list.toArray(new PreparedStatement[list.size()]);
    }
}
