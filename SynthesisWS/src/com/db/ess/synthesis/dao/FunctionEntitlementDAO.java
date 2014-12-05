package com.db.ess.synthesis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.FunctionEntitlement;
import com.db.ess.synthesis.dvo.GetFunctionEntitlementRequest;
import com.db.ess.synthesis.dvo.GetFunctionEntitlementResponse;
import com.db.ess.synthesis.dvo.GetUserProfileRequest;
import com.db.ess.synthesis.dvo.UserProfile;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;

public class FunctionEntitlementDAO {

	private static Logger logger = Logger
			.getLogger(FunctionEntitlementDAO.class.getName());
	private static String GET_FUNCTENTL = " SELECT uf.appId, "
			+ " uf.functionId, f.description, u.location " 
			+ " FROM ETS_Entitlement..EM_User u, ETS_Entitlement..EM_UserFunction uf, "
			+ " ETS_Entitlement..EM_Function f "
			+ " WHERE upper(u.emailAddress) = upper(?) "
			+ "   AND f.appId = uf.appId "
			+ "   AND f.functionId = uf.functionId "
			+ "   AND u.userId = uf.userId ";
	
	public GetFunctionEntitlementResponse getEntitlements(GetFunctionEntitlementRequest request) throws Exception {
		GetFunctionEntitlementResponse response = new GetFunctionEntitlementResponse();
		logger.info("Inside getEntitlements(), request received: "+request);
		int location = request.getlocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

		case ESSLocation.LONDON:
			getEntitlements(request, ESSLocation.LONDON, response);
			break;
			
		case ESSLocation.NEWYORK:
			getEntitlements(request, ESSLocation.NEWYORK, response);
			break;

		case ESSLocation.GLOBAL:
			getEntitlements(request, ESSLocation.LONDON, response);
			getEntitlements(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}

	public void getEntitlements(GetFunctionEntitlementRequest request, int location, GetFunctionEntitlementResponse response) throws Exception {
	
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_FUNCTENTL);
			stmt.setString(1, request.getuser());
			logger.info("Executing stmt: "+stmt);
			long time = System.currentTimeMillis();
			rs = stmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			//logger.info(">>> Functional Entitlement from " + location + " for user["+request.getuser()+"]");
			while (rs.next()) {
				FunctionEntitlement entitlement = new FunctionEntitlement();
				entitlement.setlocation(location);
				int i = 0;
				entitlement.setappId(rs.getInt(++i));
				entitlement.setfuncId(rs.getInt(++i));
				entitlement.setdescription(rs.getString(++i));
				entitlement.settradingLocation(rs.getString(++i));
				response.setentitlements(entitlement);
				//logger.info(">>> AppId["+entitlement.getappId()+"], FuncId["+entitlement.getfuncId());
			}

		} catch (Exception ex) {
		     logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}
	
	public void getEntitlements(GetUserProfileRequest request,
			UserProfile profile) throws Exception {
		
		logger.info("Inside getEntitlements(), request received: "+request+" UserProfile: "+profile);
		int location = profile.getdefaultLocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getEntitlements(request, profile, ESSLocation.LONDON);
			break;
			
		case ESSLocation.NEWYORK:
			getEntitlements(request, profile, ESSLocation.NEWYORK);
			break;

		case ESSLocation.GLOBAL:
			getEntitlements(request, profile, ESSLocation.LONDON);
			getEntitlements(request, profile, ESSLocation.NEWYORK);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
	}

	public void getEntitlements(GetUserProfileRequest request,
			UserProfile profile,  int location) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_FUNCTENTL);
			stmt.setString(1, request.getuser());
			logger.info("Executing stmt: "+stmt);
			long time = System.currentTimeMillis();
			rs = stmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			//logger.info(">>> Functional Entitlement from " + location + " for user["+request.getuser()+"]");
			while (rs.next()) {
				FunctionEntitlement entitlement = new FunctionEntitlement();
				entitlement.setlocation(request.getlocation());
				int i = 0;
				entitlement.setappId(rs.getInt(++i));
				entitlement.setfuncId(rs.getInt(++i));
				entitlement.setdescription(rs.getString(++i));
				profile.setfunctionEntitlement(entitlement);
				//logger.info(">>> AppId["+entitlement.getappId()+"], FuncId["+entitlement.getfuncId()+"]");
			}
			
			Iterator iter = profile.getfunctionEntitlementIterator();
			int count = 0;
			while(iter.hasNext()) {
				iter.next(); count++;
			}
			logger.info(">>> FE count = " + count);

		} catch (Exception ex) {
		     logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}
}
