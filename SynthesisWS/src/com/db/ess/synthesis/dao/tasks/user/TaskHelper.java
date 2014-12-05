package com.db.ess.synthesis.dao.tasks.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.Layout;
import com.db.ess.synthesis.dvo.UpdateUserProfileRequest;
import com.db.ess.synthesis.dvo.UserProfile;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;

public class TaskHelper {
	
	private static final Logger logger = Logger.getLogger(TaskHelper.class.getName());
	
	private static final String GET_USERLOCATION_EM_USER ="{ call SYN_UserValidation (?) }";
	private static final String SAVE_USERPROFILE = "{ call SYN_SaveUserProfile (?,?,?) }";
	private static final String GET_LAYOUT = "SELECT l.type, l.value "
			+ "FROM dbo.SYN_Layout l WHERE l.userId = ?";
	private static final String INSERT_LAYOUT = "INSERT INTO dbo.SYN_Layout "
			+ " (type, value, userId) VALUES ( ?, ?, ? )";
	private static final String DELETE_LAYOUT = "DELETE FROM dbo.SYN_Layout "
			+ " WHERE userId = ?";
	
	public static int Exist(String emailAddress, int location) throws Exception{
		int userId = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_USERLOCATION_EM_USER);
			stmt.setString(1, emailAddress);
			rs = stmt.executeQuery();
			if(rs.next()){
				userId = rs.getInt(1);
			}
		} catch (Exception ex) {
	        logger.error("Failed to execute query: ", ex); 
		    throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		return userId;
	}
	
	public static void updateProfile(UpdateUserProfileRequest request) throws Exception {
       updateProfile(request, ESSLocation.LONDON);
       updateProfile(request, ESSLocation.NEWYORK);
    }

	public static void updateProfile(UpdateUserProfileRequest request, int location) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(SAVE_USERPROFILE);
			stmt.setInt(1, request.getuser());
			stmt.setString(2,request.getname());
			stmt.setInt(3, request.getdefaultLocation());
			stmt.execute();
			logger.info("============================");
			logger.info("Updated user Profile.");
			logger.info("DB Location: " + location);
			logger.info("User: " + request.getuser());
			logger.info("User Name: " + request.getname());
			logger.info("User Location: " + request.getdefaultLocation());
			logger.info("============================");

			if (request.getlayoutIterator().hasNext())
			{
				updateLayout(request, location);
			}

		} catch (Exception ex) {
			logger.error(">>> failed to insert/update UserProfile: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, null);
		}
	}
	
	public static void setLayout(UserProfile up, int location) throws Exception {

		Layout layout = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int usrId = up.getuserId();
		if(location==2 && up.getuserIdNY() > 0)
		{
			usrId = up.getuserIdNY();
		}

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_LAYOUT);
			logger.info("Executing query: " + GET_LAYOUT);
			long time = System.currentTimeMillis(); 
			int i = 0;
			stmt.setInt(++i, usrId);
			rs = stmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				i = 0;
				layout = new Layout();
				layout.setuserId(up.getuserId());
				layout.settype(rs.getString(++i));
				layout.setvalue(rs.getString(++i));
				up.setlayout(layout);
			}
		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}

	public static boolean updateLayout(UpdateUserProfileRequest request, int location)
			throws Exception {

		boolean bRet = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			// Delete all layouts
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(DELETE_LAYOUT);
			stmt.setInt(1, request.getuser());
			int count = stmt.executeUpdate();
            logger.info(">>> Deleted " + count + " layouts");
			
			// Insert new layouts
			for (Iterator iter = request.getlayoutIterator(); iter.hasNext();) {
				try {
					Layout layout = (Layout) iter.next();

					stmt.close();
					stmt = conn.prepareStatement(INSERT_LAYOUT);
					int i = 0;
					stmt.setString(++i, layout.gettype());
					stmt.setString(++i, layout.getvalue());
					stmt.setInt(++i, request.getuser());
					stmt.executeUpdate();
				} catch (Exception e) {
					logger.error(">>> failed to insert layout: ", e);
				}
			}

		} catch (Exception ex) {
		     logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		return bRet;
	}
}
