// Title:        SwapTransaction
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

public class SwapTransaction implements FastCacheDVOBase  {
	public Date gettradeDate() { 
		return _tradeDate;
	}
	public void settradeDate(Date newValue) { 
		_tradeDate = newValue;
	}
	public Date getsettleDate() { 
		return _settleDate;
	}
	public void setsettleDate(Date newValue) { 
		_settleDate = newValue;
	}
	public Date getmaturityDate() { 
		return _maturityDate;
	}
	public void setmaturityDate(Date newValue) { 
		_maturityDate = newValue;
	}
	public Date getenterTime() { 
		return _enterTime;
	}
	public void setenterTime(Date newValue) { 
		_enterTime = newValue;
	}
	public Date getoriginalEnterTime() { 
		return _originalEnterTime;
	}
	public void setoriginalEnterTime(Date newValue) { 
		_originalEnterTime = newValue;
	}
	public int geteventId() {
		return _eventId;
	}
	public void seteventId(int newValue) {
		_eventId = newValue;
	}
	public int getlongShort() {
		return _longShort;
	}
	public void setlongShort(int newValue) {
		_longShort = newValue;
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
	public String getswiftCode() {
		if(_swiftCode!=null) {
			return new String(_swiftCode);
		}
		else {
			return null;
		}
	}
	public void setswiftCode(String newValue) {
		_swiftCode = newValue!=null ? newValue.toCharArray() : null;
	}
	public double getqty() {
		return _qty;
	}
	public void setqty(double newValue) {
		_qty = newValue;
	}
	public double getprice() {
		return _price;
	}
	public void setprice(double newValue) {
		_price = newValue;
	}
	public double getbasePrice() {
		return _basePrice;
	}
	public void setbasePrice(double newValue) {
		_basePrice = newValue;
	}
	public double getfxRate() {
		return _fxRate;
	}
	public void setfxRate(double newValue) {
		_fxRate = newValue;
	}
	public double getintRate() {
		return _intRate;
	}
	public void setintRate(double newValue) {
		_intRate = newValue;
	}
	public String getspreadEx() {
		if(_spreadEx!=null) {
			return new String(_spreadEx);
		}
		else {
			return null;
		}
	}
	public void setspreadEx(String newValue) {
		_spreadEx = newValue!=null ? newValue.toCharArray() : null;
	}
	public int geteventType() {
		return _eventType;
	}
	public void seteventType(int newValue) {
		_eventType = newValue;
	}
	public double getcommission() {
		return _commission;
	}
	public void setcommission(double newValue) {
		_commission = newValue;
	}
	public String getcommissionType() {
		if(_commissionType!=null) {
			return new String(_commissionType);
		}
		else {
			return null;
		}
	}
	public void setcommissionType(String newValue) {
		_commissionType = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getcommissionPay() {
		if(_commissionPay!=null) {
			return new String(_commissionPay);
		}
		else {
			return null;
		}
	}
	public void setcommissionPay(String newValue) {
		_commissionPay = newValue!=null ? newValue.toCharArray() : null;
	}
	public double getnotional() {
		return _notional;
	}
	public void setnotional(double newValue) {
		_notional = newValue;
	}
	public String getdescription() {
		if(_description!=null) {
			return new String(_description);
		}
		else {
			return null;
		}
	}
	public void setdescription(String newValue) {
		_description = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getfiId() {
		return _fiId;
	}
	public void setfiId(int newValue) {
		_fiId = newValue;
	}
	public String geteventCode() {
		if(_eventCode!=null) {
			return new String(_eventCode);
		}
		else {
			return null;
		}
	}
	public void seteventCode(String newValue) {
		_eventCode = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getcorpAction() {
		if(_corpAction!=null) {
			return new String(_corpAction);
		}
		else {
			return null;
		}
	}
	public void setcorpAction(String newValue) {
		_corpAction = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getswapNum() {
		return _swapNum;
	}
	public void setswapNum(int newValue) {
		_swapNum = newValue;
	}
	public int getcustomerId() {
		return _customerId;
	}
	public void setcustomerId(int newValue) {
		_customerId = newValue;
	}
	public int gettradePartyId() {
		return _tradePartyId;
	}
	public void settradePartyId(int newValue) {
		_tradePartyId = newValue;
	}
	public int getbookId() {
		return _bookId;
	}
	public void setbookId(int newValue) {
		_bookId = newValue;
	}
	public int getlegId() {
		return _legId;
	}
	public void setlegId(int newValue) {
		_legId = newValue;
	}
	public double getbuySell() {
		return _buySell;
	}
	public void setbuySell(double newValue) {
		_buySell = newValue;
	}
	public double getbaseAmount() {
		return _baseAmount;
	}
	public void setbaseAmount(double newValue) {
		_baseAmount = newValue;
	}
	public double getnotionalBaseCcy() {
		return _notionalBaseCcy;
	}
	public void setnotionalBaseCcy(double newValue) {
		_notionalBaseCcy = newValue;
	}
	public double getpayAmount() {
		return _payAmount;
	}
	public void setpayAmount(double newValue) {
		_payAmount = newValue;
	}
	public double getnotionalPayCcy() {
		return _notionalPayCcy;
	}
	public void setnotionalPayCcy(double newValue) {
		_notionalPayCcy = newValue;
	}
	public String getclientRef() {
		if(_clientRef!=null) {
			return new String(_clientRef);
		}
		else {
			return null;
		}
	}
	public void setclientRef(String newValue) {
		_clientRef = newValue!=null ? newValue.toCharArray() : null;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 35);
		item.putDate(_tradeDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_settleDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_maturityDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_enterTime);
		da.setDataItem(sa_idx++, item);
		item.putDate(_originalEnterTime);
		da.setDataItem(sa_idx++, item);
		item.putInt(_eventId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_longShort);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_ticker);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_swiftCode);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_qty);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_price);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_basePrice);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_fxRate);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_intRate);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_spreadEx);
		da.setDataItem(sa_idx++, item);
		item.putInt(_eventType);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_commission);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_commissionType);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_commissionPay);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_notional);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_description);
		da.setDataItem(sa_idx++, item);
		item.putInt(_fiId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_eventCode);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_corpAction);
		da.setDataItem(sa_idx++, item);
		item.putInt(_swapNum);
		da.setDataItem(sa_idx++, item);
		item.putInt(_customerId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_tradePartyId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_bookId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_legId);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_buySell);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_baseAmount);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_notionalBaseCcy);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_payAmount);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_notionalPayCcy);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_clientRef);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_tradeDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_settleDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_maturityDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_enterTime = item.getDate();
		in.getDataItem(sa_idx++, item);
		_originalEnterTime = item.getDate();
		in.getDataItem(sa_idx++, item);
		_eventId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_longShort = item.getInt();
		in.getDataItem(sa_idx++, item);
		_ticker = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_swiftCode = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_qty = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_price = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_basePrice = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_fxRate = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_intRate = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_spreadEx = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_eventType = item.getInt();
		in.getDataItem(sa_idx++, item);
		_commission = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_commissionType = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_commissionPay = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_notional = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_description = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_fiId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_eventCode = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_corpAction = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_swapNum = item.getInt();
		in.getDataItem(sa_idx++, item);
		_customerId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_tradePartyId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_bookId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_legId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_buySell = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_baseAmount = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_notionalBaseCcy = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_payAmount = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_notionalPayCcy = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_clientRef = item.getCharArray();
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
	private Date _tradeDate =  new Date();
	private Date _settleDate =  new Date();
	private Date _maturityDate =  new Date();
	private Date _enterTime =  new Date();
	private Date _originalEnterTime =  new Date();
	private int _eventId;
	private int _longShort;
	private char[] _ticker;
	private char[] _swiftCode;
	private double _qty;
	private double _price;
	private double _basePrice;
	private double _fxRate;
	private double _intRate;
	private char[] _spreadEx;
	private int _eventType;
	private double _commission;
	private char[] _commissionType;
	private char[] _commissionPay;
	private double _notional;
	private char[] _description;
	private int _fiId;
	private char[] _eventCode;
	private char[] _corpAction;
	private int _swapNum;
	private int _customerId;
	private int _tradePartyId;
	private int _bookId;
	private int _legId;
	private double _buySell;
	private double _baseAmount;
	private double _notionalBaseCcy;
	private double _payAmount;
	private double _notionalPayCcy;
	private char[] _clientRef;
	private DataItem item = new DataItem();
}

