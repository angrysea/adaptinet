package com.db.ess.synthesis.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.Adjustment;

public class UpdateAdjCreateStatement extends BaseCreateStatement 
{
	public String UPDATE_ADJUSTMENT="update BasketEvent "
		+ "set instrId = ?," 
		+ "tradeDate = ?," 
		+ "settleDate = ?," 
		+ "price = ?," 
		+ "basePrice = ?," 
		+ "enterUser = ?," 
		+ "enterTime = GETDATE() "
		+ "WHERE eventId = ? AND eventType = ?";
	public int userId;
	
	private static final Logger logger = Logger.getLogger(UpdateAdjCreateStatement.class.getName());
	List<Adjustment> adList;
	
	public UpdateAdjCreateStatement(Connection conn, List<Adjustment> adList, int userId)
	{
		super(conn);
		this.adList = adList;
		this.userId=userId;
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
			logger.info(">>> Created UPDATE query in " + UPDATE_ADJUSTMENT);
			stmt = conn.prepareStatement(UPDATE_ADJUSTMENT);
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
		 if(getIntValueFromReq(ad.getInstrumentId())!=null)
			 stmt.setInt(i++, ad.getInstrumentId());
		 stmt.setDate(i++, getDateValueFromReq(ad.getTradeDate())==null?null:
				new java.sql.Date(ad.getTradeDate().getTime()));
		 stmt.setDate(i++, getDateValueFromReq(ad.getSettleDate())==null?null:
				new java.sql.Date(ad.getSettleDate().getTime()));
		 stmt.setDouble(i++, getDoubleValueFromReq(ad.getPrice())==null?0:getDoubleValueFromReq(ad.getPrice()));
		 stmt.setDouble(i++, getDoubleValueFromReq(ad.getPrice())==null?0:getDoubleValueFromReq(ad.getPrice()));
		 stmt.setInt(i++, userId);
		 if(getIntValueFromReq(ad.getEventId())!=null)
			 stmt.setInt(i++, ad.getEventId());
		 stmt.setInt(i++, ad.getTradeTypeId());
		 
		logger.info("Number of params set in UPDATE query >>> " + i +" for eventId: "+ad.getEventId());
		return stmt;
	}

}
