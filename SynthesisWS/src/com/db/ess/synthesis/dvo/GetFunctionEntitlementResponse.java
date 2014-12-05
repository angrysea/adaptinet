// Title:        GetFunctionEntitlementResponse
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

public class GetFunctionEntitlementResponse implements FastCacheDVOBase  {
	public ReturnResponse getreturnResponse() { 
		return _returnResponse;
	}
	public void setreturnResponse(ReturnResponse newValue) { 
		_returnResponse = newValue;
	}
	public long getentitlementsCount() { 
		return _entitlementsCount;
	}
	public FunctionEntitlement[] getentitlementsArray() { 
		return _entitlements;
	}
	public void setentitlementsArray(FunctionEntitlement newValue[]) { 
		_entitlements=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator getentitlementsIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_entitlements==null) return false;
				if(last<0) {
					last = _entitlements.length;
					while(--last>-1&&_entitlements[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _entitlements[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_entitlements[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public FunctionEntitlement getentitlements(int idx) { 
		return (FunctionEntitlement)_entitlements[idx];
	}
	public void setentitlements(FunctionEntitlement newValue) { 
		if(_entitlements!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_entitlements.length;__I_A++) {
				if(_entitlements[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_entitlements.length;
				FunctionEntitlement array[] = new FunctionEntitlement[__OPEN_A+10];
				System.arraycopy(_entitlements,0,array,0,_entitlements.length);
				_entitlements = array;
			}
			_entitlements[__OPEN_A] = newValue;
		}
		else {
			_entitlements = new FunctionEntitlement[10];
			_entitlements[0] = newValue;
		}
		_entitlementsCount++;
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
		if(_entitlements!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _entitlementsCount);
			for(int __I_A=0;__I_A<_entitlementsCount;__I_A++) {
				daChild.setDataArray(__I_A, _entitlements[__I_A].writeCache());
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
		_entitlementsCount = daItem.getElements();
		if(_entitlementsCount>0) {
			_entitlements = new FunctionEntitlement [(int)_entitlementsCount];
			for(int __I_A=0;__I_A<_entitlementsCount;__I_A++) {
				_entitlements[__I_A] = new FunctionEntitlement();
				daItem.getDataArray(__I_A, daElement);
				_entitlements[__I_A].readCache(daElement);
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
	private FunctionEntitlement _entitlements[] = null;
	private long _entitlementsCount = 0;
	private DataItem item = new DataItem();
}

