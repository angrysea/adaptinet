// Title:        Exceptions
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

public class Exceptions implements FastCacheDVOBase  {
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
	public int getswapNum() {
		return _swapNum;
	}
	public void setswapNum(int newValue) {
		_swapNum = newValue;
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
	public int getspread() {
		return _spread;
	}
	public void setspread(int newValue) {
		_spread = newValue;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 7);
		item.putDate(_startDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_endDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_enterTime);
		da.setDataItem(sa_idx++, item);
		item.putInt(_swapNum);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_type);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_userName);
		da.setDataItem(sa_idx++, item);
		item.putInt(_spread);
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
		_swapNum = item.getInt();
		in.getDataItem(sa_idx++, item);
		_type = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_userName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_spread = item.getInt();
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
	private int _swapNum;
	private char[] _type;
	private char[] _userName;
	private int _spread;
	private DataItem item = new DataItem();
}
