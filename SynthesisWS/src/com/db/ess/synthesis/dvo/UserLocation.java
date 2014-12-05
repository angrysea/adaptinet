// Title:        UserLocation
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

public class UserLocation implements FastCacheDVOBase  {
	public String getlocalUserName() {
		if(_localUserName!=null) {
			return new String(_localUserName);
		}
		else {
			return null;
		}
	}
	public void setlocalUserName(String newValue) {
		_localUserName = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getlocation() {
		if(_location!=null) {
			return new String(_location);
		}
		else {
			return null;
		}
	}
	public void setlocation(String newValue) {
		_location = newValue!=null ? newValue.toCharArray() : null;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 2);
		item.putCharArray(_localUserName);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_location);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_localUserName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_location = item.getCharArray();
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
	private char[] _localUserName;
	private char[] _location;
	private DataItem item = new DataItem();
}

