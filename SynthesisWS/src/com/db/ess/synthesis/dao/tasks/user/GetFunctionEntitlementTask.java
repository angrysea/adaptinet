package com.db.ess.synthesis.dao.tasks.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dvo.FunctionEntitlement;
import com.db.ess.synthesis.dvo.GetFunctionEntitlementRequest;
import com.db.ess.synthesis.dvo.GetFunctionEntitlementResponse;

public class GetFunctionEntitlementTask extends BaseQueryDaoTask<GetFunctionEntitlementResponse> {
	private static final Logger logger = Logger.getLogger(GetFunctionEntitlementTask.class.getName());
	private GetFunctionEntitlementRequest request;
    
	public static final String SELECT_FUNCTION_ENTITLEMENT = 
		  " SELECT uf.appId, "
		+ "        uf.functionId, f.description, u.location " 
		+ "   FROM ETS_Entitlement..EM_User u, ETS_Entitlement..EM_UserFunction uf, "
		+ "        ETS_Entitlement..EM_Function f "
		+ "  WHERE upper(u.emailAddress) = upper(?) "
		+ "    AND f.appId = uf.appId "
		+ "    AND f.functionId = uf.functionId "
		+ "    AND u.userId = uf.userId ";
	
    public GetFunctionEntitlementTask(GetFunctionEntitlementRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
	
	@Override
    protected Statement createStatement(Connection c) throws SQLException {
    	PreparedStatement stmt = c.prepareStatement(SELECT_FUNCTION_ENTITLEMENT);
    	stmt.setString(1, request.getuser());
    	return stmt;
    }
    
	@Override
    protected ResultSet runStatement(Statement stmt) throws SQLException {
    	return ((PreparedStatement)stmt).executeQuery();
    }
    
	@Override
    protected int processResult(GetFunctionEntitlementResponse res, ResultSet rs) throws SQLException {
    	int count = 0;
    	logger.info(">>> process data result for Functional Entitlements....");
		while (rs.next()) {
			FunctionEntitlement entitlement = new FunctionEntitlement();
			entitlement.setlocation(request.getlocation());
			int i = 0;
			entitlement.setappId(rs.getInt(++i));
			entitlement.setfuncId(rs.getInt(++i));
			entitlement.setdescription(rs.getString(++i));
			entitlement.settradingLocation(rs.getString(++i));
			res.setentitlements(entitlement);
			count++;
		}
		return count;
    }
}
