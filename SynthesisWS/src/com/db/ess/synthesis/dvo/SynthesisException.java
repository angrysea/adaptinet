// Title:        SynthesisException
// Author:       Generate code
// Company:      
// Description:  This class was generated by the XML-Broker Developer Console
// Schema name:  /home/anthony/workspace/SynthesisWS/schema/Synthesis.xsd
// Java SDK:     

package com.db.ess.synthesis.dvo;

import java.util.Date;
import java.io.IOException;

import org.adaptinet.sdk.adaptinetex.FastCacheException;
import org.adaptinet.sdk.fastcache.CacheServer;
import org.adaptinet.sdk.fastcache.DataArray;
import org.adaptinet.sdk.fastcache.DataItem;
import org.adaptinet.sdk.fastcache.FastCacheDVOBase;

public class SynthesisException implements FastCacheDVOBase  {
	public Date getstartDate() { 
		return _startDate;
	}
	public void setstartDate(Date newValue) { 
		_startDate = newValue;
	}
	public Date getendDate() { 
		return _endDate;
	}
	public void setendDate(Date newValue) { 
		_endDate = newValue;
	}
	public Date getenterTime() { 
		return _enterTime;
	}
	public void setenterTime(Date newValue) { 
		_enterTime = newValue;
	}
	public double getspread() {
		return _spread;
	}
	public void setspread(double newValue) {
		_spread = newValue;
	}
	public double getrate() {
		return _rate;
	}
	public void setrate(double newValue) {
		_rate = newValue;
	}
	public double gettradeRate() {
		return _tradeRate;
	}
	public void settradeRate(double newValue) {
		_tradeRate = newValue;
	}
	public double getsettleRate() {
		return _settleRate;
	}
	public void setsettleRate(double newValue) {
		_settleRate = newValue;
	}
	public double getborrowCost() {
		return _borrowCost;
	}
	public void setborrowCost(double newValue) {
		_borrowCost = newValue;
	}
	public short getisDividend() {
		return _isDividend;
	}
	public void setisDividend(short newValue) {
		_isDividend = newValue;
	}
	public short getstatus() {
		return _status;
	}
	public void setstatus(short newValue) {
		_status = newValue;
	}
	public int getexceptId() {
		return _exceptId;
	}
	public void setexceptId(int newValue) {
		_exceptId = newValue;
	}
	public boolean getexceptIdHasError() {
		return _exceptIdHasError;
	}
	public void setexceptIdHasError(boolean newValue) {
		_exceptIdHasError = newValue;
	}
	public String getexceptIdError() {
		if(_exceptIdError!=null) {
			return new String(_exceptIdError);
		}
		else {
			return null;
		}
	}
	public void setexceptIdError(String newValue) {
		_exceptIdError = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getexceptUpdateTblName() {
		if(_exceptUpdateTblName!=null) {
			return new String(_exceptUpdateTblName);
		}
		else {
			return null;
		}
	}
	public void setexceptUpdateTblName(String newValue) {
		_exceptUpdateTblName = newValue!=null ? newValue.toCharArray() : null;
	}
	public short getlongShort() {
		return _longShort;
	}
	public void setlongShort(short newValue) {
		_longShort = newValue;
	}
	public String getrequestor() {
		if(_requestor!=null) {
			return new String(_requestor);
		}
		else {
			return null;
		}
	}
	public void setrequestor(String newValue) {
		_requestor = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getreasonId() {
		return _reasonId;
	}
	public void setreasonId(int newValue) {
		_reasonId = newValue;
	}
	public String getreasonComment() {
		if(_reasonComment!=null) {
			return new String(_reasonComment);
		}
		else {
			return null;
		}
	}
	public void setreasonComment(String newValue) {
		_reasonComment = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getbookSeriesId() {
		return _bookSeriesId;
	}
	public void setbookSeriesId(int newValue) {
		_bookSeriesId = newValue;
	}
	public int getcountryId() {
		return _countryId;
	}
	public void setcountryId(int newValue) {
		_countryId = newValue;
	}
	public String gettickerName() {
		if(_tickerName!=null) {
			return new String(_tickerName);
		}
		else {
			return null;
		}
	}
	public void settickerName(String newValue) {
		_tickerName = newValue!=null ? newValue.toCharArray() : null;
	}
	public String gettickerDescription() {
		if(_tickerDescription!=null) {
			return new String(_tickerDescription);
		}
		else {
			return null;
		}
	}
	public void settickerDescription(String newValue) {
		_tickerDescription = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getticker() {
		if(_ticker!=null) {
			return new String(_ticker);
		}
		else {
			return null;
		}
	}
	public void setticker(String newValue) {
		_ticker = newValue!=null ? newValue.toCharArray() : null;
	}
	public boolean gettickerHasError() {
		return _tickerHasError;
	}
	public void settickerHasError(boolean newValue) {
		_tickerHasError = newValue;
	}
	public String gettickerError() {
		if(_tickerError!=null) {
			return new String(_tickerError);
		}
		else {
			return null;
		}
	}
	public void settickerError(String newValue) {
		_tickerError = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getcusip() {
		if(_cusip!=null) {
			return new String(_cusip);
		}
		else {
			return null;
		}
	}
	public void setcusip(String newValue) {
		_cusip = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getsedol() {
		if(_sedol!=null) {
			return new String(_sedol);
		}
		else {
			return null;
		}
	}
	public void setsedol(String newValue) {
		_sedol = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getisin() {
		if(_isin!=null) {
			return new String(_isin);
		}
		else {
			return null;
		}
	}
	public void setisin(String newValue) {
		_isin = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getccy() {
		if(_ccy!=null) {
			return new String(_ccy);
		}
		else {
			return null;
		}
	}
	public void setccy(String newValue) {
		_ccy = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getcountryCode() {
		if(_countryCode!=null) {
			return new String(_countryCode);
		}
		else {
			return null;
		}
	}
	public void setcountryCode(String newValue) {
		_countryCode = newValue!=null ? newValue.toCharArray() : null;
	}
	public boolean getcountryCodeHasError() {
		return _countryCodeHasError;
	}
	public void setcountryCodeHasError(boolean newValue) {
		_countryCodeHasError = newValue;
	}
	public String getcountryCodeError() {
		if(_countryCodeError!=null) {
			return new String(_countryCodeError);
		}
		else {
			return null;
		}
	}
	public void setcountryCodeError(String newValue) {
		_countryCodeError = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getcountryDescription() {
		if(_countryDescription!=null) {
			return new String(_countryDescription);
		}
		else {
			return null;
		}
	}
	public void setcountryDescription(String newValue) {
		_countryDescription = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getsecType() {
		if(_secType!=null) {
			return new String(_secType);
		}
		else {
			return null;
		}
	}
	public void setsecType(String newValue) {
		_secType = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getinstrId() {
		return _instrId;
	}
	public void setinstrId(int newValue) {
		_instrId = newValue;
	}
	public int getinstitutionId() {
		return _institutionId;
	}
	public void setinstitutionId(int newValue) {
		_institutionId = newValue;
	}
	public boolean getinstitutionIdHasError() {
		return _institutionIdHasError;
	}
	public void setinstitutionIdHasError(boolean newValue) {
		_institutionIdHasError = newValue;
	}
	public String getinstitutionIdError() {
		if(_institutionIdError!=null) {
			return new String(_institutionIdError);
		}
		else {
			return null;
		}
	}
	public void setinstitutionIdError(String newValue) {
		_institutionIdError = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getswapId() {
		return _swapId;
	}
	public void setswapId(int newValue) {
		_swapId = newValue;
	}
	public int getcustId() {
		return _custId;
	}
	public void setcustId(int newValue) {
		_custId = newValue;
	}
	public boolean getcustIdHasError() {
		return _custIdHasError;
	}
	public void setcustIdHasError(boolean newValue) {
		_custIdHasError = newValue;
	}
	public String getcustIdError() {
		if(_custIdError!=null) {
			return new String(_custIdError);
		}
		else {
			return null;
		}
	}
	public void setcustIdError(String newValue) {
		_custIdError = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getuserId() {
		return _userId;
	}
	public void setuserId(int newValue) {
		_userId = newValue;
	}
	public String getactionedByUserName() {
		if(_actionedByUserName!=null) {
			return new String(_actionedByUserName);
		}
		else {
			return null;
		}
	}
	public void setactionedByUserName(String newValue) {
		_actionedByUserName = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getbookSeries() {
		if(_bookSeries!=null) {
			return new String(_bookSeries);
		}
		else {
			return null;
		}
	}
	public void setbookSeries(String newValue) {
		_bookSeries = newValue!=null ? newValue.toCharArray() : null;
	}
	public boolean getbookSeriesHasError() {
		return _bookSeriesHasError;
	}
	public void setbookSeriesHasError(boolean newValue) {
		_bookSeriesHasError = newValue;
	}
	public String getbookSeriesError() {
		if(_bookSeriesError!=null) {
			return new String(_bookSeriesError);
		}
		else {
			return null;
		}
	}
	public void setbookSeriesError(String newValue) {
		_bookSeriesError = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getinstitutionName() {
		if(_institutionName!=null) {
			return new String(_institutionName);
		}
		else {
			return null;
		}
	}
	public void setinstitutionName(String newValue) {
		_institutionName = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getcustomerName() {
		if(_customerName!=null) {
			return new String(_customerName);
		}
		else {
			return null;
		}
	}
	public void setcustomerName(String newValue) {
		_customerName = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getswapNumber() {
		return _swapNumber;
	}
	public void setswapNumber(int newValue) {
		_swapNumber = newValue;
	}
	public boolean getswapNumberHasError() {
		return _swapNumberHasError;
	}
	public void setswapNumberHasError(boolean newValue) {
		_swapNumberHasError = newValue;
	}
	public String getswapNumberError() {
		if(_swapNumberError!=null) {
			return new String(_swapNumberError);
		}
		else {
			return null;
		}
	}
	public void setswapNumberError(String newValue) {
		_swapNumberError = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getinstrumentType() {
		if(_instrumentType!=null) {
			return new String(_instrumentType);
		}
		else {
			return null;
		}
	}
	public void setinstrumentType(String newValue) {
		_instrumentType = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getuserName() {
		if(_userName!=null) {
			return new String(_userName);
		}
		else {
			return null;
		}
	}
	public void setuserName(String newValue) {
		_userName = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getreason() {
		if(_reason!=null) {
			return new String(_reason);
		}
		else {
			return null;
		}
	}
	public void setreason(String newValue) {
		_reason = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getlocation() {
		return _location;
	}
	public void setlocation(int newValue) {
		_location = newValue;
	}
	public String gettype() {
		if(_type!=null) {
			return new String(_type);
		}
		else {
			return null;
		}
	}
	public void settype(String newValue) {
		_type = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getrateReason() {
		if(_rateReason!=null) {
			return new String(_rateReason);
		}
		else {
			return null;
		}
	}
	public void setrateReason(String newValue) {
		_rateReason = newValue!=null ? newValue.toCharArray() : null;
	}
	public boolean getuserHasError() {
		return _userHasError;
	}
	public void setuserHasError(boolean newValue) {
		_userHasError = newValue;
	}
	public boolean getlocationHasError() {
		return _locationHasError;
	}
	public void setlocationHasError(boolean newValue) {
		_locationHasError = newValue;
	}
	public int getmanual() {
		return _manual;
	}
	public void setmanual(int newValue) {
		_manual = newValue;
	}
	public boolean gethasApprovedCousin() {
		return _hasApprovedCousin;
	}
	public void sethasApprovedCousin(boolean newValue) {
		_hasApprovedCousin = newValue;
	}
	public void putToCache(String key, CacheServer server) throws FastCacheException {
		DataArray da = writeCache();
		item.putDataArray(da);
		server.putValue(key, item);
		item.DataItemClear();
	}
	public void getFromCache(String key, CacheServer server) throws FastCacheException {
		DataItem item = server.getValue(key);
		DataArray da = new DataArray();
		item.getDataArray(da);
		readCache(da);
		item.DataItemClear();
	}
	public DataArray writeCache() throws FastCacheException {
		int sa_idx = 0;
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 62);
		item.putDate(_startDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_endDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_enterTime);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_spread);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_rate);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_tradeRate);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_settleRate);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_borrowCost);
		da.setDataItem(sa_idx++, item);
		item.putShort(_isDividend);
		da.setDataItem(sa_idx++, item);
		item.putShort(_status);
		da.setDataItem(sa_idx++, item);
		item.putInt(_exceptId);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_exceptIdHasError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_exceptIdError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_exceptUpdateTblName);
		da.setDataItem(sa_idx++, item);
		item.putShort(_longShort);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_requestor);
		da.setDataItem(sa_idx++, item);
		item.putInt(_reasonId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_reasonComment);
		da.setDataItem(sa_idx++, item);
		item.putInt(_bookSeriesId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_countryId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_tickerName);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_tickerDescription);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_ticker);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_tickerHasError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_tickerError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_cusip);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_sedol);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_isin);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_ccy);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_countryCode);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_countryCodeHasError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_countryCodeError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_countryDescription);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_secType);
		da.setDataItem(sa_idx++, item);
		item.putInt(_instrId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_institutionId);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_institutionIdHasError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_institutionIdError);
		da.setDataItem(sa_idx++, item);
		item.putInt(_swapId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_custId);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_custIdHasError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_custIdError);
		da.setDataItem(sa_idx++, item);
		item.putInt(_userId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_actionedByUserName);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_bookSeries);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_bookSeriesHasError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_bookSeriesError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_institutionName);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_customerName);
		da.setDataItem(sa_idx++, item);
		item.putInt(_swapNumber);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_swapNumberHasError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_swapNumberError);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_instrumentType);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_userName);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_reason);
		da.setDataItem(sa_idx++, item);
		item.putInt(_location);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_type);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_rateReason);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_userHasError);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_locationHasError);
		da.setDataItem(sa_idx++, item);
		item.putInt(_manual);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_hasApprovedCousin);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_startDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_endDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_enterTime = item.getDate();
		in.getDataItem(sa_idx++, item);
		_spread = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_rate = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_tradeRate = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_settleRate = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_borrowCost = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_isDividend = item.getShort();
		in.getDataItem(sa_idx++, item);
		_status = item.getShort();
		in.getDataItem(sa_idx++, item);
		_exceptId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_exceptIdHasError = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_exceptIdError = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_exceptUpdateTblName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_longShort = item.getShort();
		in.getDataItem(sa_idx++, item);
		_requestor = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_reasonId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_reasonComment = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_bookSeriesId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_countryId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_tickerName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_tickerDescription = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_ticker = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_tickerHasError = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_tickerError = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_cusip = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_sedol = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_isin = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_ccy = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_countryCode = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_countryCodeHasError = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_countryCodeError = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_countryDescription = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_secType = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_instrId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_institutionId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_institutionIdHasError = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_institutionIdError = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_swapId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_custId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_custIdHasError = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_custIdError = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_userId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_actionedByUserName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_bookSeries = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_bookSeriesHasError = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_bookSeriesError = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_institutionName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_customerName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_swapNumber = item.getInt();
		in.getDataItem(sa_idx++, item);
		_swapNumberHasError = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_swapNumberError = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_instrumentType = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_userName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_reason = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_location = item.getInt();
		in.getDataItem(sa_idx++, item);
		_type = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_rateReason = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_userHasError = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_locationHasError = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_manual = item.getInt();
		in.getDataItem(sa_idx++, item);
		_hasApprovedCousin = item.getBoolean();
	}
	public void writeObject(byte [] data) throws FastCacheException {
		item.writeObject(data);
		DataArray in = new DataArray();
		item.getDataArray(in);
		readCache(in);
	}
	public byte [] readObject() throws FastCacheException, IOException {
		DataArray requestDA = writeCache();
		item.putDataArray(requestDA);
		return item.readObject();
	}
	private Date _startDate =  new Date();
	private Date _endDate =  new Date();
	private Date _enterTime =  new Date();
	private double _spread;
	private double _rate;
	private double _tradeRate;
	private double _settleRate;
	private double _borrowCost;
	private short _isDividend;
	private short _status;
	private int _exceptId;
	private boolean _exceptIdHasError;
	private char[] _exceptIdError;
	private char[] _exceptUpdateTblName;
	private short _longShort;
	private char[] _requestor;
	private int _reasonId;
	private char[] _reasonComment;
	private int _bookSeriesId;
	private int _countryId;
	private char[] _tickerName;
	private char[] _tickerDescription;
	private char[] _ticker;
	private boolean _tickerHasError;
	private char[] _tickerError;
	private char[] _cusip;
	private char[] _sedol;
	private char[] _isin;
	private char[] _ccy;
	private char[] _countryCode;
	private boolean _countryCodeHasError;
	private char[] _countryCodeError;
	private char[] _countryDescription;
	private char[] _secType;
	private int _instrId;
	private int _institutionId;
	private boolean _institutionIdHasError;
	private char[] _institutionIdError;
	private int _swapId;
	private int _custId;
	private boolean _custIdHasError;
	private char[] _custIdError;
	private int _userId;
	private char[] _actionedByUserName;
	private char[] _bookSeries;
	private boolean _bookSeriesHasError;
	private char[] _bookSeriesError;
	private char[] _institutionName;
	private char[] _customerName;
	private int _swapNumber;
	private boolean _swapNumberHasError;
	private char[] _swapNumberError;
	private char[] _instrumentType;
	private char[] _userName;
	private char[] _reason;
	private int _location;
	private char[] _type;
	private char[] _rateReason;
	private boolean _userHasError;
	private boolean _locationHasError;
	private int _manual;
	private boolean _hasApprovedCousin;
	private DataItem item = new DataItem();
}
