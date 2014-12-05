package com.db.ess.synthesis.dao.tasks.securityId;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dvo.GetSecurityIdMappingRequest;
import com.db.ess.synthesis.dvo.GetSecurityIdMappingResponse;
import com.db.ess.synthesis.util.SQLUtils;

public class GetSecurityIdMappingTask extends
		BaseQueryDaoTask<GetSecurityIdMappingResponse> {

	private static final Logger logger = Logger
			.getLogger(GetSecurityIdMappingTask.class.getName());

	private GetSecurityIdMappingRequest request;
	public static final String SELECT_ID_MAPPINGS = "select rtrim(ric_cde), rtrim(bb_ticker), currency from symbols_extract "
			+ "where ric_cde in ( ? ) " + " or bb_ticker in ( ? ) ";
	public static final String SEPARATOR = "|";

	public GetSecurityIdMappingTask(GetSecurityIdMappingRequest req, int loc) {
		super(loc);
		this.request = req;
	}

	@Override
	protected Statement createStatement(Connection c) throws SQLException {
		String constituents="''";
		Iterator it= request.getsecurityIdIterator();
		while(it.hasNext()){
			constituents += (",'"+it.next()+"'");
		}
		String SELECT_ID_MAPPINGS_Query = new String(SELECT_ID_MAPPINGS).replace("?", constituents);
		CallableStatement cstmt = c.prepareCall(SELECT_ID_MAPPINGS_Query);
		logger.info(">>> running " + SELECT_ID_MAPPINGS_Query );
		return cstmt;
	}

	@Override
	protected ResultSet runStatement(Statement stmt) throws SQLException {
		
		return ((PreparedStatement) stmt).executeQuery();
	}

	@Override
	protected int processResult(GetSecurityIdMappingResponse res, ResultSet rs)
			throws Exception {
		int count = 0;
		logger.info(">>> process data result for Ric and Ticker....");
		while (rs.next()) {

			res.setsecurityIdMapping(rs.getString(1) + SEPARATOR
					+ rs.getString(2)+ SEPARATOR +rs.getString(3));
			count++;
		}
		return count;
	}

	@Override
	public void run(GetSecurityIdMappingResponse res) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		long time = System.currentTimeMillis();
		try {
			conn = TaskHelper.getConnection();
			stmt = createStatement(conn);
			stmt.setQueryTimeout(300);
			rs = runStatement(stmt);

			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time));
			int count = processResult(res, rs);
			logger.info("Found [" + count + "] rows ");
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			throw ex;
		} finally {

			SQLUtils.closeResources(conn, stmt, rs);
		}

	}

}
