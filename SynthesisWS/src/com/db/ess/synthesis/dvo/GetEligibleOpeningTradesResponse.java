// Title:        GetEligibleOpeningTradesResponse
// Author:       Generate code
// Company:      
// Description:  This class was generated by the XML-Broker Developer Console
// Schema name:  /home/anthony/workspace/SynthesisWS/schema/Synthesis.xsd
// Java SDK:     

package com.db.ess.synthesis.dvo;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.IOException;

import org.adaptinet.sdk.adaptinetex.FastCacheException;
import org.adaptinet.sdk.fastcache.CacheServer;
import org.adaptinet.sdk.fastcache.DataArray;
import org.adaptinet.sdk.fastcache.DataItem;
import org.adaptinet.sdk.fastcache.FastCacheDVOBase;

public class GetEligibleOpeningTradesResponse implements FastCacheDVOBase  {
	public ReturnResponse getreturnResponse() { 
		return _returnResponse;
	}
	public void setreturnResponse(ReturnResponse newValue) { 
		_returnResponse = newValue;
	}
	public long geteligibleOpeningTradesCount() { 
		return _eligibleOpeningTradesCount;
	}
	public EligibleOpeningTrade[] geteligibleOpeningTradesArray() { 
		return _eligibleOpeningTrades;
	}
	public void seteligibleOpeningTradesArray(EligibleOpeningTrade newValue[]) { 
		_eligibleOpeningTrades=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator geteligibleOpeningTradesIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_eligibleOpeningTrades==null) return false;
				if(last<0) {
					last = _eligibleOpeningTrades.length;
					while(--last>-1&&_eligibleOpeningTrades[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _eligibleOpeningTrades[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_eligibleOpeningTrades[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public EligibleOpeningTrade geteligibleOpeningTrades(int idx) { 
		return (EligibleOpeningTrade)_eligibleOpeningTrades[idx];
	}
	public void seteligibleOpeningTrades(EligibleOpeningTrade newValue) { 
		if(_eligibleOpeningTrades!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_eligibleOpeningTrades.length;__I_A++) {
				if(_eligibleOpeningTrades[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_eligibleOpeningTrades.length;
				EligibleOpeningTrade array[] = new EligibleOpeningTrade[__OPEN_A+10];
				System.arraycopy(_eligibleOpeningTrades,0,array,0,_eligibleOpeningTrades.length);
				_eligibleOpeningTrades = array;
			}
			_eligibleOpeningTrades[__OPEN_A] = newValue;
		}
		else {
			_eligibleOpeningTrades = new EligibleOpeningTrade[10];
			_eligibleOpeningTrades[0] = newValue;
		}
		_eligibleOpeningTradesCount++;
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
		item.putDataArray(_returnResponse.writeCache());
		da.setDataItem(sa_idx++, item);
		if(_eligibleOpeningTrades!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _eligibleOpeningTradesCount);
			for(int __I_A=0;__I_A<_eligibleOpeningTradesCount;__I_A++) {
				daChild.setDataArray(__I_A, _eligibleOpeningTrades[__I_A].writeCache());
			}
			item.putDataArray(daChild);
		}
		else {
			item.putEmpty();
		}
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		DataArray daItem = new DataArray();
		DataArray daElement = new DataArray();
		in.getDataItem(sa_idx++, item);
		item.getDataArray(daItem);
		_returnResponse.readCache(daItem);
		in.getDataItem(sa_idx++, item);
		item.getDataArray(daItem);
		_eligibleOpeningTradesCount = daItem.getElements();
		if(_eligibleOpeningTradesCount>0) {
			_eligibleOpeningTrades = new EligibleOpeningTrade [(int)_eligibleOpeningTradesCount];
			for(int __I_A=0;__I_A<_eligibleOpeningTradesCount;__I_A++) {
				_eligibleOpeningTrades[__I_A] = new EligibleOpeningTrade();
				daItem.getDataArray(__I_A, daElement);
				_eligibleOpeningTrades[__I_A].readCache(daElement);
			}
		}
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
	private ReturnResponse _returnResponse =  new ReturnResponse();
	private EligibleOpeningTrade _eligibleOpeningTrades[] = null;
	private long _eligibleOpeningTradesCount = 0;
	private DataItem item = new DataItem();
}
