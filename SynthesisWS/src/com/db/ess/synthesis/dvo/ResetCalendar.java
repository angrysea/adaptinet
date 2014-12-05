// Title:        ResetCalendar
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

public class ResetCalendar implements FastCacheDVOBase  {
	public Date getstartDate() { 
		return _startDate;
	}
	public void setstartDate(Date newValue) { 
		_startDate = newValue;
	}
	public Date getresetDate() { 
		return _resetDate;
	}
	public void setresetDate(Date newValue) { 
		_resetDate = newValue;
	}
	public Date getpayDate() { 
		return _payDate;
	}
	public void setpayDate(Date newValue) { 
		_payDate = newValue;
	}
	public Date getfxDate() { 
		return _fxDate;
	}
	public void setfxDate(Date newValue) { 
		_fxDate = newValue;
	}
	public double getresetValue() { 
		return _resetValue.doubleValue();
	}
	public void setresetValue(double newValue) { 
		_resetValue = Double.valueOf(newValue);
	}
	public String getrowType() { 
		return _rowType;
	}
	public void setrowType(String newValue) { 
		_rowType = newValue;
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
		item.putDate(_startDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_resetDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_payDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_fxDate);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_resetValue);
		da.setDataItem(sa_idx++, item);
		item.putString(_rowType);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_startDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_resetDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_payDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_fxDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_resetValue = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_rowType = item.getString();
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
	private Date _resetDate =  new Date();
	private Date _payDate =  new Date();
	private Date _fxDate =  new Date();
	private Double _resetValue =  Double.valueOf(0);
	private String _rowType =  new String();
	private DataItem item = new DataItem();
}

