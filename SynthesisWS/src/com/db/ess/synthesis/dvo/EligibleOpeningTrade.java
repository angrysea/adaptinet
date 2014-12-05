// Title:        EligibleOpeningTrade
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

public class EligibleOpeningTrade implements FastCacheDVOBase  {
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
	public int geteventId() {
		return _eventId;
	}
	public void seteventId(int newValue) {
		_eventId = newValue;
	}
	public float getqty() {
		return _qty;
	}
	public void setqty(float newValue) {
		_qty = newValue;
	}
	public int getlegId() {
		return _legId;
	}
	public void setlegId(int newValue) {
		_legId = newValue;
	}
	public int getinstrId() {
		return _instrId;
	}
	public void setinstrId(int newValue) {
		_instrId = newValue;
	}
	public float getprice () {
		return _price ;
	}
	public void setprice (float newValue) {
		_price  = newValue;
	}
	public float getmatchedQty() {
		return _matchedQty;
	}
	public void setmatchedQty(float newValue) {
		_matchedQty = newValue;
	}
	public int getlotAge() {
		return _lotAge;
	}
	public void setlotAge(int newValue) {
		_lotAge = newValue;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 9);
		item.putDate(_tradeDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_settleDate);
		da.setDataItem(sa_idx++, item);
		item.putInt(_eventId);
		da.setDataItem(sa_idx++, item);
		item.putFloat(_qty);
		da.setDataItem(sa_idx++, item);
		item.putInt(_legId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_instrId);
		da.setDataItem(sa_idx++, item);
		item.putFloat(_price );
		da.setDataItem(sa_idx++, item);
		item.putFloat(_matchedQty);
		da.setDataItem(sa_idx++, item);
		item.putInt(_lotAge);
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
		_eventId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_qty = item.getFloat();
		in.getDataItem(sa_idx++, item);
		_legId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_instrId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_price  = item.getFloat();
		in.getDataItem(sa_idx++, item);
		_matchedQty = item.getFloat();
		in.getDataItem(sa_idx++, item);
		_lotAge = item.getInt();
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
	private int _eventId;
	private float _qty;
	private int _legId;
	private int _instrId;
	private float _price ;
	private float _matchedQty;
	private int _lotAge;
	private DataItem item = new DataItem();
}
