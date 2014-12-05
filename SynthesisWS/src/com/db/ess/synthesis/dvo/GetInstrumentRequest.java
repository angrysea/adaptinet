// Title:        GetInstrumentRequest
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

public class GetInstrumentRequest implements FastCacheDVOBase  {
	public int getlocation() {
		return _location;
	}
	public void setlocation(int newValue) {
		_location = newValue;
	}
	public String getisnstrumentName() {
		if(_isnstrumentName!=null) {
			return new String(_isnstrumentName);
		}
		else {
			return null;
		}
	}
	public void setisnstrumentName(String newValue) {
		_isnstrumentName = newValue!=null ? newValue.toCharArray() : null;
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
	public String getissueCountry() {
		if(_issueCountry!=null) {
			return new String(_issueCountry);
		}
		else {
			return null;
		}
	}
	public void setissueCountry(String newValue) {
		_issueCountry = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getextType() {
		if(_extType!=null) {
			return new String(_extType);
		}
		else {
			return null;
		}
	}
	public void setextType(String newValue) {
		_extType = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getextValue() {
		if(_extValue!=null) {
			return new String(_extValue);
		}
		else {
			return null;
		}
	}
	public void setextValue(String newValue) {
		_extValue = newValue!=null ? newValue.toCharArray() : null;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 6);
		item.putInt(_location);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_isnstrumentName);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_description);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_issueCountry);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_extType);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_extValue);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_location = item.getInt();
		in.getDataItem(sa_idx++, item);
		_isnstrumentName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_description = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_issueCountry = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_extType = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_extValue = item.getCharArray();
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
	private int _location;
	private char[] _isnstrumentName;
	private char[] _description;
	private char[] _issueCountry;
	private char[] _extType;
	private char[] _extValue;
	private DataItem item = new DataItem();
}
