package com.db.ess.synthesis.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.Adjustment;

public class DeleteAdjCreateStatement extends BaseCreateStatement 
{
	public String DELETE_ADJUSTMENT="DELETE BasketEvent WHERE eventId = ? AND eventType = ?";
	
	private static final Logger logger = Logger.getLogger(DeleteAdjCreateStatement.class.getName());
	List<Adjustment> adList;
	
	public DeleteAdjCreateStatement(Connection conn, List<Adjustment> adList)
	{
		super(conn);
		this.adList = adList;
	}
	
	@Override
	public List<PreparedStatement> createStatement() throws SQLException 
	{
		if(adList==null || adList.isEmpty() || conn==null)
			return Collections.EMPTY_LIST;
		List<PreparedStatement> listPS = new ArrayList<PreparedStatement>(); 
		PreparedStatement stmt = null;

		for (Adjustment ad : adList)
		{
			logger.info(">>> Created DELETE query in " + DELETE_ADJUSTMENT);
			stmt = conn.prepareStatement(DELETE_ADJUSTMENT);
			stmt = setParamsForStatement(ad,stmt);
			listPS.add(stmt);
			
		}
		
		return listPS;
	
	}
	
	public PreparedStatement setParamsForStatement(Adjustment ad, PreparedStatement stmt) throws SQLException
	{
		if(stmt==null)
			return null;
		 int i =1;
		 if(getIntValueFromReq(ad.getEventId())!=null)
			 stmt.setInt(i++, ad.getEventId());
		 stmt.setInt(i++, ad.getTradeTypeId());
		 
		logger.info("Number of params set in DELETE query >>> " + i +" for eventId: "+ad.getEventId());
		return stmt;
	}

}
