package com.db.ess.synthesis.dao.tasks.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.AddFunctionEntitlementRequest;
import com.db.ess.synthesis.dvo.AddFunctionEntitlementResponse;
import com.db.ess.synthesis.dvo.FunctionEntitlement;

public class AddFunctionEntitlementTask extends BaseExecuteDaoTask<AddFunctionEntitlementResponse> {
	
	private static final Logger logger = Logger.getLogger(AddFunctionEntitlementTask.class.getName());
	private AddFunctionEntitlementRequest request;
    
	public static final String DELETE_ALL = 
		"DELETE ETS_Entitlement..EM_UserFunction WHERE userId = ? AND appId = ?";
	
	public static final String DELETE_SELECTED = 
	    "DELETE ETS_Entitlement..EM_UserFunction " +
	    "  FROM ETS_Entitlement..EM_User u, ETS_Entitlement..EM_UserFunction uf " +
	    " WHERE uf.appId = ? " +
	    "   AND upper(u.emailAddress) = upper(?) " +
	    "   AND u.userId = uf.userId ";
	
    public static final String INSERT_FUNCTION_ENTITLEMENT = 		
		"INSERT INTO ETS_Entitlement..EM_UserFunction (userId, appId, functionId, accessLevel,approverFlag) " +
		"VALUES (?, 7, ?, 1, 0)";
    
    // Copy Eli's entitlements for convenience
    public static final String DELETE_ALL_FUNCTIONAL_ENTITLEMENT = 
        "DELETE ETS_Entitlement..EM_UserFunction " +
        "  FROM ETS_Entitlement..EM_UserFunction uf, ETS_Entitlement..EM_User u " +
        " WHERE upper(u.emailAddress) = upper(?) " +
        "   AND u.userId = uf.userId ";
    
    public static final String INSERT_ELI_FUNCTIONAL_ENTITLEMENT = 
        "INSERT ETS_Entitlement..EM_UserFunction " +
        "SELECT (select userId " +
        "      from ETS_Entitlement..EM_User " + 
        "     where upper(emailAddress) = upper(?)) userId, " + 
        "    uf.appId, uf.functionId, uf.accessLevel, uf.approverFlag " +
        "  FROM ETS_Entitlement..EM_User u, ETS_Entitlement..EM_UserFunction uf, " +
        "       ETS_Entitlement..EM_Function f " +
        " WHERE upper(u.emailAddress) = upper('eli.holsinger@db.com') " +
        "   AND f.appId = uf.appId " +
        "   AND f.functionId = uf.functionId " + 
        "   AND u.userId = uf.userId ";    
    
    public static final String DELETE_ALL_DATA_ENTITLEMENT = 
    	"DELETE ETS_Entitlement..EM_UserBusinessUnit " +
    	"  FROM ETS_Entitlement..EM_UserBusinessUnit ubu, ETS_Entitlement..EM_User u " +
    	" WHERE upper(u.emailAddress) = upper(?) " +
    	"   AND u.userId = ubu.userId ";
    
    public static final String INSERT_ALL_DATA_ENTITLEMENT = 
    	"INSERT ETS_Entitlement..EM_UserBusinessUnit " +
    	"SELECT (select userId " + 
    	"          from ETS_Entitlement..EM_User " + 
    	"         where upper(emailAddress) = upper(?)) userId, "+ 
    	"        ubu.appId, ubu.functionId, ubu.accessLevel " +
    	"  FROM ETS_Entitlement..EM_User u, ETS_Entitlement..EM_UserBusinessUnit ubu " +
    	" WHERE upper(u.emailAddress) = upper('eli.holsinger@db.com') " + 
    	"   AND u.userId = ubu.userId "; 
	
    public AddFunctionEntitlementTask(AddFunctionEntitlementRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
    
    @Override
    protected PreparedStatement[] createStatement(Connection c) throws SQLException {
    	if (request.getcopy() == true) {
    		return createCopyStatement(c);
    	} else {
    		return createNonCopyStatement(c);
    	}
    }

    private PreparedStatement[] createCopyStatement (Connection c) throws SQLException {
    	
    	logger.info(">>> Copying Eli's entitlement...");
    	
    	List<PreparedStatement> list = new ArrayList<PreparedStatement>();
    	String userName = request.getuserName();
    	try {
    		if (TaskHelper.Exist(userName, location) > 0) {
    			PreparedStatement delete_all_function = c.prepareStatement(DELETE_ALL_FUNCTIONAL_ENTITLEMENT);
    			delete_all_function.setString(1, userName);
    			list.add(delete_all_function);

    			PreparedStatement insert_eli_function = c.prepareStatement(INSERT_ELI_FUNCTIONAL_ENTITLEMENT);
    			insert_eli_function.setString(1, userName);
    			list.add(insert_eli_function);

    			PreparedStatement delete_all_data = c.prepareStatement(DELETE_ALL_DATA_ENTITLEMENT);
    			delete_all_data.setString(1, userName);
    			list.add(delete_all_data);

    			PreparedStatement insert_eli_data = c.prepareStatement(INSERT_ALL_DATA_ENTITLEMENT);
    			insert_eli_data.setString(1, userName);
    			list.add(insert_eli_data);
    		}
    	} catch (Exception e) {
    		logger.error(">>> failed to copy Eli's entitlement..", e);
    	}
    	return (PreparedStatement[])list.toArray(new PreparedStatement[list.size()]);
    }
    
    private PreparedStatement[] createNonCopyStatement (Connection c) throws SQLException {
    	
    	logger.info(">>> Updating selected entitlements...");
    	
    	List<PreparedStatement> list = new ArrayList<PreparedStatement>();
    	String userName = request.getuserName();

    	try {
    		if (TaskHelper.Exist(userName, location) > 0) {

    			// Delete all
    			PreparedStatement stmt = c.prepareStatement(DELETE_ALL);
    			if (request.getuserId() <= 0) {
    				logger.error(">>> userId is not set!!!");
    				throw new SQLException ("userId is not set.");
    			}
    			stmt.setInt(1, request.getuserId());
    			stmt.setInt(2, 7); // hardcoded for the appId of functional entitlements
    			list.add(stmt);

    			// Delete selected
    			PreparedStatement stmtDelete = c.prepareStatement(DELETE_SELECTED);
    			stmtDelete.setInt(1, 7); // appId = 7 
    			stmtDelete.setString(2, request.getuserName());
    			list.add(stmtDelete);

    			Iterator iter = request.getentitlementsIterator();
    			while (iter.hasNext()) {
    				// Insert 
    				FunctionEntitlement fe = (FunctionEntitlement)iter.next();
    				PreparedStatement stmtInsert = c.prepareStatement(INSERT_FUNCTION_ENTITLEMENT);
    				stmtInsert.setInt(1, request.getuserId());
    				stmtInsert.setInt(2, fe.getfuncId()); 
    				list.add(stmtInsert);
    			}
    		}
    	} catch (Exception e) {
    		logger.error(">>> failed to update entitlements..");
    	}
    	return (PreparedStatement[])list.toArray(new PreparedStatement[list.size()]);
    }
}
