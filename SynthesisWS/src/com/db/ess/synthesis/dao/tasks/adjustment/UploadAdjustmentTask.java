package com.db.ess.synthesis.dao.tasks.adjustment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dao.tasks.exception.IdGeneratorTask;
import com.db.ess.synthesis.dvo.Adjustment;
import com.db.ess.synthesis.dvo.UploadAdjustmentRequest;
import com.db.ess.synthesis.dvo.UploadAdjustmentResponse;
import com.db.ess.synthesis.statement.DeleteAdjCreateStatement;
import com.db.ess.synthesis.statement.NewAdjCreateStatement;
import com.db.ess.synthesis.statement.UpdateAdjCreateStatement;
import com.db.ess.synthesis.util.SQLUtils;

//import edu.emory.mathcs.backport.java.util.concurrent.PriorityBlockingQueue;

public class UploadAdjustmentTask extends BaseExecuteDaoTask<UploadAdjustmentResponse>
{
	private static final Logger logger = Logger.getLogger(UploadAdjustmentTask.class.getName());
	List<Adjustment> reqList = null;
	boolean requestHasError;
	public int userId;
	public int eventId;
	
	public UploadAdjustmentTask(UploadAdjustmentRequest req, int location)
	{
		super(location);
		requestHasError = false;
		this.userId = req.getuserId();
		reqList = new ArrayList<Adjustment>();
		
		if(req.getadjustmentsIterator()!=null)
		{
			AdjustmentsValidator adjValid = null;
			Iterator itr = req.getadjustmentsIterator();
			while(itr.hasNext())
			{
				adjValid = new AdjustmentsValidator((Adjustment)itr.next(), location, req.getisTrader());
				adjValid.performValidation();
				if(!requestHasError)
					requestHasError = adjValid.adjustmentHasError();
				
				reqList.add(adjValid.getAdjustmentObject());
			}	
			
		}
		
	}

	@Override
	protected PreparedStatement[] createStatement(Connection c) throws SQLException 
	{
		if(requestHasError)
			return null;
		List<PreparedStatement> psList = new ArrayList<PreparedStatement>();
		// Setting it to false to maintain atomicity of the transaction
		c.setAutoCommit(false);
		List<Adjustment> updateList = new ArrayList<Adjustment>();
		List<Adjustment> createList = new ArrayList<Adjustment>();
		List<Adjustment> deleteList = new ArrayList<Adjustment>();
		for(Adjustment ad : reqList)
		{
			if(ad.getAction().startsWith("New"))
				createList.add(ad);
			else if(ad.getAction().startsWith("Am") && ad.getEventId() > 0)
				updateList.add(ad);
			else if(ad.getAction().startsWith("Del") && ad.getEventId() > 0)
				deleteList.add(ad);
			else
			{
				requestHasError=true;
				break;
			}
				
		}
		
		if(createList.size() > 0 || !createList.isEmpty())
		{
			List<Integer> eventIdList = new IdGeneratorTask(location, 1, 2054, createList.size()).run();
			//PriorityBlockingQueue idQ = new PriorityBlockingQueue(eventIdList);
			//setUpAdjustIds(createList,idQ);
			psList.addAll(new NewAdjCreateStatement(c,createList,userId).createStatement());
		}
		
		psList.addAll(new UpdateAdjCreateStatement(c, updateList,userId).createStatement());
		psList.addAll(new DeleteAdjCreateStatement(c, deleteList).createStatement());
		
		return  (PreparedStatement[])psList.toArray(new PreparedStatement[psList.size()]);
	}
	
	public void run(UploadAdjustmentResponse res) throws Exception {
		if(requestHasError)
		{
			for(Adjustment ad:reqList)
			{
				res.setadjustments(ad);
			}
			res.setisUploadSuccess(false);
			logger.info("isUploadSuccess : "+res.getisUploadSuccess());
			return;
		}
			
		Connection conn = null;
		PreparedStatement[] stmts = null;
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			stmts = createStatement(conn);
            for(PreparedStatement s : stmts) {
            	s.executeUpdate();
            }
			conn.commit();
			res.setisUploadSuccess(true);
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			logger.info(">>> Successfully INSERT/UPDATE/DELETE in " + location);
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			conn.rollback();
			res.setisUploadSuccess(false);
			throw ex;			
		} finally {
			SQLUtils.closeResources(conn, stmts, null);
		}
		//Handle cases where database might throw errors
		processResult(res);
	}
	
	public void processResult(UploadAdjustmentResponse res)
	{
		//res.setisUploadSuccess(!requestHasError);
		if(!res.getisUploadSuccess() || requestHasError)
		{
			for(Adjustment ad:reqList)
			{
				res.setadjustments(ad);
			}	
		}
	}
/*	
	public void setUpAdjustIds(List<Adjustment> listA,PriorityBlockingQueue idQ)
	{
		if(listA!=null && !listA.isEmpty() && idQ!=null && !idQ.isEmpty())
		{
			for(Adjustment ad : listA)
			{
				eventId = ((Integer)idQ.poll()).intValue();
				++eventId;
				//logger.info("Event Id Generated :: "+eventId);
				ad.setEventId(eventId);
			}
			logger.info("Successfully updated eventIds: ");
		}
	}
*/	
}
