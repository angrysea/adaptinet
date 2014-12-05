// Title:        PendingExceptionAlert
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

public class PendingExceptionAlert implements FastCacheDVOBase  {
	public int getPendingLndCount() {
		return _PendingLndCount;
	}
	public void setPendingLndCount(int newValue) {
		_PendingLndCount = newValue;
	}
	public int getPendingNYCount() {
		return _PendingNYCount;
	}
	public void setPendingNYCount(int newValue) {
		_PendingNYCount = newValue;
	}
	public int getTotalPendingCount() {
		return _TotalPendingCount;
	}
	public void setTotalPendingCount(int newValue) {
		_TotalPendingCount = newValue;
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
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 3);
		item.putInt(_PendingLndCount);
		da.setDataItem(sa_idx++, item);
		item.putInt(_PendingNYCount);
		da.setDataItem(sa_idx++, item);
		item.putInt(_TotalPendingCount);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_PendingLndCount = item.getInt();
		in.getDataItem(sa_idx++, item);
		_PendingNYCount = item.getInt();
		in.getDataItem(sa_idx++, item);
		_TotalPendingCount = item.getInt();
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
	private int _PendingLndCount;
	private int _PendingNYCount;
	private int _TotalPendingCount;
	private DataItem item = new DataItem();
}

