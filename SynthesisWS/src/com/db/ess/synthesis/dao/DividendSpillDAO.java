package com.db.ess.synthesis.dao;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.log4j.Logger;
import com.db.ess.synthesis.dvo.DividendSpill;
import com.db.ess.synthesis.dvo.GetDividendSpillRequest;
import com.db.ess.synthesis.dvo.GetDividendSpillResponse;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;
import com.db.ess.synthesis.util.SynthesisConstants;


public class DividendSpillDAO extends SynthesisBaseDAO {
	private static final Logger logger = Logger.getLogger(DividendSpillDAO.class.getName());
	private static final String GET_DIVIDENDSPILL = "{ call dbo.SYN_GetSpillQueueESS(";

	public GetDividendSpillResponse getDividendSpill (GetDividendSpillRequest request) throws Exception {

		GetDividendSpillResponse response = new GetDividendSpillResponse();
		logger.info("Inside getDividendSpill...");
		  int location = request.getlocation();
		  switch (location) {

			    case ESSLocation.UNKNOWN:
			    	location = ESSLocation.LONDON;
			    // Fall thru using london as a default.
			    case ESSLocation.LONDON:
			    	getDividendSpill(request, ESSLocation.LONDON, response);
			    break;
			    case ESSLocation.NEWYORK:
			    	getDividendSpill(request, ESSLocation.NEWYORK, response);
			    break;
			    case ESSLocation.GLOBAL:
			    	getDividendSpill(request, ESSLocation.LONDON, response);
			    	getDividendSpill(request, ESSLocation.NEWYORK, response);
			    break;
			    default:
			    	throw new Exception("Invalid or Unknown location.");

		  }
		 return response;

		}
	
	public GetDividendSpillResponse getDividendSpill(GetDividendSpillRequest request,
			int location, GetDividendSpillResponse response) throws Exception {
			Connection conn = null;
			CallableStatement cstmt = null;
			ResultSet rs = null;
			String query = createQueryString(request);
			long time = System.currentTimeMillis(); 
			try {
					conn = SQLUtils.getConnection(location);
					cstmt = conn.prepareCall(query);
					setParameters(request, cstmt);
					rs = cstmt.executeQuery();
					logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
					int count = 0;
					while (rs.next()) {
						if (count > SynthesisConstants.MAX_RESULT_SET_SIZE) {
							response.setreturnResponse(getResultSetOverFlowResponse());
							break;
						}
					    DividendSpill dividendSpill = populateDividendSpill(rs, location);
					    response.setdividendSpill(dividendSpill);
					    count++;
					}

			logger.info("Found ["+count+"] DividendSpill For the given search criteria.");
			} catch (Exception ex) {
				logger.error("Failed to execute query: ", ex); 
				throw new Exception(ex);
			} finally {
				SQLUtils.closeResources(conn, cstmt, rs);
				}
		  return response;

		}
	private String createQueryString(GetDividendSpillRequest request) {
		StringBuilder qb = new StringBuilder(GET_DIVIDENDSPILL);
		StringBuilder log = new StringBuilder(GET_DIVIDENDSPILL);
		boolean paramSet = false;
	
			qb.append(" @feedDividendRef = ?");
			log.append(" @feedDividendRef = " + request.getfeedId());
			paramSet = true;
		
		if (request.getunderlyingId() >=0) {
			if (paramSet) {
				qb.append(", @underlyingId = ?");
				log.append(", @underlyingId = " + request.getunderlyingId());
			}
			else{
				qb.append("@underlyingId = ?");
				log.append("@underlyingId = " + request.getunderlyingId());
				paramSet = true;
			}
		}
		if (request.getinstrId() >= 0) {
			if (paramSet){
				qb.append(", @instrId = ?");
				log.append(", @instrId = " + request.getinstrId());
			}
			else{
				qb.append("@instrId = ?");
				log.append("@instrId = " + request.getinstrId());
				paramSet = true;
			}
		}
		
		if (paramSet){
			qb.append(", @Status = ?");
			log.append(", @Status = " + request.getstatus());
		}
		else{
			qb.append("@Status = ?");
			log.append("@Status = " + request.getstatus());
		}
		
		qb.append(", @ExDate = ?");
		log.append(", @ExDate = " + request.getexDate());
        
		qb.append(", @RecDate = ?");
		log.append(", @RecDate = " + request.getrecDate());

		qb.append(", @PaymentDate = ?");
		log.append(", @PaymentDate = " + request.getpayDate());

		qb.append(", @Currency = ?");
		log.append(", @Currency = " + request.getcurrency());
		
		qb.append(", @DivId = ?");
		log.append(", @DivId = " + request.getdivId());
		
		qb.append(", @DivSource = ?");
		log.append(", @DivSource = " + request.getdivSource());
		
		
		qb.append(") } ");
		log.append(") } ");
		logger.info("Executing query"+log.toString());
		return qb.toString();
	}
	
	private void setParameters(GetDividendSpillRequest request,
			CallableStatement cstmt) throws Exception {
	    
		StringBuilder sb = new StringBuilder();
	    if (request.getunderlyingId() < 0) {
	    	sb.append("\n\r underlyingId must be set");
	    }
	    if (request.getinstrId() < 0) {
	    	sb.append("\n\r instrId must be set");
	    }
	    if (request.getstatus() < 0) {
	    	sb.append("\n\r status must be set");
	    }
	    if (request.getexDate() == null || request.getexDate().getTime() < 0) {
	    	sb.append("\n\r ExDate must be set");
	    }
        if (request.getcurrency() == null) {
        	sb.append("\n\r currenty must be set");
        }
        if (request.getdivId() < 0) {
	    	sb.append("\n\r divId must be set");
	    }

        if (sb.toString().length() > 0) {
        	throw new Exception (sb.toString());
        }
		
		int i = 0;
		
		// feedDividendRef
		//modified the following condition to consider single stock dividends whose feedID is null(SYN-2638)
		if((request.getfeedId()== null)||((request.getfeedId().length())==0) || request.getfeedId().isEmpty()){
			cstmt.setString(++i, null);
		} else {
			cstmt.setString(++i, request.getfeedId());
		}
			
		// underlyingId
		cstmt.setInt(++i, request.getunderlyingId());
			
		// instrId
		cstmt.setInt(++i, request.getinstrId());
			
		// Status
		cstmt.setInt(++i, request.getstatus());
			
		// ExDate
		cstmt.setDate(++i, new java.sql.Date(request.getexDate().getTime()));
	    cstmt.setDate(++i, new java.sql.Date(request.getrecDate().getTime()));
		// PaymentDate
	       cstmt.setDate(++i, new java.sql.Date(request.getpayDate().getTime()));
	   
		// Currency
		cstmt.setString(++i, request.getcurrency());
		// DivId
		cstmt.setInt(++i, request.getdivId());
		// Div source
		cstmt.setString(++i, request.getdivSource());
	}	


	
	private DividendSpill populateDividendSpill(ResultSet rs, int location) throws SQLException {
		
		
		DividendSpill dividendspill = new DividendSpill();
			int i = 0;
			dividendspill.setessLocation(Integer.toString(location));
			dividendspill.setticker(rs.getString(++i));
			dividendspill.setconstTicker(rs.getString(++i));
			dividendspill.setdividendSource(rs.getString(++i));
			dividendspill.setdivCcy(rs.getString(++i));
			
			Timestamp exDateTimeStamp = rs.getTimestamp(++i);
			Date exDate = exDateTimeStamp == null ? null : new Date(
			exDateTimeStamp.getTime());
			dividendspill.setexDate(exDate);
			
			Timestamp recDateTimeStamp = rs.getTimestamp(++i);
			Date recDate = recDateTimeStamp == null ? null : new Date(
			recDateTimeStamp.getTime());
			dividendspill.setrecDate(recDate);
			
			Timestamp payDateTimeStamp = rs.getTimestamp(++i);
			Date payDate = payDateTimeStamp == null ? null : new Date(
			payDateTimeStamp.getTime());
			dividendspill.setpayDate(payDate);
			
			Timestamp annDateTimeStamp = rs.getTimestamp(++i);
			Date annDate = annDateTimeStamp == null ? null : new Date(
			annDateTimeStamp.getTime());
			dividendspill.setannDate(annDate); 
			
			dividendspill.setgross(rs.getFloat(++i));
			dividendspill.setnet(rs.getFloat(++i));
			dividendspill.setuserComment(rs.getString(++i));
			dividendspill.setuserComment(rs.getString(++i));
			dividendspill.setunderlyingId(rs.getInt(++i));
			dividendspill.setindexId(rs.getInt(++i));
			dividendspill.setfeedId(rs.getString(++i));
			dividendspill.setstatus(rs.getInt(++i));
			dividendspill.setdivId(rs.getInt(++i));
			dividendspill.setspillType(rs.getString(++i));
			dividendspill.setuserIdCreate(rs.getInt(++i));
			dividendspill.setuserNameCreate(rs.getString(++i));
			
			return dividendspill;
		
		}
	

}




