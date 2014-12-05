package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.constants.IConstants;
import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.ExceptionActionRequest;
import com.db.ess.synthesis.dvo.ExceptionActionResponse;
import com.db.ess.synthesis.dvo.SynthesisException;
import com.db.ess.synthesis.util.SQLUtils;

public class ExceptionActionTask extends BaseExecuteDaoTask<ExceptionActionResponse> 
{
	private static final Logger logger = Logger.getLogger(ExceptionActionTask.class.getName());
	public List<SynthesisException> requestList = null;
	public ExceptionActionRequest request = null;
	public String action = null;
	public boolean isTrader= false;
	public int userId = 0;
	//public Date expCloseTime = null;
	//public String expComments = null;
	private static final String CLOSE_EXCEPTIONS = "{ call dbo.SYN_CloseException( @ExceptId = ? ," +
			"@UserId = ? , @IsDividend = ? , @ReasonComment = ?, @IsTrader = ?, @ExpCloseTime = ? ) }";
	private static final String APPROVE_EXCEPTIONS = "{ call dbo.SYN_ApproveException( @ExceptId = ? ," +
			"@UserId = ? ,@IsDividend = ? , @ReasonComment = ?) }";
	private static final String REJECT_EXCEPTIONS = "{ call dbo.SYN_RejectException( @ExceptId = ? ," +
			"@UserId = ? ,@IsDividend = ? , @ReasonComment = ?) }";
	
	// ReRate Exception is moved to ExceptionReRateTask to handle Bulk ReRate
	/*private static final String RERATE_EXCEPTIONS = "{ call dbo.SYN_ReRateException( @OrigExceptId = ? ," +
			"@UserId = ? , @IsDividend = ? ,@ReasonComment = ? , @IsTrader = ?, @RateValue1 = ? " +
			", @RateValue2 = ? , @StartDate = ? , @EndDate = ? ,@NewExceptId = ?,@Status = ?) }";*/
	
	/*
	public ExceptionActionTask(List<SynthesisException>  req, int location,String action,boolean isTrader, int userId,Date expCloseTime,String expComments)
	{
		super(location);
		this.requestList = req;
		this.action = action;
		this.isTrader = isTrader;
		this.userId = userId;
		this.expCloseTime = expCloseTime;
		this.expComments = expComments;
	}
	*/
	
	public ExceptionActionTask(ExceptionActionRequest  req, int location)
	{
		super(location);
		this.requestList = getExceptionObjects(req);
		this.request = req;
		this.action = req.getaction();
		this.isTrader = req.getisTrader();
		this.userId = req.getuserId();
		//this.expCloseTime = req.getexpCloseTime();
		//this.expComments = req.getexpComments();
	}
	
	
	@Override
	protected PreparedStatement[] createStatement(Connection c) throws SQLException 
	{
		List<PreparedStatement> psList = new ArrayList<PreparedStatement>();
		CallableStatement cstmt = null;
		
		String query = getQuery(action);
		for(SynthesisException se : requestList)
		{
			logger.debug("Inside set parameters" + this.userId + se.getexceptId()+this.isTrader);
			cstmt = c.prepareCall(query);
			setParameters(se, cstmt);
			psList.add(cstmt);
		}
		//logger.info("Successfully INSERT/UPDATE/DELETE in " + query);
		
		return  (PreparedStatement[])psList.toArray(new PreparedStatement[psList.size()]);
	}
	
	@Override
	public void run(ExceptionActionResponse res) throws Exception {
		logger.info("Ellapsed time: inside run method"); 
		
		Connection conn = null;
		PreparedStatement[] stmts = null;
		long time = System.currentTimeMillis(); 
		try {
			conn = SQLUtils.getConnection(location);
			// Setting it to false to maintain atomicity of the transaction
			conn.setAutoCommit(false);
			stmts = createStatement(conn);
            for(PreparedStatement s : stmts) {
            	logger.info("Executing Statemnet : "+s);
            	s.executeUpdate();
            }
			conn.commit();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			logger.info(">>> Successfully INSERT/UPDATE/DELETE in " + location);
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			conn.rollback();
			res.setisActionSuccess(false);
			processResult(res);// this method is to capture any errors
			//throw ex;			
		} finally {
			SQLUtils.closeResources(conn, stmts, null);
		}
	}
	
	public void processResult(ExceptionActionResponse res)
	{
		//res.setisUploadSuccess(!requestHasError);
		if(!res.getisActionSuccess())
		{
			for(SynthesisException se:requestList)
			{
				res.setexceptions(se);
			}	
		}
	}
	
	public String getQuery(String action)
	{
		String query = "";
		if(action == null || action.isEmpty())
			return query;
		else if(action.equalsIgnoreCase(IConstants.CLOSE))
			query= CLOSE_EXCEPTIONS;
		else if (action.equalsIgnoreCase(IConstants.APPROVE))
			query = APPROVE_EXCEPTIONS;
		else if (action.equalsIgnoreCase(IConstants.REJECT))
			query =  REJECT_EXCEPTIONS;
		/*else if (action.equalsIgnoreCase(IConstants.RERATE))
			query =  RERATE_EXCEPTIONS;*/
		logger.info("Ellapsed time: inside run method"+query);
		
		return query;
	}
	
	public void setParameters(SynthesisException se, PreparedStatement cstmt) throws SQLException
	{
		int i =0;
		cstmt.setInt(++i, se.getexceptId());
		cstmt.setInt(++i, this.userId);
		cstmt.setInt(++i, se.gettype().contains("ivi")?1:0);
		//cstmt.setString(++i, request.getexpComments());
		cstmt.setString(++i, se.getreasonComment());
		setParamsForAction(cstmt,action,i);
		if(action != null && !"".equals(action) && action.equalsIgnoreCase(IConstants.CLOSE))
		{
			cstmt.setInt(++i, this.isTrader?1:0);
			cstmt.setDate(++i, request.getexpCloseTime()==null?null:new java.sql.Date(request.getexpCloseTime().getTime())); // put Null check	
		}
		logger.info("Inside set parameters userId " + this.userId +", exceptId :" + se.getexceptId()+" , isTrader : "+this.isTrader
				+ " isDividend: "+se.gettype().contains("ivi") + " Comments as :"+se.getreasonComment());
		logger.info("Start Date:"+ request.getreStartDate());
		/*if(action.equalsIgnoreCase(IConstants.RERATE))
		{
			cstmt.setShort(11, se.getstatus());
			logger.info("Status"+ se.getstatus());
		}*/
		
	}
	
	public void setParamsForAction(PreparedStatement cstmt, String action,int i) throws SQLException
	{
		/*if(action.equalsIgnoreCase(IConstants.RERATE))
		{
			cstmt.setInt(++i, this.isTrader?1:0);
			cstmt.setDouble(++i, request.getreValue1());
			cstmt.setDouble(++i, request.getreValue2());
			cstmt.setDate(++i, request.getreStartDate()==null?null:new java.sql.Date(request.getreStartDate().getTime()));
			cstmt.setDate(++i, request.getreEndDate()==null?null:new java.sql.Date(request.getreEndDate().getTime()));
			List<Integer> lExceptIdList = new IdGeneratorTask(location, 1, 100, 1).run();
			cstmt.setInt(++i, lExceptIdList.size()>0?lExceptIdList.get(0):null);
		}*/
	}
	
	public boolean nullEmptyCheck(String toCheck)
	{
		if ((toCheck != null && !"".equals(toCheck)))
			return true;
		else 
			return false;
	}
	
	public List<SynthesisException> getExceptionObjects(ExceptionActionRequest request)
	{
		List<SynthesisException> reqList = new ArrayList<SynthesisException>();
		SynthesisException se = null;
		if(request.getexceptionsIterator()!=null)
		{
			Iterator itr = request.getexceptionsIterator();
			while(itr.hasNext())
			{
				se = (SynthesisException)itr.next();
				reqList.add(se);
			}	
			
		}
		return reqList;
	}

}
