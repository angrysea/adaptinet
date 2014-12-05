// Title:        CacheEntry
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

public class CacheEntry implements FastCacheDVOBase  {
	public int getkey() {
		return _key;
	}
	public void setkey(int newValue) {
		_key = newValue;
	}
	public String getvalue() {
		if(_value!=null) {
			return new String(_value);
		}
		else {
			return null;
		}
	}
	public void setvalue(String newValue) {
		_value = newValue!=null ? newValue.toCharArray() : null;
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
	public int getlocation() {
		return _location;
	}
	public void setlocation(int newValue) {
		_location = newValue;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 5);
		item.putInt(_key);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_value);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_type);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_description);
		da.setDataItem(sa_idx++, item);
		item.putInt(_location);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_key = item.getInt();
		in.getDataItem(sa_idx++, item);
		_value = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_type = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_description = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_location = item.getInt();
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
	private int _key;
	private char[] _value;
	private char[] _type;
	private char[] _description;
	private int _location;
	private DataItem item = new DataItem();
}
