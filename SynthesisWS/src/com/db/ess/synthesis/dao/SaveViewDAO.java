package com.db.ess.synthesis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.DeleteSavedViewRequest;
import com.db.ess.synthesis.dvo.GetSavedViewRequest;
import com.db.ess.synthesis.dvo.GetSavedViewResponse;
import com.db.ess.synthesis.dvo.InsertSavedViewRequest;
import com.db.ess.synthesis.dvo.SavedView;
import com.db.ess.synthesis.dvo.UpdateSavedViewRequest;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;

public class SaveViewDAO {

	private static Logger logger = Logger.getLogger(SaveViewDAO.class.getName());

	private static final String GET_SAVEDVIEWS = "select sv.userId, sv.viewerName, sv.componentName, "
		+ " sv.savedViewName, sv.isPublic, sv.lastUpdated, sv.layout "
		+ " from SYN_SaveView sv "
		+ " where (sv.userId = ? or isPublic = 1) ";
//		+ "   and  sv.viewerName = ? ";

	private static final String INSERT_SAVEDVIEW = "insert into dbo.SYN_SaveView "
		+ " (userId, viewerName, componentName, savedViewName, isPublic, lastUpdated, layout) "
		+ " values ( ?, ?, ?, ?, ?, ?, ? ) ";

	private static final String UPDATE_SAVEDVIEW = "update dbo.SYN_SaveView "
		+ "set savedViewName = ?, isPublic = ?, lastUpdated = ?, layout = ? " 
		+ " where userId = ? " 
		+ "   and viewerName = ? "
		+ "   and componentName = ? "
		+ "   and savedViewName = ? ";

	private static final String DELETE_SAVEDVIEW = "delete from dbo.SYN_SaveView "
		+ " where userId = ? " 
		+ "   and viewerName = ? "
		+ "   and componentName = ? "
		+ "   and savedViewName = ? ";

	public GetSavedViewResponse getSavedView(GetSavedViewRequest request)
	throws Exception {
		logger.info("Inside getSavedView(), request recieved: " + request);
		GetSavedViewResponse response = new GetSavedViewResponse();

		int location = request.getdefaultLocation();

		// There is no need to store saved views in both databases.
		// Just use London for all the time
		switch (location) {
		case ESSLocation.UNKNOWN:
		case ESSLocation.LONDON:
		case ESSLocation.NEWYORK:
		case ESSLocation.GLOBAL:
			getSavedView (request, ESSLocation.LONDON, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public void getSavedView(GetSavedViewRequest request, int location, GetSavedViewResponse response)
	throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_SAVEDVIEWS);

			stmt.setInt(1, request.getuserId());
			//stmt.setString(2, request.getviewerName());
			
			
			StringBuilder sb = new StringBuilder();
			sb.append("userId = ");
			sb.append(request.getuserId());
			sb.append(", viewerName = ");
			sb.append(request.getviewerName());
			logger.info(sb.toString());
			logger.info("Executing query: " + GET_SAVEDVIEWS);
			rs = stmt.executeQuery();

			while (rs.next()) {
				SavedView savedView = new SavedView();
				savedView.setuserId(rs.getInt(1));
				savedView.setviewerName(rs.getString(2));
				savedView.setcomponentName(rs.getString(3));
				savedView.setsavedViewName(rs.getString(4));
				savedView.setisPublic(rs.getBoolean(5));

				Timestamp lastUpdatedTimeStamp = rs.getTimestamp(6);
				Date lastUpdatedDate = lastUpdatedTimeStamp == null ? null : new Date(
						lastUpdatedTimeStamp.getTime());
				savedView.setlastUpdated(lastUpdatedDate);

				savedView.setlayout(rs.getString(7));

				response.setsavedViews(savedView);
			}

		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}

	public boolean insertLayout(InsertSavedViewRequest request, int location)
	    throws Exception {

		boolean bRet = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(ESSLocation.LONDON);

			for (Iterator iter = request.getsavedViewsIterator(); iter.hasNext();) {
				try {
					SavedView savedView = (SavedView) iter.next();

					stmt = conn.prepareStatement(INSERT_SAVEDVIEW);
					stmt.setInt(1, savedView.getuserId());
					stmt.setString(2, savedView.getviewerName());
					stmt.setString(3, savedView.getcomponentName());
					stmt.setString(4, savedView.getsavedViewName());
					stmt.setBoolean(5, savedView.getisPublic());
					stmt.setDate(6, new java.sql.Date(new Date().getTime()) );
					stmt.setString(7, savedView.getlayout());
					stmt.executeUpdate();
					stmt.close();
				} catch (Exception e) {
					logger.error(">>> failed to insert layout: ", e);
				}
			}

		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}

		return bRet;
	}

	public boolean updateLayout(UpdateSavedViewRequest request, int location)
	throws Exception {

		boolean bRet = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(ESSLocation.LONDON);
			stmt = conn.prepareStatement(UPDATE_SAVEDVIEW);
			
			for (Iterator iter = request.getsavedViewsIterator(); iter.hasNext();) {
				try {
					SavedView savedView = (SavedView) iter.next();
					stmt.setString(1, savedView.getsavedViewName());
					stmt.setBoolean(2, savedView.getisPublic());
					stmt.setTimestamp(3, new Timestamp(new Date().getTime()) );
					stmt.setString(4, savedView.getlayout());

					stmt.setInt(5, savedView.getuserId());
					stmt.setString(6, savedView.getviewerName());
					stmt.setString(7, savedView.getcomponentName());
					stmt.setString(8, savedView.getoldSavedViewName());
					stmt.executeUpdate();
					
				} catch (Exception e) {
					logger.error(">>> failed to update layout: ", e);
				}
			}

		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}

		return bRet;
	}
	

	public boolean deleteLayout(DeleteSavedViewRequest request, int location)
	    throws Exception {

		boolean bRet = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(ESSLocation.LONDON);

			for (Iterator iter = request.getsavedViewsIterator(); iter.hasNext();) {
				try {
					SavedView savedView = (SavedView) iter.next();

					stmt = conn.prepareStatement(DELETE_SAVEDVIEW);
					stmt.setInt(1, savedView.getuserId());
					stmt.setString(2, savedView.getviewerName());
					stmt.setString(3, savedView.getcomponentName());
					stmt.setString(4, savedView.getsavedViewName());
					stmt.executeUpdate();
	                stmt.close();

				} catch (Exception e) {
					logger.error(">>> failed to update layout: ", e);
				}
			}

		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}

		return bRet;
	}
}
