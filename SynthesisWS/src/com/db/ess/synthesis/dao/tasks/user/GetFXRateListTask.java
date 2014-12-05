package com.db.ess.synthesis.dao.tasks.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseQueryDaoTask;
import com.db.ess.synthesis.dvo.FXRate;
import com.db.ess.synthesis.dvo.FXRateList;
import com.db.ess.synthesis.dvo.GetFXRateListsRequest;
import com.db.ess.synthesis.dvo.GetFXRateListsResponse;

public class GetFXRateListTask extends BaseQueryDaoTask<GetFXRateListsResponse> {
	private static final Logger logger = Logger.getLogger(GetFXRateListTask.class.getName());
	private GetFXRateListsRequest request;
    
	private static final String GET_FXRATENODATE = "select c.swiftCode, c2.swiftCode, pp.todayRate, pp.creationDate "
		+ "from CurrencyPairPoint pp join CurrencyPair p on pp.ccyPairId = p.ccyPairId "
		+ "join Currency c on p.majorCcyId = c.ccyId join Currency c2 on p.minorCcyId = c2.ccyId "
		+ "where pp.creationDate = (select max(pp2.creationDate) from CurrencyPairPoint pp2) "
		+ "order by p.majorCcyId, p.minorCcyId";

    private static final String GET_FXRATE = "select c.swiftCode, c2.swiftCode, pp.todayRate, pp.creationDate "
		+ "from CurrencyPairPoint pp join CurrencyPair p on pp.ccyPairId = p.ccyPairId "
		+ "join Currency c on p.majorCcyId = c.ccyId join Currency c2 on p.minorCcyId = c2.ccyId "
		+ "where pp.creationDate > ? and pp.creationDate = (select max(pp2.creationDate) from CurrencyPairPoint pp2) "
		+ "order by p.majorCcyId, p.minorCcyId";
	
    public GetFXRateListTask(GetFXRateListsRequest req, int loc) {
    	super(loc);
        this.request = req;
    }
	
	@Override
    protected Statement createStatement(Connection c) throws SQLException {
		PreparedStatement stmt;
		if (request.getlastDate() == null) {
			stmt = c.prepareStatement(GET_FXRATENODATE);
		} else {
			stmt = c.prepareStatement(GET_FXRATE);
			stmt.setDate(1, new java.sql.Date(request.getlastDate().getTime()));
		}
    	return stmt;
    }
    
	@Override
    protected ResultSet runStatement(Statement stmt) throws SQLException {
    	return ((PreparedStatement)stmt).executeQuery();
    }
    
	@Override
    protected int processResult(GetFXRateListsResponse res, ResultSet rs) throws Exception {
    	int count = 0;
    	logger.info(">>> process data result for FXRates....");
    	FXRateList rateList = null;
		String currentMajorCcy = null;
		Date lastDate = null;
		while (rs.next()) {
			int i = 0;
			String majorCcy = rs.getString(++i);
			if (rateList == null || !majorCcy.equals(currentMajorCcy)) {
				currentMajorCcy = majorCcy;
				rateList = new FXRateList();
				rateList.setmajorCcy(currentMajorCcy);
				res.setfxRateLists(rateList);
			}
			String minorCcy = rs.getString(++i);
			double todayRate = rs.getDouble(++i);
			lastDate = rs.getDate(++i);
			FXRate fxRate = new FXRate();
			fxRate.setminorCcy(minorCcy);
			fxRate.settodayRate(todayRate);
			rateList.setfxRate(fxRate);
			count++;
		}
		res.setlastDate(lastDate);
		return count;
    }
}
