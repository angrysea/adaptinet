package com.db.ess.synthesis.dao.tasks.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.dvo.UpdateUserProfileRequest;

public class UpdateUserProfileTask extends BaseExecuteDaoTask<ReturnResponse> {
	private static final Logger logger = Logger.getLogger(UpdateUserProfileTask.class.getName());
	private UpdateUserProfileRequest request;
    
	private static final String SAVE_USERPROFILE = "{ call SYN_SaveUserProfile (?,?,?) }";
	
    public UpdateUserProfileTask(UpdateUserProfileRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
    
    @Override
    protected PreparedStatement[] createStatement(Connection c) throws SQLException {
    	
    	List<PreparedStatement> list = new ArrayList<PreparedStatement>();

    	PreparedStatement stmt = c.prepareStatement(SAVE_USERPROFILE);
		stmt.setInt(1, request.getuser());
		stmt.setString(2,request.getname());
		stmt.setInt(3, request.getdefaultLocation());
		list.add(stmt);
		
		logger.info("===============================================");
        logger.info("UpdateUserProfileTask updating user Profile.");
        logger.info("DB Location: " + location);
        logger.info("User: " + request.getuser());
        logger.info("User Name: " + request.getname());
        logger.info("User Location: " + request.getdefaultLocation());
        logger.info("===============================================");
		
        if (request.getlayoutIterator().hasNext())
        {
        	logger.info(">>> Update Layout as well....");
        	try {
		        TaskHelper.updateLayout(request, location);
        	} catch (Exception ex) {
        		logger.error(">>> Failed to update layout.....", ex);
        	}
        }
    	
        return (PreparedStatement[])list.toArray(new PreparedStatement[list.size()]);
    }
}
