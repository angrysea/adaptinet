package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dao.tasks.TaskHelper;
import com.db.ess.synthesis.dvo.GetSynthesisExceptionRequest;
import com.db.ess.synthesis.dvo.GetSynthesisExceptionResponse;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.dvo.SynthesisException;
import com.db.ess.synthesis.util.SynthesisConstants;

public class GetSynthesisExceptionTask extends BaseQueryDaoTask<GetSynthesisExceptionResponse> {
	
	private static final Logger logger = Logger.getLogger(GetSynthesisExceptionTask.class.getName());
	private GetSynthesisExceptionRequest request;
	private GetSynthesisExceptionResponse response;
    
	public static final String SELECT_EXCEPTIONS ="{ call dbo.SYN_GetException( " +
			" @IsDividend=?,  @IsSpread=?,    @IsRate=?,       @DateType=?,   @DateValueFrom=?, " +
			" @DateValueTo=?, @SecCodeJoin=?, @SecCodeWhere=?, @Customer=?,   @Institution=?, " +
			" @SwapNumber=?,  @ExceptionId=?, @Country=?,      @BookSeries=?, @IsApproved=?, " +
			" @IsPending=?,   @IsRejected=?, @UserId=? )}"; 
    
	public GetSynthesisExceptionTask(GetSynthesisExceptionRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
	
	@Override
    protected Statement createStatement(Connection c) throws SQLException {
		
    	CallableStatement cstmt = c.prepareCall(SELECT_EXCEPTIONS);
    	cstmt.setInt   (1,  request.getisDividend());
    	cstmt.setInt   (2,  request.getisSpread());
    	cstmt.setInt   (3,  request.getisRate());
    	cstmt.setString(4,  request.getdateType());
    	cstmt.setString(5,  request.getdateValueFrom());
    	cstmt.setString(6,  request.getdateValueTo());
    	cstmt.setString(7,  TaskHelper.createJoin(request.getextValue(), request.getextType()));
    	cstmt.setString(8,  TaskHelper.createWhere(request.getextValue(), request.getextType()));  
    	cstmt.setString(9,  TaskHelper.convert(request.getlegalEntity(), "upper(le.name)", false));
    	cstmt.setString(10, TaskHelper.convert(request.getinstitution(), "upper(n.name)", false));
    	cstmt.setString(11, TaskHelper.convert(request.getswapNum(), "s.swapNum", true));
    	cstmt.setString(12, TaskHelper.convert(request.getexceptionId(), "e.exceptId", true));
    	cstmt.setString(13, TaskHelper.convert(request.getcountry(), "upper(c.description)", false));
    	cstmt.setString(14, TaskHelper.convert(request.getbookSeries(), "upper(bsd.description)", false));
    	cstmt.setInt   (15, request.getisApproved());
    	cstmt.setInt   (16, request.getisPending());
    	cstmt.setInt   (17, request.getisRejected());
    	cstmt.setInt   (18, request.getuserId());
    	return cstmt;
    }
    
	@Override
    protected ResultSet runStatement(Statement stmt) throws SQLException {
		logger.info(">>> running " +
		String.format(
		    "call dbo.SYN_GetException( " +
			" @IsDividend=%d,  @IsSpread=%d,    @IsRate=%d,  @DateType=%s,   @DateValueFrom=%s, " +
			" @DateValueTo=%s, @SecCodeJoin=%s, @SecCodeWhere=%s, @Customer=%s,   @Institution=%s, " +
			" @SwapNumber=%s,  @ExceptionId=%s, @Country=%s, @BookSeries=%s, @IsApproved=%d, " +
			" @IsPending=%d,   @IsRejected=%d, @UserId = %d)" 
			,  request.getisDividend()
	    	,  request.getisSpread()
	    	,  request.getisRate()
	    	,  request.getdateType()
	    	,  request.getdateValueFrom()
	    	,  request.getdateValueTo()

	    	,  TaskHelper.createJoin(request.getextValue(), request.getextType())
	    	,  TaskHelper.createWhere(request.getextValue(), request.getextType())
	    	,  TaskHelper.convert(request.getlegalEntity(), "upper(le.name)", false)
    	    ,  TaskHelper.convert(request.getinstitution(), "upper(n.name)", false)
    	    ,  TaskHelper.convert(request.getswapNum(), "s.swapNum", true)
    	    ,  TaskHelper.convert(request.getexceptionId(), "e.exceptId", true)
    	    ,  TaskHelper.convert(request.getcountry(), "upper(c.description)", false)
    	    ,  TaskHelper.convert(request.getbookSeries(), "upper(bsd.description)", false)
	    	
	    	, request.getisApproved()
	    	, request.getisPending()
	    	, request.getisRejected()  
	    	, request.getuserId()
		    
		));
    	return ((PreparedStatement)stmt).executeQuery();
    }
    
	@Override
    protected int processResult(GetSynthesisExceptionResponse res, ResultSet rs) throws SQLException {
    	int count = 0;
    	logger.info(">>> process data result for Exceptions....");
		while (rs.next()) {
			if (count > SynthesisConstants.MAX_RESULT_SET_SIZE) {
				res.setreturnResponse(getRSOverFlowResponse());
				break;
			}
			int i = 1;
            SynthesisException se = new SynthesisException();
            se.setspread(rs.getDouble(i++));
            se.setrate(rs.getDouble(i++));
            se.settradeRate(rs.getDouble(i++));
            se.setsettleRate(rs.getDouble(i++));
            se.setisDividend(rs.getShort(i++));
            se.setstatus(rs.getShort(i++));
            se.setstartDate(convert(rs.getTimestamp(i++)));
            se.setendDate(convert(rs.getTimestamp(i++)));
            se.setenterTime(convert(rs.getTimestamp(i++)));
            se.setexceptId(rs.getInt(i++));
            se.setlongShort(rs.getShort(i++));
            se.setrequestor(rs.getString(i++));
            se.setreasonId(rs.getInt(i++));
            se.setreasonComment(rs.getString(i++));
            se.setbookSeriesId(rs.getInt(i++));
            se.setcountryId(rs.getInt(i++));
            se.settickerName(rs.getString(i++));
            se.settickerDescription(rs.getString(i++));
            se.setticker(rs.getString(i++));
            se.setcusip(rs.getString(i++));
            se.setsedol(rs.getString(i++));
            se.setisin(rs.getString(i++));
            se.setccy(rs.getString(i++));
            se.setcountryCode(rs.getString(i++));
            se.setcountryDescription(rs.getString(i++));
            se.setsecType(rs.getString(i++));
            se.setinstrId(rs.getInt(i++));
            se.setinstitutionId(rs.getInt(i++));
            se.setswapId(rs.getInt(i++));
            se.setcustId(rs.getInt(i++));
            se.setuserId(rs.getInt(i++));
            se.setactionedByUserName(rs.getString(i++));
            se.setbookSeries(rs.getString(i++));
            se.setinstitutionName(rs.getString(i++));
            se.setcustomerName(rs.getString(i++));
            se.setswapNumber(rs.getInt(i++));
            se.setinstrumentType(rs.getString(i++));
            se.setuserName(rs.getString(i++));
            se.setreason(rs.getString(i++));
            se.setmanual(rs.getInt(i++));
            se.sethasApprovedCousin(rs.getInt(i++)==1?true:false);
            se.setlocation(location);
			res.setexceptions(se);
			count++;
		}
		return count;
    }
	
	private Date convert(Timestamp t) {
		return (t == null ? null : new Date(t.getTime()));
	}
	
	protected ReturnResponse getRSOverFlowResponse() {

		ReturnResponse returnResponse = new ReturnResponse();
		returnResponse.setmessage(SynthesisConstants.RESULT_SET_OVERFLOW_MSG);
		returnResponse
				.setreturnCode(SynthesisConstants.RESULT_SET_OVERFLOW_ERRORCODE);
		return returnResponse;

	}
	
}