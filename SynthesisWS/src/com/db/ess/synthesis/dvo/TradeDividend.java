// Title:        TradeDividend
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

public class TradeDividend implements FastCacheDVOBase  {
	public Date getexDate() { 
		return _exDate;
	}
	public void setexDate(Date newValue) { 
		_exDate = newValue;
	}
	public Date getrecordDate() { 
		return _recordDate;
	}
	public void setrecordDate(Date newValue) { 
		_recordDate = newValue;
	}
	public Date getpayDate() { 
		return _payDate;
	}
	public void setpayDate(Date newValue) { 
		_payDate = newValue;
	}
	public String getticker() {
		if(_ticker!=null) {
			return new String(_ticker);
		}
		else {
			return null;
		}
	}
	public void setticker(String newValue) {
		_ticker = newValue!=null ? newValue.toCharArray() : null;
	}
	public double getdivRate() {
		return _divRate;
	}
	public void setdivRate(double newValue) {
		_divRate = newValue;
	}
	public double getaltRate() {
		return _altRate;
	}
	public void setaltRate(double newValue) {
		_altRate = newValue;
	}
	public String getdivCcy() {
		if(_divCcy!=null) {
			return new String(_divCcy);
		}
		else {
			return null;
		}
	}
	public void setdivCcy(String newValue) {
		_divCcy = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getdivAltCcy() {
		if(_divAltCcy!=null) {
			return new String(_divAltCcy);
		}
		else {
			return null;
		}
	}
	public void setdivAltCcy(String newValue) {
		_divAltCcy = newValue!=null ? newValue.toCharArray() : null;
	}
	public double getdivAmount() {
		return _divAmount;
	}
	public void setdivAmount(double newValue) {
		_divAmount = newValue;
	}
	public double getexception() {
		return _exception;
	}
	public void setexception(double newValue) {
		_exception = newValue;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 10);
		item.putDate(_exDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_recordDate);
		da.setDataItem(sa_idx++, item);
		item.putDate(_payDate);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_ticker);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_divRate);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_altRate);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_divCcy);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_divAltCcy);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_divAmount);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_exception);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_exDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_recordDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_payDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_ticker = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_divRate = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_altRate = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_divCcy = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_divAltCcy = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_divAmount = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_exception = item.getDouble();
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
	private Date _exDate =  new Date();
	private Date _recordDate =  new Date();
	private Date _payDate =  new Date();
	private char[] _ticker;
	private double _divRate;
	private double _altRate;
	private char[] _divCcy;
	private char[] _divAltCcy;
	private double _divAmount;
	private double _exception;
	private DataItem item = new DataItem();
}

