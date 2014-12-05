package com.db.ess.synthesis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.Book;
import com.db.ess.synthesis.dvo.GetBooksRequest;
import com.db.ess.synthesis.dvo.GetBooksResponse;
import com.db.ess.synthesis.dvo.GetUserProfileRequest;
import com.db.ess.synthesis.dvo.GetUserProfileResponse;
import com.db.ess.synthesis.dvo.Layout;
import com.db.ess.synthesis.dvo.UpdatePreferredBooksRequest;
import com.db.ess.synthesis.dvo.UpdateUserProfileRequest;
import com.db.ess.synthesis.dvo.UserProfile;
import com.db.ess.synthesis.exception.UserNotAuthenticatedException;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;

public class UserProfileDAO extends SynthesisBaseDAO {

	private static Logger logger = Logger.getLogger(UserProfileDAO.class
			.getName());

	private static final String GET_USERLOCATION_EM_USER ="{ call SYN_UserValidation (?) }";
	
	private static final String SAVE_USERPROFILE = "{ call SYN_SaveUserProfile (?,?,?) }";
	
	private static final String GET_LAYOUT = "select l.type, l.value "
			+ "from dbo.SYN_Layout l where l.userId = ?";
	private static final String INSERT_LAYOUT = "insert into dbo.SYN_Layout "
			+ " (type, value, userId) values ( ?, ?, ? )";
	private static final String DELETE_LAYOUT = "delete from dbo.SYN_Layout "
			+ " where userId = ?";
	private static final String GET_BOOKS = "{ call dbo.SYN_GetBookPreferences ( ";
	private static final String UPDATE_BOOKS = "{ call dbo.SYN_AddBookPreferences (@UserId=?, @QueryString=?) }";
	private static final String GET_USERPROFILE_BY_USERNAME = 
		"SELECT up.userId, up.userName, up.defaultLocationId " +
	    "  FROM SYN_UserProfile up, ETS_Entitlement..EM_User emu WHERE up.userName=? and emu.userId=up.userId and emu.deleteFlag != 1";
	private static final String GET_USERPROFILE_BY_USERID = 
		"SELECT userId, userName, defaultLocationId " +
	    "  FROM SYN_UserProfile WHERE userId=?";
	
	public GetUserProfileResponse getUserProfile(GetUserProfileRequest request)
			throws Exception {
		logger.info("Inside getUserProfile(), request recieved: " + request);
		GetUserProfileResponse response = new GetUserProfileResponse();

		int location = request.getlocation();

		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getUserProfile(request, location, response);
			break;

		case ESSLocation.NEWYORK:
			getUserProfile(request, location, response);
			break;

		case ESSLocation.GLOBAL:
			getUserProfile(request, ESSLocation.LONDON, response);
			getUserProfile(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public void getUserProfile(GetUserProfileRequest request, int location,
			GetUserProfileResponse response) throws Exception {

		UserProfile userProfile = new UserProfile();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isPreferredExist = false;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_USERPROFILE_BY_USERNAME);
			stmt.setString(1, request.getuser());
			rs = stmt.executeQuery();
			
			// If SYN_UserProfile has the user information, use it 
			if (rs.next()) {
				userProfile.setuserId(rs.getInt(1));
				userProfile.setemail(rs.getString(2));
				userProfile.setdefaultLocation(rs.getInt(3));
				logger.info(">>> ===================================== <<<");
				logger.info(">>> UserName["+ request.getuser() + " logging in...");
				logger.info(">>> UserId[" + userProfile.getuserId() + 
						    ", UserLocation[" + userProfile.getdefaultLocation());
				logger.info(">>> ===================================== <<<");
			
				isPreferredExist = true;
			// Otherwise check ETS_Entitlement..EM_User 
			} //else {
			
			String userEmail = request.getuser();
				
				userProfile.setemail(userEmail);
				
			boolean userFoundInLdn = false;
			boolean userFoundInNy = false;

				// Check if the user exists in ESS London
			int userIdLdn = getUserIdForLocation(userEmail,1);
			if(userIdLdn > 0){
				userProfile.setuserId(userIdLdn);
				userFoundInLdn = true;
				userProfile.setavailableLocation(1);
			}
			
				// Check if the user exists in ESS NY
			int userIdNy = getUserIdForLocation(userEmail,2);
			if(userIdNy > 0){
				userProfile.setuserId(userIdNy);
				userFoundInNy = true;
				userProfile.setavailableLocation(2);
			}
			
			if(userFoundInLdn == true && userFoundInNy == true){
				userProfile.setuserId(userIdLdn);
				userProfile.setuserIdNY(userIdNy);
				userProfile.setavailableLocation(3);
			}
			
			if (!userFoundInLdn && !userFoundInNy) {
				String message = ">>> The user, " + userEmail + ", does not exist.";
				logger.error(message);
				throw new UserNotAuthenticatedException(message); 
			}
				
			// Insert user information to SYN_UserProfile
			if(!isPreferredExist)
			{
				userProfile.setdefaultLocation(userProfile.getavailableLocation());
				logger.info(">>> UserName[" + userEmail + "] does not exist in SYN_UserPfofile. Creating one...");
				UpdateUserProfileRequest updateProfileRequest = new UpdateUserProfileRequest();
				updateProfileRequest.setuser(userProfile.getuserId());
				updateProfileRequest.setname(userProfile.getemail());
				if(userProfile.getavailableLocation() == 3)
				{
					updateProfileRequest.setdefaultLocation(1); //Default set to London
					updateProfileRequest.setlocation(1); //Default set to London
				} else {
					updateProfileRequest.setdefaultLocation(userProfile.getavailableLocation());
					updateProfileRequest.setlocation(userProfile.getavailableLocation());
				}
				updateProfile(updateProfileRequest);
			}
			setLayout(userProfile, location);
			response.setuserProfile(userProfile);
		} catch (UserNotAuthenticatedException unae) {
			logger.error(">>> throwing the exception...");
			throw unae;
		} catch (Exception ex) {
		     logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}
	
	public int getUserIdForLocation(String emailAddress, int location) throws Exception{
		int userId = 0;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try{
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_USERLOCATION_EM_USER);
	
			stmt.setString(1, emailAddress);
			logger.info("Executing query: " + GET_USERLOCATION_EM_USER);
			long time = System.currentTimeMillis(); 
			rs = stmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			if(rs.next()){
				userId = rs.getInt(1);
			}
		}catch (Exception ex) {
	     logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		
		return userId;
	}

	public boolean updateProfile(UpdateUserProfileRequest request)
			throws Exception {
		logger.info("Inside updateProfile(), request recieved: " + request);
		boolean bRet = false;
	    bRet = updateProfile(request, ESSLocation.LONDON);
		bRet = updateProfile(request, ESSLocation.NEWYORK);
		return bRet;
	}

	public boolean updateProfile(UpdateUserProfileRequest request, int location)
			throws Exception {

		boolean bRet = false;
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
            	logger.info(">>> Update Layout as well....");
			    updateLayout(request, location);
            }

		} catch (Exception ex) {
			logger.error(">>> failed to insert/update UserProfile: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, null);
		}
		return bRet;
	}

	public void setLayout(UserProfile up, int location) throws Exception {

		Layout layout = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_LAYOUT);
			logger.info("Executing query: " + GET_LAYOUT);
			long time = System.currentTimeMillis(); 
			int i = 0;
			stmt.setInt(++i, up.getuserId());
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

	public boolean updateLayout(UpdateUserProfileRequest request, int location)
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

	public GetBooksResponse getBooks(GetBooksRequest request) throws Exception {
		GetBooksResponse response = new GetBooksResponse();
		getBooks(request, ESSLocation.LONDON, response);
		getBooks(request, ESSLocation.NEWYORK, response);
		
		return response;
	}

	public void getBooks(GetBooksRequest request, int location,
			GetBooksResponse response) throws Exception {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = createBooksQuery(request);
		logger.info("Executing query : " + query);
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			cstmt = conn.prepareCall(query);
			setBooksParameters(request, cstmt);
			rs = cstmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			int count = 0; 
			int i;
			while (rs.next()) {
				i = 0;
				Book book = new Book();
				book.setbookId(rs.getInt(++i));
				book.setacctId(rs.getInt(++i));
				book.setname(rs.getString(++i));
				book.setfullName(rs.getString(++i));
				book.setmnemonic(rs.getString(++i));
				book.setadpAcct(rs.getString(++i));
				book.setessLocation(location);
				response.setbooks(book);
				count++;
			}
			StringBuilder sb = new StringBuilder();
            sb.append("=======================\r\n");
			sb.append("  Location: ");
			sb.append(location);
			sb.append("\r\n");
			sb.append("      User: ");
			sb.append(request.getuserId());
			sb.append("\r\n");
			sb.append(" Book type: ");
			sb.append(request.getrequestType());
			sb.append("\r\n");
			sb.append("Book count: ");
			sb.append(count);
			sb.append("\r\n");
			sb.append("=======================");
			logger.info(sb.toString());
			
		} catch (Exception ex) {
			logger.error(">>> Failed to fetch BookPreferences: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, cstmt, rs);
		}
	}

	private String createBooksQuery(GetBooksRequest request) {
		StringBuilder qb = new StringBuilder(GET_BOOKS);
		if (request.getuserId() > 0) {
			qb.append("@UserId = ?");
		}
		if (request.getrequestType() != null) {
			qb.append(", @RequestType = ?");
		}
		if (request.getemailAddress() != null) {
			qb.append(", @EmailAddress = ?");
		}
		qb.append(")}");
		return qb.toString();

	}

	private void setBooksParameters(GetBooksRequest request,
			CallableStatement cstmt) throws SQLException {
		int i = 0;
		if (request.getuserId() > 0)
			cstmt.setInt(++i, request.getuserId());
		if (request.getrequestType() != null)
			cstmt.setString(++i, request.getrequestType());
		if (request.getemailAddress() != null)
			cstmt.setString(++i, request.getemailAddress());

	}

	public void updatePreferredBooks(UpdatePreferredBooksRequest request)
			throws Exception {
		int location = request.getoldLocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
		case ESSLocation.LONDON:
			updatePreferredBooks(request, ESSLocation.LONDON);
			break;

		case ESSLocation.NEWYORK:
			updatePreferredBooks(request, ESSLocation.NEWYORK);
			break;

		case ESSLocation.GLOBAL:
			updatePreferredBooks(request, ESSLocation.LONDON);
			updatePreferredBooks(request, ESSLocation.NEWYORK);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		
		// Update user profile to update the location if changed
		if (request.getlocation() != request.getoldLocation()) {
			UpdateUserProfileRequest updateUserProfileRequest = new UpdateUserProfileRequest();
			updateUserProfileRequest.setuser(request.getuserId());
			if(request.getlocation() == ESSLocation.LONDON)	
				updateUserProfileRequest.setname(getUserName(request.getuserId(), ESSLocation.LONDON));
			else
				updateUserProfileRequest.setname(getUserName(request.getuserId(), ESSLocation.NEWYORK));
			updateUserProfileRequest.setdefaultLocation(request.getlocation());
			updateProfile(updateUserProfileRequest);
		}
	}
	
	public String getUserName(int userId, int location) throws Exception {
		String userName = ""; 
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_USERPROFILE_BY_USERID);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			while (rs.next()) {
                userName = rs.getString(2);
			}
			logger.info(">>> userName for userId " + userId + " is " + userName);
		} catch (Exception ex) {
			logger.error(">>> Failed to get user name: ", ex);
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	    return userName;	
	}

	public void updatePreferredBooks(UpdatePreferredBooksRequest request, int location) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			long time = System.currentTimeMillis();
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(UPDATE_BOOKS);

			int userId = request.getuserId();
			stmt.setInt(1, userId);
			String queryString = getWildCardSearchString(request.getqueryString(), "upper(b.name)");
			
			logger.info(">>> queryString = " + queryString);
			
			stmt.setString(2, queryString);
			
			int count = stmt.executeUpdate();
			logger.info("=================================================");
			logger.info(">>> " + count + " rows are added in " + 
					(System.currentTimeMillis() - time) + "ms");
			
		} catch (Exception ex) {
		     logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}

}
