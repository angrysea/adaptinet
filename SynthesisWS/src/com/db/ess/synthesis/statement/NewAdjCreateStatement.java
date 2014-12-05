package com.db.ess.synthesis.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.Adjustment;

public class NewAdjCreateStatement extends BaseCreateStatement 
{
	public String INSERT_ADJUSTMENT="DECLARE @legId int "
		+ "SELECT @legId=max(fiId) " 
		+ "FROM SwapLeg "
		+ "WHERE parentId = ? AND structureType=0 " 
		+ "INSERT INTO BasketEvent (eventId, legId, instrId, tradeDate, settleDate, qty, price, basePrice, fxRate, intRate, eventType, enterUser, enterTime) "
		+ "SELECT ?, sl.fiId, ?, ?, ?, 1, ?, ?, 1, 0, ?, ?, GETDATE() "
		+ "FROM SwapLeg sl, Swap s " 
		+ "WHERE s.fiId = ? "
		+ "AND s.fiId=sl.parentId "
		+ "AND sl.fiId=@legId";
	public int userId;
	
	private static final Logger logger = Logger.getLogger(NewAdjCreateStatement.class.getName());
	List<Adjustment> adList;
	
	public NewAdjCreateStatement(Connection conn, List<Adjustment> adList, int userId)
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
			logger.info(">>> Created INSERT query in " + INSERT_ADJUSTMENT);
			stmt = conn.prepareStatement(INSERT_ADJUSTMENT);
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
		 if(getIntValueFromReq(ad.getSwapId())!=null)
			 stmt.setInt(i++, ad.getSwapId());
		 if(getIntValueFromReq(ad.getEventId())!=null)
			 stmt.setInt(i++, ad.getEventId());
		 if(getIntValueFromReq(ad.getInstrumentId())!=null)
			 stmt.setInt(i++, ad.getInstrumentId());
		 stmt.setDate(i++, getDateValueFromReq(ad.getTradeDate())==null?null:
				new java.sql.Date(ad.getTradeDate().getTime()));
		 stmt.setDate(i++, getDateValueFromReq(ad.getSettleDate())==null?null:
				new java.sql.Date(ad.getSettleDate().getTime()));
		 stmt.setDouble(i++, getDoubleValueFromReq(ad.getPrice())==null?0:getDoubleValueFromReq(ad.getPrice()));
		 stmt.setDouble(i++, getDoubleValueFromReq(ad.getPrice())==null?0:getDoubleValueFromReq(ad.getPrice()));
		 stmt.setInt(i++, ad.getTradeTypeId());
		 stmt.setInt(i++, userId);
		 if(getIntValueFromReq(ad.getSwapId())!=null)
			 stmt.setInt(i++, ad.getSwapId());
		
		logger.info("Number of params set in INSERT query for userId:"+userId+" >>> " + i);
		return stmt;
	}

}
