// Title:        AddAltDivCcyRequest
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

public class AddAltDivCcyRequest implements FastCacheDVOBase  {
	public int getlocation() {
		return _location;
	}
	public void setlocation(int newValue) {
		_location = newValue;
	}
	public int getdividendId() {
		return _dividendId;
	}
	public void setdividendId(int newValue) {
		_dividendId = newValue;
	}
	public String getaltCcyCode() {
		if(_altCcyCode!=null) {
			return new String(_altCcyCode);
		}
		else {
			return null;
		}
	}
	public void setaltCcyCode(String newValue) {
		_altCcyCode = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getaltCcyId() {
		return _altCcyId;
	}
	public void setaltCcyId(int newValue) {
		_altCcyId = newValue;
	}
	public float getaltGrossRate() {
		return _altGrossRate;
	}
	public void setaltGrossRate(float newValue) {
		_altGrossRate = newValue;
	}
	public float getaltNetRate() {
		return _altNetRate;
	}
	public void setaltNetRate(float newValue) {
		_altNetRate = newValue;
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
	public String getunderlyingTicker() {
		if(_underlyingTicker!=null) {
			return new String(_underlyingTicker);
		}
		else {
			return null;
		}
	}
	public void setunderlyingTicker(String newValue) {
		_underlyingTicker = newValue!=null ? newValue.toCharArray() : null;
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
	public double getdivCcyGrossRate() {
		return _divCcyGrossRate;
	}
	public void setdivCcyGrossRate(double newValue) {
		_divCcyGrossRate = newValue;
	}
	public double getdivCcyNetRate() {
		return _divCcyNetRate;
	}
	public void setdivCcyNetRate(double newValue) {
		_divCcyNetRate = newValue;
	}
	public String getexDate() {
		if(_exDate!=null) {
			return new String(_exDate);
		}
		else {
			return null;
		}
	}
	public void setexDate(String newValue) {
		_exDate = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getrecordDate() {
		if(_recordDate!=null) {
			return new String(_recordDate);
		}
		else {
			return null;
		}
	}
	public void setrecordDate(String newValue) {
		_recordDate = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getpaymentDate() {
		if(_paymentDate!=null) {
			return new String(_paymentDate);
		}
		else {
			return null;
		}
	}
	public void setpaymentDate(String newValue) {
		_paymentDate = newValue!=null ? newValue.toCharArray() : null;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 16);
		item.putInt(_location);
		da.setDataItem(sa_idx++, item);
		item.putInt(_dividendId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_altCcyCode);
		da.setDataItem(sa_idx++, item);
		item.putInt(_altCcyId);
		da.setDataItem(sa_idx++, item);
		item.putFloat(_altGrossRate);
		da.setDataItem(sa_idx++, item);
		item.putFloat(_altNetRate);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_comment);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_userName);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_underlyingTicker);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_ticker);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_divCcy);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_divCcyGrossRate);
		da.setDataItem(sa_idx++, item);
		item.putDouble(_divCcyNetRate);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_exDate);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_recordDate);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_paymentDate);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_location = item.getInt();
		in.getDataItem(sa_idx++, item);
		_dividendId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_altCcyCode = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_altCcyId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_altGrossRate = item.getFloat();
		in.getDataItem(sa_idx++, item);
		_altNetRate = item.getFloat();
		in.getDataItem(sa_idx++, item);
		_comment = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_userName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_underlyingTicker = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_ticker = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_divCcy = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_divCcyGrossRate = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_divCcyNetRate = item.getDouble();
		in.getDataItem(sa_idx++, item);
		_exDate = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_recordDate = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_paymentDate = item.getCharArray();
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
	private int _dividendId;
	private char[] _altCcyCode;
	private int _altCcyId;
	private float _altGrossRate;
	private float _altNetRate;
	private char[] _comment;
	private char[] _userName;
	private char[] _underlyingTicker;
	private char[] _ticker;
	private char[] _divCcy;
	private double _divCcyGrossRate;
	private double _divCcyNetRate;
	private char[] _exDate;
	private char[] _recordDate;
	private char[] _paymentDate;
	private DataItem item = new DataItem();
}
