// Title:        Desk
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

public class Desk implements FastCacheDVOBase  {
	public int getdeskId() {
		return _deskId;
	}
	public void setdeskId(int newValue) {
		_deskId = newValue;
	}
	public int getbusinessUnitId() {
		return _businessUnitId;
	}
	public void setbusinessUnitId(int newValue) {
		_businessUnitId = newValue;
	}
	public String getname() {
		if(_name!=null) {
			return new String(_name);
		}
		else {
			return null;
		}
	}
	public void setname(String newValue) {
		_name = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getfullName() {
		if(_fullName!=null) {
			return new String(_fullName);
		}
		else {
			return null;
		}
	}
	public void setfullName(String newValue) {
		_fullName = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getmnemonic() {
		return _mnemonic;
	}
	public void setmnemonic(int newValue) {
		_mnemonic = newValue;
	}
	public int getstatus() {
		return _status;
	}
	public void setstatus(int newValue) {
		_status = newValue;
	}
	public int getenterUserId() {
		return _enterUserId;
	}
	public void setenterUserId(int newValue) {
		_enterUserId = newValue;
	}
	public Date getenterTime() {
		return _enterTime;
	}
	public void setenterTime(Date newValue) {
		_enterTime = newValue;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 8);
		item.putInt(_deskId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_businessUnitId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_name);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_fullName);
		da.setDataItem(sa_idx++, item);
		item.putInt(_mnemonic);
		da.setDataItem(sa_idx++, item);
		item.putInt(_status);
		da.setDataItem(sa_idx++, item);
		item.putInt(_enterUserId);
		da.setDataItem(sa_idx++, item);
		item.putDate(_enterTime);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_deskId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_businessUnitId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_name = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_fullName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_mnemonic = item.getInt();
		in.getDataItem(sa_idx++, item);
		_status = item.getInt();
		in.getDataItem(sa_idx++, item);
		_enterUserId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_enterTime = item.getDate();
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
	private int _deskId;
	private int _businessUnitId;
	private char[] _name;
	private char[] _fullName;
	private int _mnemonic;
	private int _status;
	private int _enterUserId;
	private Date _enterTime;
	private DataItem item = new DataItem();
}

