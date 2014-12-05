// Title:        InstrumentAudit
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

public class InstrumentAudit implements FastCacheDVOBase  {
	public int getinstrumentId() {
		return _instrumentId;
	}
	public void setinstrumentId(int newValue) {
		_instrumentId = newValue;
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
	public Date getauditTime() {
		return _auditTime;
	}
	public void setauditTime(Date newValue) {
		_auditTime = newValue;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 4);
		item.putInt(_instrumentId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_userName);
		da.setDataItem(sa_idx++, item);
		item.putDate(_auditTime);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_comment);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_instrumentId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_userName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_auditTime = item.getDate();
		in.getDataItem(sa_idx++, item);
		_comment = item.getCharArray();
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
	private int _instrumentId;
	private char[] _userName;
	private Date _auditTime;
	private char[] _comment;
	private DataItem item = new DataItem();
}

