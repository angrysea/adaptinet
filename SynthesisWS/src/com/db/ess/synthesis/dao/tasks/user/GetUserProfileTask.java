package com.db.ess.synthesis.dao.tasks.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.CacheDAO;
import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dvo.GetUserProfileRequest;
import com.db.ess.synthesis.dvo.GetUserProfileResponse;
import com.db.ess.synthesis.dvo.UpdateUserProfileRequest;
import com.db.ess.synthesis.dvo.UserProfile;
import com.db.ess.synthesis.exception.UserNotAuthenticatedException;
import com.db.ess.synthesis.util.ESSLocation;

public class GetUserProfileTask extends BaseQueryDaoTask<GetUserProfileResponse> {
	private static final Logger logger = Logger.getLogger(GetUserProfileTask.class.getName());
	private GetUserProfileRequest request;
    
	public static final String SELECT_USERPROFILE = 
		"SELECT up.userId, up.userName, up.defaultLocationId " +
	    "  FROM SYN_UserProfile up, ETS_Entitlement..EM_User emu WHERE up.userName=? and emu.userId=up.userId and emu.deleteFlag != 1";
	
    public GetUserProfileTask(GetUserProfileRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
	
	@Override
    protected Statement createStatement(Connection c) throws SQLException {
    	PreparedStatement stmt = c.prepareStatement(SELECT_USERPROFILE);
    	stmt.setString(1, request.getuser());
    	return stmt;
    }
    
	@Override
    protected ResultSet runStatement(Statement stmt) throws SQLException {
    	return ((PreparedStatement)stmt).executeQuery();
    }
    
	@Override
    protected int processResult(GetUserProfileResponse res, ResultSet rs) throws Exception {
    	int count = 0;
    	logger.info(">>> process data result for UserProfile....");
		UserProfile up = new UserProfile();
		boolean isPreferredExist = false;
		try {
			if (rs.next()) {
				up.setuserId(rs.getInt(1));
				up.setemail(rs.getString(2));
				up.setdefaultLocation(rs.getInt(3));
				logger.info(">>> ===================================== <<<");
				logger.info(">>> UserName["+ request.getuser() + " logging in...");
				logger.info(">>> UserId[" + up.getuserId() + 
						", UserLocation[" + up.getdefaultLocation());
				logger.info(">>> ===================================== <<<");
				
				isPreferredExist = true;
			} //else {

				String userEmail = request.getuser();
				up.setemail(userEmail);
				logger.info(">>> UserName[" + userEmail + "] does not exist in SYN_UserPfofile. Creating one...");

				boolean userFoundInLdn = false;
				boolean userFoundInNy = false;

				int userIdLdn = TaskHelper.Exist(userEmail,ESSLocation.LONDON);
				if(userIdLdn > 0){
					up.setuserId(userIdLdn);
					userFoundInLdn = true;
					up.setavailableLocation(1);
				}

				int userIdNy = TaskHelper.Exist(userEmail, ESSLocation.NEWYORK);
				if (userIdNy > 0){
					up.setuserId(userIdNy);
					userFoundInNy = true;
					up.setavailableLocation(2);
				}

				if (userFoundInLdn == true && userFoundInNy == true){
					up.setuserId(userIdLdn);
					up.setuserIdNY(userIdNy);
					up.setavailableLocation(3);
				}

				if (!userFoundInLdn && !userFoundInNy) {
					String message = ">>> The user, " + userEmail + ", does not exist.";
					logger.error(message);
					throw new UserNotAuthenticatedException(message); 
				}
				
			if(!isPreferredExist)
			{
				up.setdefaultLocation(up.getavailableLocation());
				// Insert user information to SYN_UserProfile
				UpdateUserProfileRequest updateProfileRequest = new UpdateUserProfileRequest();
				updateProfileRequest.setuser(up.getuserId());
				updateProfileRequest.setname(up.getemail());
				if(up.getavailableLocation() == 3)
				{
					updateProfileRequest.setdefaultLocation(1); //Default set to London
					updateProfileRequest.setlocation(1); //Default set to London
				} else {
					updateProfileRequest.setdefaultLocation(up.getavailableLocation());
					updateProfileRequest.setlocation(up.getavailableLocation());
				}
				TaskHelper.updateProfile(updateProfileRequest);
			}
			
			// set cache version - TODO: refactor the access to DAO....
			up.setcacheVersion(new CacheDAO().getCacheVersion());
			TaskHelper.setLayout(up, up.getdefaultLocation());
			res.setuserProfile(up);
		} catch (Exception ex) {
			logger.error(">>> failed to process GetUserProfile task: ", ex);
			throw ex;
		}
		return count;
    }
}
