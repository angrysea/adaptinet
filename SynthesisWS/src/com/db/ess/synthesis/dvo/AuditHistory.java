// Title:        AuditHistory
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

public class AuditHistory implements FastCacheDVOBase  {
	public Date getEnterTime() { 
		return _EnterTime;
	}
	public void setEnterTime(Date newValue) { 
		_EnterTime = newValue;
	}
	public String getAction() {
		if(_Action!=null) {
			return new String(_Action);
		}
		else {
			return null;
		}
	}
	public void setAction(String newValue) {
		_Action = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getFromValues() {
		if(_FromValues!=null) {
			return new String(_FromValues);
		}
		else {
			return null;
		}
	}
	public void setFromValues(String newValue) {
		_FromValues = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getToValues() {
		if(_ToValues!=null) {
			return new String(_ToValues);
		}
		else {
			return null;
		}
	}
	public void setToValues(String newValue) {
		_ToValues = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getBasketId() {
		return _BasketId;
	}
	public void setBasketId(int newValue) {
		_BasketId = newValue;
	}
	public String getUser() {
		if(_User!=null) {
			return new String(_User);
		}
		else {
			return null;
		}
	}
	public void setUser(String newValue) {
		_User = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getComment() {
		if(_Comment!=null) {
			return new String(_Comment);
		}
		else {
			return null;
		}
	}
	public void setComment(String newValue) {
		_Comment = newValue!=null ? newValue.toCharArray() : null;
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
		item.putDate(_EnterTime);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_Action);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_FromValues);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_ToValues);
		da.setDataItem(sa_idx++, item);
		item.putInt(_BasketId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_User);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_Comment);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_EnterTime = item.getDate();
		in.getDataItem(sa_idx++, item);
		_Action = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_FromValues = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_ToValues = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_BasketId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_User = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_Comment = item.getCharArray();
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
	private Date _EnterTime =  new Date();
	private char[] _Action;
	private char[] _FromValues;
	private char[] _ToValues;
	private int _BasketId;
	private char[] _User;
	private char[] _Comment;
	private DataItem item = new DataItem();
}
