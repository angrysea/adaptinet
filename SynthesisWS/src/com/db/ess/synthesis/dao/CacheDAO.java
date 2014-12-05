package com.db.ess.synthesis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.CacheEntry;
import com.db.ess.synthesis.dvo.FXRate;
import com.db.ess.synthesis.dvo.FXRateList;
import com.db.ess.synthesis.dvo.GetFXRateListsRequest;
import com.db.ess.synthesis.dvo.GetFXRateListsResponse;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;
import com.db.ess.synthesis.util.cache.CachedBasis;
import com.db.ess.synthesis.util.cache.CachedBook;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;
import com.db.ess.synthesis.vo.Exchange;

public class CacheDAO {

	private static Logger logger = Logger.getLogger(CacheDAO.class.getName());

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

	private static final String INSERT_CACHE_INFO = 
		"INSERT INTO SYN_CacheInfo (version, updatedTime) VALUES (?,?)";
	private static final String UPDATE_CACHE_INFO = 
		 "UPDATE SYN_CacheInfo SET version = ?, updatedTime = ?";

	public int getCacheVersion() {
		return cacheVersion;
	}
	
	public void udpateCacheInfo() {
		cacheVersion = readCacheVersion(ESSLocation.LONDON);
		if (cacheVersion <= 0) {
			cacheVersion = readCacheVersion(ESSLocation.NEWYORK);
		}
		cacheVersion += 1;
		logger.info(">>> current cache version is increased to : " + cacheVersion);
		
		if (isCacheInfoExisting()) {
			updateCacheInfo(ESSLocation.LONDON);
		} else {
			insertCacheInfo(ESSLocation.LONDON);
		}
		
		if (isCacheInfoExisting()) {
			updateCacheInfo(ESSLocation.NEWYORK);
		} else {
			insertCacheInfo(ESSLocation.NEWYORK);
		}
	}
	
	private void updateCacheInfo(int location) {
		
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(UPDATE_CACHE_INFO);
	    	stmt.setInt(1, cacheVersion);
	    	Calendar currenttime = Calendar.getInstance();
	        java.util.Date currentdate = currenttime.getTime();
	    	stmt.setDate(2, new java.sql.Date(currentdate.getTime())); 
			stmt.executeUpdate();
		} catch (Exception ex) {
			logger.error("Failed to update cache info: ", ex); 
		} finally {
			SQLUtils.closeResources(conn, stmt, null);
		}
	}
	
	private void insertCacheInfo(int location) {
		
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(INSERT_CACHE_INFO);
	    	stmt.setInt(1, cacheVersion);
	    	Calendar currenttime = Calendar.getInstance();
	        java.util.Date currentdate = currenttime.getTime();
	    	stmt.setDate(2, new java.sql.Date(currentdate.getTime())); 
			stmt.executeUpdate();
		} catch (Exception ex) {
			logger.error("Failed to insert cache info: ", ex); 
		} finally {
			SQLUtils.closeResources(conn, stmt, null);
		}
	}
	
	private static int cacheVersion = 0; 
	private int readCacheVersion(int location) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement("SELECT version FROM SYN_CacheInfo");
			rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		return 0;
	}
	private boolean isCacheInfoExisting() {
		return cacheVersion > 1;
	}
   
	
	
	public void loadMap(String sql, Map<Integer, String> map,int location)
			throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int key = rs.getInt(1);
				String value = rs.getString(2);
				if (key >= 0 && value != null) {
				    map.put(key, value);
				} else {
					logger.warn(String.format("either key[%d] or value[%s] is invalid", key, value));
				}
			}
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}

	public void loadBookMap(String sql, Map<Integer, CachedBook> map,int location)
			throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int i = 0;
				CachedBook book = new CachedBook();
				book.setBookId(rs.getInt(++i));
				book.setName(rs.getString(++i));
				book.setAdpAcctNum(rs.getString(++i));
				book.setAccountname(rs.getString(++i));
				book.setDeskname(rs.getString(++i));
				book.setBusinessUnitName(rs.getString(++i));
				map.put(Integer.valueOf(book.getBookId()), book);
			}
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}

	public void loadCurrencyMap(String sql, Map<Integer, CachedCurrency> map,int location)
			throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int i = 0;
				CachedCurrency currency = new CachedCurrency();
				currency.setCcyId(rs.getInt(++i));
				currency.setSwiftCode(rs.getString(++i));
				currency.setDecPlaces(rs.getInt(++i));
				currency.setBasis(rs.getInt(++i));
				currency.setSwapBasis(rs.getInt(++i));
				currency.setSpotDays(rs.getShort(++i));
				currency.setRounding(rs.getShort(++i));
				currency.setCalId(rs.getInt(++i));
				currency.setRtFeedDivisor(rs.getInt(++i));
				currency.setDisplay(rs.getInt(++i));
				map.put(Integer.valueOf(currency.getCcyId()), currency);
			}
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}

	public void loadBasisMap(String sql, Map<Integer, CachedBasis> map,int location)
			throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int i = 0;
				CachedBasis basis = new CachedBasis();
				basis.setUnitType(rs.getInt(++i));
				basis.setUnitTypeCode(rs.getString(++i));
				basis.setDays(rs.getInt(++i));
				basis.setMonth30days(rs.getShort(++i));
				basis.setDisplay(rs.getShort(++i));
				map.put(Integer.valueOf(basis.getUnitType()), basis);
			}
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}

	public Date loadFXRateMap(String sql,
			Map<String, Map<String, Double>> map,int location) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Date lastDate = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			Map<String, Double> rateMap = null;
			String currentMajorCcy = null;
			while (rs.next()) {
				int i = 0;
				String majorCcy = rs.getString(++i);
				if (rateMap == null || !majorCcy.equals(currentMajorCcy)) {
					currentMajorCcy = majorCcy;
					rateMap = new HashMap<String, Double>();
					map.put(currentMajorCcy, rateMap);
				}
				String minorCcy = rs.getString(++i);
				double todayRate = rs.getDouble(++i);
				lastDate = rs.getDate(++i);
				rateMap.put(minorCcy, Double.valueOf(todayRate));
			}
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		return lastDate;
	}
	
	public void loadExchangeMap(String sql,
			Map<Integer, Exchange> map,int location) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int i = 0;
				Exchange exchange = new Exchange();
				int exchId = rs.getInt(++i);
				exchange.setExchId(exchId);
				exchange.setExchCode(rs.getString(++i));
				exchange.setDescription(rs.getString(++i));
				exchange.setCountryCode(rs.getString(++i));
				map.put(exchId, exchange);
			}
		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
	}

	public GetFXRateListsResponse getFXRateList(GetFXRateListsRequest request)
			throws Exception {

		GetFXRateListsResponse response = new GetFXRateListsResponse();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Date lastDate = request.getlastDate();

		try {
			conn = SQLUtils.getConnection();

			if (lastDate == null) {
				stmt = conn.prepareStatement(GET_FXRATENODATE);
			} else {
				stmt = conn.prepareStatement(GET_FXRATE);
				stmt.setDate(1, new java.sql.Date(lastDate.getTime()));
			}
			rs = stmt.executeQuery();
			FXRateList rateList = null;
			String currentMajorCcy = null;
			while (rs.next()) {
				int i = 0;
				String majorCcy = rs.getString(++i);
				if (rateList == null || !majorCcy.equals(currentMajorCcy)) {
					currentMajorCcy = majorCcy;
					rateList = new FXRateList();
					rateList.setmajorCcy(currentMajorCcy);
					response.setfxRateLists(rateList);
				}
				String minorCcy = rs.getString(++i);
				double todayRate = rs.getDouble(++i);
				lastDate = rs.getDate(++i);
				FXRate fxRate = new FXRate();
				fxRate.setminorCcy(minorCcy);
				fxRate.settodayRate(todayRate);
				rateList.setfxRate(fxRate);
			}
			response.setlastDate(lastDate);
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		return response;
	}

	static public boolean checkFXRateDate(String sql, Date currentDate)
			throws Exception {

		boolean bRet = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = SQLUtils.getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Date lastDate = rs.getDate(1);
				bRet = ((lastDate == currentDate) ? true : false);
			}
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		return bRet;
	}
	
	public Map<String, List<CacheEntry>> loadSecurities(String sql, int location) throws Exception {

		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		Map<String, List<CacheEntry>> map = new HashMap<String, List<CacheEntry>>();

		for (String securityType : RefCache.SecurityType.types()) {
			try {
				conn = SQLUtils.getConnection(location);
				stmt = conn.prepareCall(sql);
				stmt.setString(1, securityType);
				rs = stmt.executeQuery();
				
				List<CacheEntry> list = new ArrayList<CacheEntry>();
				map.put(securityType, list);
				while (rs.next()) {
					CacheEntry cacheEntry = new CacheEntry();
					int key = rs.getInt(1);
					String value = rs.getString(2);
					String type = rs.getString(3);
					String description = rs.getString(4);

					// trim string values
					if (value != null) value = value.trim();
					if (type != null) type = type.trim();
					if (description != null) description = description.trim();

					cacheEntry.setkey(key);
					cacheEntry.setvalue(value);
					cacheEntry.settype(type);
					cacheEntry.setdescription(description);
					cacheEntry.setlocation(location);
					list.add(cacheEntry);
				}
			} catch (Exception ex) {
				throw new Exception(ex);
			} finally {
				SQLUtils.closeResources(conn, stmt, rs);
			}
		}
		return map;
	}
}
