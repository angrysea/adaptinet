package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.constants.IConstants;
import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.CreateExceptionRequest;
import com.db.ess.synthesis.dvo.CreateExceptionResponse;
import com.db.ess.synthesis.dvo.ResponseParams;
import com.db.ess.synthesis.dvo.TickerLevelParams;
import com.db.ess.synthesis.util.SQLUtils;

public class CreateExceptionTask extends BaseExecuteDaoTask<CreateExceptionResponse>{
	
	private static final Logger logger = Logger.getLogger(CreateExceptionTask.class.getName());
	private CreateExceptionRequest request;
	public List<TickerLevelParams> tickerParamsList = null;
	//public List<ExceptionIDParams> exceptionIdList = null;
	//public List<ResponseParams> responseParamsList = null;
	ResponseParams responseParams;
	
	private static final String INSERT_DIVIDEND_EXCEPTION = "insert into DividendException (startDate,endDate,userId,enterTime,exceptId,manual,endDateInput,longShort,requestor,reasonId,reasonComment,actionedByUserId,tradeRate,settleRate";
	private static final String INSERT_SPREAD_EXCEPTION= "insert into    SpreadException   (startDate,endDate,userId,enterTime,exceptId,manual,endDateInput,longShort,requestor,reasonId,reasonComment,actionedByUserId,spread,rate";
	private static final String INSERT_Values_Part = " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
			" ?, ?" ; // for spread/rate and trade/settle
	private static final String INSERT_DIVIDEND_EXCEPTION_PENDING = "insert into DividendExceptionPending (startDate,endDate,userId,enterTime,exceptId,manual,endDateInput,longShort,requestor,reasonId,reasonComment,actionedByUserId,tradeRate,settleRate,status";
	private static final String INSERT_SPREAD_EXCEPTION_PENDING= "insert into    SpreadExceptionPending   (startDate,endDate,userId,enterTime,exceptId,manual,endDateInput,longShort,requestor,reasonId,reasonComment,actionedByUserId,spread,rate,status";
	private static final String INSERT_Values_Part_Pending = " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
	" ?, ?, ?" ; // for spread/rate, trade/settle and status
	private static final String Separator = ", ";
	private static final String ValueHolder = "?";
	private  static final String Query_Closing = ")";
	private boolean isTrader;
	private int errCount = 0;
	//private int institutionId = 0;
	//private int legalEntityId = 0;
	private int swapNum = 0;
	private int dbLocation = 1;
	private boolean isRowExist = false;
	private String[] swapArr;
	

	public CreateExceptionTask(CreateExceptionRequest req, int location) {
		super(location);
		this.request = req;
		this.tickerParamsList = getTickerParams(req);
		this.isTrader = req.getisTrader();
	}


	
	protected PreparedStatement[] createStatement(Connection c) throws SQLException
	{
		List<PreparedStatement> psList = new ArrayList<PreparedStatement>();
		PreparedStatement cstmt = null;
		
		String query = getQuery();
		for(TickerLevelParams tp : tickerParamsList)
		{
				if(!request.getexceptionType().contains("ivi"))
				{
					List<TickerLevelParams> tList = getParamList(tp);
					if(tList != null)
					{
						for(TickerLevelParams tpl : tList)
						{
							cstmt = c.prepareStatement(query);
							setParameters(tpl, cstmt);
							psList.add(cstmt);
						}
					}
				}
				else
				{
					if(tp.gettradeRate() != IConstants.noValue || tp.getsettleRate() != IConstants.noValue)
					{
						cstmt = c.prepareStatement(query);
						setParameters(tp, cstmt);
						psList.add(cstmt);
					}
				}
			}
		
		return  (PreparedStatement[])psList.toArray(new PreparedStatement[psList.size()]);
	}
	
	
	private List<TickerLevelParams> getParamList(TickerLevelParams tickParam)
	{
		List<TickerLevelParams> tpList = new ArrayList<TickerLevelParams>();
		TickerLevelParams tickParamSplit;
		double rate = tickParam.getrate();
		double spread = tickParam.getspread();

			if(rate != IConstants.noValue && spread != IConstants.noValue)
		    {
				for(int i=1; i<=2; i++)
				{
					tickParamSplit = new TickerLevelParams();
					tickParamSplit.setticker(tickParam.getticker());
					tickParamSplit.setside(tickParam.getside());
					tickParamSplit.setstartDate(tickParam.getstartDate());
					tickParamSplit.setendDate(tickParam.getendDate());
					tickParamSplit.setreason(tickParam.getreason());
					tickParamSplit.setcomment(tickParam.getcomment());
					if(i==1)
					{
						tickParamSplit.setspread(0);
						tickParamSplit.setrate(rate);
					}else
					{
						tickParamSplit.setspread(1);
						tickParamSplit.setrate(spread);
					}
					tpList.add(tickParamSplit);
				}
		    }
		    else if(rate != IConstants.noValue)
		    {
		    	tickParamSplit = new TickerLevelParams();
				tickParamSplit.setticker(tickParam.getticker());
				tickParamSplit.setside(tickParam.getside());
				tickParamSplit.setstartDate(tickParam.getstartDate());
				tickParamSplit.setendDate(tickParam.getendDate());
				tickParamSplit.setreason(tickParam.getreason());
				tickParamSplit.setcomment(tickParam.getcomment());
				tickParamSplit.setspread(0);
				tickParamSplit.setrate(rate);
				tpList.add(tickParamSplit);
		    }
		    else if(spread != IConstants.noValue)
		    {
		    	tickParamSplit = new TickerLevelParams();
				tickParamSplit.setticker(tickParam.getticker());
				tickParamSplit.setside(tickParam.getside());
				tickParamSplit.setstartDate(tickParam.getstartDate());
				tickParamSplit.setendDate(tickParam.getendDate());
				tickParamSplit.setreason(tickParam.getreason());
				tickParamSplit.setcomment(tickParam.getcomment());
				tickParamSplit.setspread(1);
				tickParamSplit.setrate(spread);
				tpList.add(tickParamSplit);
		    }
		    else
		    {
		    	logger.info("Invalid values in Rate/Spread columns");
		    	return null;
		    }
		
		return tpList;
	}


	public void processResult(CreateExceptionResponse res)
	{
		//res.setresponseParam(responseParams);
		Iterator<ResponseParams> itr = res.getresponseParamIterator();
		while(itr.hasNext())
		{
			ResponseParams resPar = itr.next();
			Iterator<Integer> itr1 = resPar.getexceptionIDParamIterator();
			while(itr1.hasNext())
			{
				logger.info("Response ExceptionId :: "+itr1.next());
			}
			//logger.info("Response ticker :: "+resPar.getticker());
			//logger.info("Response ticker error :: "+resPar.gettickerHasError());
			logger.info("Response success/fail :: "+resPar.getisActionAccess());
		}
	}

	public void run(CreateExceptionResponse res) throws Exception {
		logger.info("Ellapsed time: inside run method"); 
		
		Connection conn = null;
		PreparedStatement[] stmts = null;
		long time = System.currentTimeMillis();
		dbLocation = request.getlocation();
		swapArr = request.getswapNumber()!=null?(request.getswapNumber().length()>0?request.getswapNumber().split(","):null):null;
		
		try {
				CreateExceptionValidatorTask validatorObj = new CreateExceptionValidatorTask(request,res);
				res = validatorObj.validate();
				errCount = validatorObj.getErrorCount();
				//logger.info("No. of errors :: "+errCount);
				
				if(errCount == 0)
				{
					conn = SQLUtils.getConnection(dbLocation);
					// Setting it to false to maintain atomicity of the transaction
					conn.setAutoCommit(false);
					responseParams = new ResponseParams();
					responseParams.setisBTB(false);
					for(int i=0; ((swapArr==null || swapArr.length==0)?i<1:i<swapArr.length); i++)
					{
						if(swapArr!=null && swapArr[i] != null)
							setSwapNo(Integer.parseInt(swapArr[i]));

							stmts = createStatement(conn);
							for(PreparedStatement s : stmts) {
								logger.info("Executing Statement : "+s);
								isRowExist = true;
								s.executeUpdate();
							}
							conn.commit();
					}
					logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
					if(isRowExist == true)
					{
						logger.info(">>> Successfully INSERT/UPDATE/DELETE in " + dbLocation);
						responseParams.setisActionAccess(true);
						res.setresponseParam(responseParams);
					}else
					{
						logger.info(">>> Zero Rows INSERT/UPDATE/DELETE in " + dbLocation);
					}
				}
				
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			conn.rollback();
			responseParams.setisActionAccess(false);
			res.setresponseParam(responseParams);
		} finally {
			processResult(res);
			if(errCount == 0)
			SQLUtils.closeResources(conn, stmts, null);
		}
	}
	
	public String getQuery()
	{
		String query = "";
		if(request.getexceptionType().contains("ivi"))
			query= constructQuery(true,isTrader);
		else 
			query= constructQuery(false,isTrader);
		logger.info("Ellapsed time: inside run method "+query);
		
		return query;
	}
	
	public String constructQuery (boolean isDividend, boolean isTrader)
	{
		//String finalQuery = "";
		StringBuilder sbrQuery;
		StringBuilder sbrValues;
		if(isTrader)
		{
			sbrQuery = new StringBuilder(isDividend?INSERT_DIVIDEND_EXCEPTION:INSERT_SPREAD_EXCEPTION);
			sbrValues = new StringBuilder(INSERT_Values_Part);
		}
		else
		{
			sbrQuery = new StringBuilder(isDividend?INSERT_DIVIDEND_EXCEPTION_PENDING:INSERT_SPREAD_EXCEPTION_PENDING);
			sbrValues = new StringBuilder(INSERT_Values_Part_Pending);
		}
		
		
		if(request.getinstitution()>0)
		{
			sbrQuery.append(Separator);
			sbrQuery.append(" institutionId");
			sbrValues.append(Separator);
			sbrValues.append(ValueHolder);
		}
		if(getSwapNo()>0)
		{
			sbrQuery.append(Separator);
			sbrQuery.append(" swapId");
			sbrValues.append(Separator);
			sbrValues.append(ValueHolder);
		}
		if(request.getlegalEntity()>0)
		{
			sbrQuery.append(Separator);
			sbrQuery.append(" custId");
			sbrValues.append(Separator);
			sbrValues.append(ValueHolder);
		}
		if(request.getbookSeries()>0)
		{
			sbrQuery.append(Separator);
			sbrQuery.append(" bookSeriesId");
			sbrValues.append(Separator);
			sbrValues.append(ValueHolder);
		}
		if(request.getcountry()>0)
		{
			sbrQuery.append(Separator);
			sbrQuery.append(" countryId");
			sbrValues.append(Separator);
			sbrValues.append(ValueHolder);
		}
		if(tickerParamsList.size()>0)
		{
			sbrQuery.append(Separator);
			sbrQuery.append(" instrId");
			sbrValues.append(Separator);
			sbrValues.append(ValueHolder);
		}	
		
		sbrQuery.append(Query_Closing);
		sbrValues.append(Query_Closing);
		
		return sbrQuery.append(sbrValues).toString();
	}
	
	public void setParameters(TickerLevelParams se, PreparedStatement cstmt) throws SQLException
	{
		int i =0;

		cstmt.setDate(++i, se.getstartDate() == null?null:new java.sql.Date(se.getstartDate().getTime())); //startDate
		cstmt.setDate(++i, se.getendDate() == null?null:new java.sql.Date(se.getendDate().getTime())); //endDate
		cstmt.setInt(++i, request.getuserId());  //userId
		cstmt.setDate(++i,new java.sql.Date(new java.util.Date().getTime()));  //enterTime
		List<Integer> lExceptIdList = new IdGeneratorTask(request.getlocation(), 1, 600, 1).run();
		cstmt.setInt(++i, lExceptIdList.size()>0?lExceptIdList.get(0):null);  //exceptId
		//exceptionIds.append(":"+lExceptIdList.get(0));
		logger.info("New exception Id "+lExceptIdList.get(0));
		responseParams.setexceptionIDParam(lExceptIdList.get(0));
		cstmt.setInt(++i, 1);  //manual
		cstmt.setDate(++i, se.getendDate()==null?null:new java.sql.Date(se.getendDate().getTime())); //endDateInput
		cstmt.setInt(++i, se.getside());  //longShort
		cstmt.setString(++i, (request.getrequester()!=null?request.getrequester():("user"+request.getuserId())));  //requestor
		cstmt.setInt(++i, se.getreason());  //reasonId
		cstmt.setString(++i, se.getcomment());   //reasonComment
		cstmt.setInt(++i, request.getuserId());  //actionedByUserId
		
		/// check if exception type is Dividend or Spread(Finance/Rate) 
		if(request.getexceptionType().contains("ivi"))
		{
			cstmt.setDouble(++i, se.gettradeRate());  //tradeRate
			cstmt.setDouble(++i, se.getsettleRate());  //settleRate
		}
		else
		{
			cstmt.setInt(++i, (int)se.getspread());  //spread -- Finance=1 and Rate=0
			cstmt.setDouble(++i, se.getrate());  //rate
		}
		
		// adding status in case of Non-Trader
		if(!isTrader)
		{
			cstmt.setInt(++i, 0);  //status
		}
		
	
		if(request.getinstitution()>0)
		{
			cstmt.setInt(++i,request.getinstitution());
		}
		if(getSwapNo()>0)
		{
			SwapNumCheckTask swapObj = new SwapNumCheckTask(request.getlocation(),getSwapNo());
			try{
				swapObj.run(1);
				if(swapObj.isSwapNumPresent()){
					cstmt.setInt(++i, swapObj.getSwapId());
				}else{
					cstmt.setInt(++i, 0);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			//cstmt.setInt(++i,getSwapNo());
		}
		if(request.getlegalEntity()>0)
		{
			cstmt.setInt(++i,request.getlegalEntity());
		}
		if(request.getbookSeries()>0)
		{
			cstmt.setInt(++i,request.getbookSeries());
		}
		if(request.getcountry()>0)
		{
			cstmt.setInt(++i,request.getcountry());
		}
		if(tickerParamsList.size()>0)
		{
			InstrumentTask instObj = new InstrumentTask(request.getlocation(), se.getticker());
			try{
				instObj.run(1);
				if(instObj.isInstrPresent()){
					cstmt.setInt(++i, instObj.getInstrId());
				}else{
					cstmt.setInt(++i, 0);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}	
	}
	
	public List<TickerLevelParams> getTickerParams(CreateExceptionRequest request)
	{
		List<TickerLevelParams> reqList = new ArrayList<TickerLevelParams>();
		TickerLevelParams se = null;
		if(request.gettickerLevelParamsIterator()!=null)
		{
			Iterator itr = request.gettickerLevelParamsIterator();
			while(itr.hasNext())
			{
				se = (TickerLevelParams)itr.next();
				reqList.add(se);
			}	
			
		}
		return reqList;
	}
	
	private int getSwapNo()
	{
		return swapNum;
	}
	
	private void setSwapNo(int newVal)
	{
		swapNum = newVal;
	}

}
