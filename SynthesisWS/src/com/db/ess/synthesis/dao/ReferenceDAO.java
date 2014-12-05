package com.db.ess.synthesis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.Country;
import com.db.ess.synthesis.dvo.Desk;
import com.db.ess.synthesis.dvo.GetCountriesRequest;
import com.db.ess.synthesis.dvo.GetCountriesResponse;
import com.db.ess.synthesis.dvo.GetCurrenciesRequest;
import com.db.ess.synthesis.dvo.GetCurrenciesResponse;
import com.db.ess.synthesis.dvo.Currency;
import com.db.ess.synthesis.dvo.GetDesksResponse;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SQLUtils;

public class ReferenceDAO {

	private static Logger logger = Logger.getLogger(ReferenceDAO.class
			.getName());

	private static final String GET_CURRENCY = "select ccyId, swiftCode, decPlaces, " +
			"basis, swapBasis, spotDays, rounding, calId, rtFeedDivisor, display " +
			"from Currency";
	private static final String GET_COUNTRY = "select ccyId, swiftCode, decPlaces, " +
			"basis, swapBasis, spotDays, rounding, calId, rtFeedDivisor, display " +
			"from Currency";
	
	private static final String GET_DESK = "select deskId, businessUnitId, name, " +
			"fullName, mnemonic, status, enterUserId, enterTime from Desk";
	
	public GetCurrenciesResponse getCurrencies(
			GetCurrenciesRequest request) throws Exception {
		GetCurrenciesResponse response = new GetCurrenciesResponse();
		logger.info("Inside getCurrencies(), request received: "+request);
		int location = request.getlocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getCurrencies(request, ESSLocation.LONDON, response);
			break;
			
		case ESSLocation.NEWYORK:
			getCurrencies(request, ESSLocation.NEWYORK, response);
			break;

		case ESSLocation.GLOBAL:
			getCurrencies(request, ESSLocation.LONDON, response);
			getCurrencies(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
	}
	
	public void getCurrencies(
			GetCurrenciesRequest request, int location, GetCurrenciesResponse response) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
		
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_CURRENCY);
			logger.info("Executing stmt: "+stmt);
			long time = System.currentTimeMillis(); 
			rs = stmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				response.setcurreny(populateCurrency(rs));
			}

		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		
	}
	
	public GetCountriesResponse getCountries(
			GetCountriesRequest request) throws Exception {
		
		GetCountriesResponse response = new GetCountriesResponse();
		logger.info("Inside getCountries(), request received: "+request);
		int location = request.getlocation();
		switch (location) {
		case ESSLocation.UNKNOWN:
			location = ESSLocation.LONDON;

			// Fall thru using london as a default.
		case ESSLocation.LONDON:
			getCountries(request, ESSLocation.LONDON, response);
			break;
			
		case ESSLocation.NEWYORK:
			getCountries(request, ESSLocation.NEWYORK, response);
			break;

		case ESSLocation.GLOBAL:
			getCountries(request, ESSLocation.LONDON, response);
			getCountries(request, ESSLocation.NEWYORK, response);
			break;

		default:
			throw new Exception("Invalid or Unknown location.");
		}
		return response;
		
	}
	
	public void getCountries(
			GetCountriesRequest request, int location, GetCountriesResponse response) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
		
			conn = SQLUtils.getConnection(location);
			stmt = conn.prepareStatement(GET_COUNTRY);
			logger.info("Executing stmt: "+stmt);
			long time = System.currentTimeMillis(); 
			rs = stmt.executeQuery();
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time)); 
			while (rs.next()) {
				response.setcountry(populateCountry(rs));
			}

		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex); 
			throw new Exception(ex);
		} finally {
			SQLUtils.closeResources(conn, stmt, rs);
		}
		
	}
	
	private Currency populateCurrency(ResultSet rs) throws SQLException {
		Currency currency = new Currency();
		int i = 0;
		currency.setccyId(rs.getInt(++i));
		currency.setswiftCode(rs.getString(++i));
		currency.setdecPlaces(rs.getInt(++i));
		currency.setbasis(rs.getInt(++i));
		currency.setswapBasis(rs.getInt(++i));
		currency.setspotDays(rs.getShort(++i));
		currency.setrounding(rs.getShort(++i));
		currency.setcalId(rs.getInt(++i));
		currency.setrtFeedDivisor(rs.getInt(++i));
		currency.setdisplay(rs.getInt(++i));
		return currency;
	}
	
	private Country populateCountry(ResultSet rs) throws SQLException {
		Country country = new Country();
		int i = 0;
		country.setcountryId(rs.getInt(++i));
		country.setcode(rs.getString(++i));
		country.setdescription(rs.getString(++i));
		country.setccyId(rs.getInt(++i));
		country.settaxRate(rs.getFloat(++i));
		country.setmanualCommission(rs.getDouble(++i));
		country.setremoteCommission(rs.getDouble(++i));
		country.setdivTreatment(rs.getShort(++i));
		country.setstoxxNet(rs.getDouble(++i));
		country.setstoxxGross(rs.getDouble(++i));
		country.setcalId(rs.getInt(++i));
		country.setzone(rs.getShort(++i));
		country.setdays(rs.getInt(++i));
		return country;
	}
	
	private Desk populateDesk(ResultSet rs) throws SQLException {
		Desk desk = new Desk();
		int i = 0;
		desk.setdeskId(rs.getInt(++i));
		desk.setbusinessUnitId(rs.getInt(++i));
		desk.setname(rs.getString(++i));
		desk.setfullName(rs.getString(++i));
		desk.setmnemonic(rs.getInt(++i));
		desk.setstatus(rs.getInt(++i));
		desk.setenterUserId(rs.getInt(++i));
		desk.setenterTime(rs.getDate(++i));
		return desk;
	}
	
}
