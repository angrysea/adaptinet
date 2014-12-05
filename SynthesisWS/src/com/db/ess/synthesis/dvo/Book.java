// Title:        Book
// Author:       Generate code
// Company:      
// Description:  This class was generated by the XML-Broker Developer Console
// Schema name:  /home/anthony/workspace/SynthesisWS/schema/Synthesis.xsd
// Java SDK:     

package com.db.ess.synthesis.dvo;

import java.io.IOException;

import org.adaptinet.sdk.adaptinetex.FastCacheException;
import org.adaptinet.sdk.fastcache.CacheServer;
import org.adaptinet.sdk.fastcache.DataArray;
import org.adaptinet.sdk.fastcache.DataItem;
import org.adaptinet.sdk.fastcache.FastCacheDVOBase;

public class Book implements FastCacheDVOBase  {
	public int getbookId() {
		return _bookId;
	}
	public void setbookId(int newValue) {
		_bookId = newValue;
	}
	public int getacctId() {
		return _acctId;
	}
	public void setacctId(int newValue) {
		_acctId = newValue;
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
	public String getmnemonic() {
		if(_mnemonic!=null) {
			return new String(_mnemonic);
		}
		else {
			return null;
		}
	}
	public void setmnemonic(String newValue) {
		_mnemonic = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getadpAcct() {
		if(_adpAcct!=null) {
			return new String(_adpAcct);
		}
		else {
			return null;
		}
	}
	public void setadpAcct(String newValue) {
		_adpAcct = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getessLocation() {
		return _essLocation;
	}
	public void setessLocation(int newValue) {
		_essLocation = newValue;
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
		item.putInt(_bookId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_acctId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_name);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_fullName);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_mnemonic);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_adpAcct);
		da.setDataItem(sa_idx++, item);
		item.putInt(_essLocation);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_bookId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_acctId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_name = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_fullName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_mnemonic = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_adpAcct = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_essLocation = item.getInt();
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
	private int _bookId;
	private int _acctId;
	private char[] _name;
	private char[] _fullName;
	private char[] _mnemonic;
	private char[] _adpAcct;
	private int _essLocation;
	private DataItem item = new DataItem();
}
