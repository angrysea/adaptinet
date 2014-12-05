// Title:        AltDivCcy
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

public class AltDivCcy implements FastCacheDVOBase  {
	public int getid() {
		return _id;
	}
	public void setid(int newValue) {
		_id = newValue;
	}
	public int getdivId() {
		return _divId;
	}
	public void setdivId(int newValue) {
		_divId = newValue;
	}
	public String getccyCode() {
		if(_ccyCode!=null) {
			return new String(_ccyCode);
		}
		else {
			return null;
		}
	}
	public void setccyCode(String newValue) {
		_ccyCode = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getccyId() {
		return _ccyId;
	}
	public void setccyId(int newValue) {
		_ccyId = newValue;
	}
	public float getgrossRate() {
		return _grossRate;
	}
	public void setgrossRate(float newValue) {
		_grossRate = newValue;
	}
	public float getnetRate() {
		return _netRate;
	}
	public void setnetRate(float newValue) {
		_netRate = newValue;
	}
	public String getcomment() {
		if(_comment!=null) {
			return new String(_comment);
		}
		else {
			return null;
		}
	}
	public void setcomment(String newValue) {
		_comment = newValue!=null ? newValue.toCharArray() : null;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 8);
		item.putInt(_id);
		da.setDataItem(sa_idx++, item);
		item.putInt(_divId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_ccyCode);
		da.setDataItem(sa_idx++, item);
		item.putInt(_ccyId);
		da.setDataItem(sa_idx++, item);
		item.putFloat(_grossRate);
		da.setDataItem(sa_idx++, item);
		item.putFloat(_netRate);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_comment);
		da.setDataItem(sa_idx++, item);
		item.putInt(_location);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_id = item.getInt();
		in.getDataItem(sa_idx++, item);
		_divId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_ccyCode = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_ccyId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_grossRate = item.getFloat();
		in.getDataItem(sa_idx++, item);
		_netRate = item.getFloat();
		in.getDataItem(sa_idx++, item);
		_comment = item.getCharArray();
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
	private int _id;
	private int _divId;
	private char[] _ccyCode;
	private int _ccyId;
	private float _grossRate;
	private float _netRate;
	private char[] _comment;
	private int _location;
	private DataItem item = new DataItem();
}
