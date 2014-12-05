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

import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.ExceptionReRateRequest;
import com.db.ess.synthesis.dvo.ExceptionReRateResponse;
import com.db.ess.synthesis.dvo.ReRateExceptionParams;
import com.db.ess.synthesis.util.SQLUtils;

public class ExceptionReRateTask extends BaseExecuteDaoTask<ExceptionReRateResponse> 
{
	private static final Logger logger = Logger.getLogger(ExceptionReRateTask.class.getName());
	public List<ReRateExceptionParams> requestList = null;
	public ExceptionReRateRequest request = null;
	public boolean isTrader= false;
	public int userId = 0;
	//public Date expCloseTime = null;
	//public String expComments = null;
	private static final String RERATE_EXCEPTIONS = "{ call dbo.SYN_ReRateException( @OrigExceptId = ? ," +
			"@UserId = ? , @IsDividend = ? ,@ReasonComment = ? , @IsTrader = ?, @RateValue1 = ? " +
			", @RateValue2 = ? , @StartDate = ? , @EndDate = ? ,@NewExceptId = ?,@Status = ?) }";
	
	
	public ExceptionReRateTask(ExceptionReRateRequest  req, int location)
	{
		super(location);
		this.requestList = getExceptionObjects(req);
		this.request = req;
		this.isTrader = req.getisTrader();
		this.userId = req.getuserId();
		//this.exceptType = req.getexceptionType();
		//this.expCloseTime = req.getexpCloseTime();
		//this.expComments = req.getexpComments();
	}
	
	
	@Override
	protected PreparedStatement[] createStatement(Connection c) throws SQLException 
	{
		List<PreparedStatement> psList = new ArrayList<PreparedStatement>();
		CallableStatement cstmt = null;
		
		String query = getQuery();
		for(ReRateExceptionParams re : requestList)
		{
			logger.debug("Inside set parameters" + this.userId + re.getexceptId()+this.isTrader);
			cstmt = c.prepareCall(query);
			setParameters(re, cstmt);
			psList.add(cstmt);
		}
		//logger.info("Successfully INSERT/UPDATE/DELETE in " + query);
		
		return  (PreparedStatement[])psList.toArray(new PreparedStatement[psList.size()]);
	}
	
	@Override
	public void run(ExceptionReRateResponse res) throws Exception {
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
			res.setisActionSuccess(true);
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			logger.info(">>> Successfully INSERT/UPDATE/DELETE in " + location);
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			conn.rollback();
			res.setisActionSuccess(false);
			//processResult(res);// this method is to capture any errors
			//throw ex;			
		} finally {
			SQLUtils.closeResources(conn, stmts, null);
		}
	}
	
	/*public void processResult(ExceptionReRateResponse res)
	{
		//res.setisUploadSuccess(!requestHasError);
		if(!res.getisActionSuccess())
		{
			for(SynthesisException se:requestList)
			{
				res.setexceptions(se);
			}	
		}
	}*/
	
	public String getQuery()
	{
		String query = RERATE_EXCEPTIONS;
		logger.info("Ellapsed time: inside run method"+query);
		
		return query;
	}
	
	public void setParameters(ReRateExceptionParams re, PreparedStatement cstmt) throws SQLException
	{
		int i =0;
		cstmt.setInt(++i, re.getexceptId());
		cstmt.setInt(++i, this.userId);
		cstmt.setInt(++i, re.getexceptionType().contains("ivi")?1:0);
		cstmt.setString(++i, re.getcomment());
		//setParamsForAction(cstmt,action,i);
		cstmt.setInt(++i, this.isTrader?1:0);
		if(re.getexceptionType().contains("ivi"))
		{
			cstmt.setDouble(++i, re.gettradeRate());
			cstmt.setDouble(++i, re.getsettleRate());
		}
		else
		{
			cstmt.setDouble(++i, re.getspread());
			cstmt.setDouble(++i, re.getrate());
		}
		
		cstmt.setDate(++i, re.getstartDate()==null?null:new java.sql.Date(re.getstartDate().getTime()));
		cstmt.setDate(++i, re.getendDate()==null?null:new java.sql.Date(re.getendDate().getTime()));
		List<Integer> lExceptIdList = new IdGeneratorTask(location, 1, 600, 1).run();
		cstmt.setInt(++i, lExceptIdList.size()>0?lExceptIdList.get(0):null);
		logger.info("Inside set parameters userId " + this.userId +", exceptId :" + re.getexceptId()+" , isTrader : "+this.isTrader
				+ " isDividend: "+re.getexceptionType().contains("ivi") + " Comments as :"+re.getcomment());
		logger.info("Start Date:"+ re.getstartDate());

		cstmt.setShort(11, re.getstatus());
		logger.info("Status"+ re.getstatus());
				
	}
	
	/*public void setParamsForAction(PreparedStatement cstmt, String action,int i) throws SQLException
	{
		if(action.equalsIgnoreCase(IConstants.RERATE))
		{
			cstmt.setInt(++i, this.isTrader?1:0);
			cstmt.setDouble(++i, request.getreValue1());
			cstmt.setDouble(++i, request.getreValue2());
			cstmt.setDate(++i, request.getreStartDate()==null?null:new java.sql.Date(request.getreStartDate().getTime()));
			cstmt.setDate(++i, request.getreEndDate()==null?null:new java.sql.Date(request.getreEndDate().getTime()));
			List<Integer> lExceptIdList = new IdGeneratorTask(location, 1, 100, 1).run();
			cstmt.setInt(++i, lExceptIdList.size()>0?lExceptIdList.get(0):null);
		}
	}*/
	
	public boolean nullEmptyCheck(String toCheck)
	{
		if ((toCheck != null && !"".equals(toCheck)))
			return true;
		else 
			return false;
	}
	
	public List<ReRateExceptionParams> getExceptionObjects(ExceptionReRateRequest request)
	{
		List<ReRateExceptionParams> reqList = new ArrayList<ReRateExceptionParams>();
		ReRateExceptionParams re = null;
		if(request.getreRateExceptionParamsIterator()!=null)
		{
			Iterator itr = request.getreRateExceptionParamsIterator();
			while(itr.hasNext())
			{
				re = (ReRateExceptionParams)itr.next();
				reqList.add(re);
			}	
			
		}
		return reqList;
	}

}
