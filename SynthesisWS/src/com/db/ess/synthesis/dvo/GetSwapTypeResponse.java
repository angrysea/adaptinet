// Title:        GetSwapTypeResponse
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

public class GetSwapTypeResponse implements FastCacheDVOBase  {
	public ReturnResponse getreturnResponse() { 
		return _returnResponse;
	}
	public void setreturnResponse(ReturnResponse newValue) { 
		_returnResponse = newValue;
	}
	public long getswapTypesCount() { 
		return _swapTypesCount;
	}
	public SwapTypes[] getswapTypesArray() { 
		return _swapTypes;
	}
	public void setswapTypesArray(SwapTypes newValue[]) { 
		_swapTypes=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator getswapTypesIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_swapTypes==null) return false;
				if(last<0) {
					last = _swapTypes.length;
					while(--last>-1&&_swapTypes[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _swapTypes[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_swapTypes[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public SwapTypes getswapTypes(int idx) { 
		return (SwapTypes)_swapTypes[idx];
	}
	public void setswapTypes(SwapTypes newValue) { 
		if(_swapTypes!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_swapTypes.length;__I_A++) {
				if(_swapTypes[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_swapTypes.length;
				SwapTypes array[] = new SwapTypes[__OPEN_A+10];
				System.arraycopy(_swapTypes,0,array,0,_swapTypes.length);
				_swapTypes = array;
			}
			_swapTypes[__OPEN_A] = newValue;
		}
		else {
			_swapTypes = new SwapTypes[10];
			_swapTypes[0] = newValue;
		}
		_swapTypesCount++;
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
		if(_swapTypes!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _swapTypesCount);
			for(int __I_A=0;__I_A<_swapTypesCount;__I_A++) {
				daChild.setDataArray(__I_A, _swapTypes[__I_A].writeCache());
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
		_swapTypesCount = daItem.getElements();
		if(_swapTypesCount>0) {
			_swapTypes = new SwapTypes [(int)_swapTypesCount];
			for(int __I_A=0;__I_A<_swapTypesCount;__I_A++) {
				_swapTypes[__I_A] = new SwapTypes();
				daItem.getDataArray(__I_A, daElement);
				_swapTypes[__I_A].readCache(daElement);
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
	private SwapTypes _swapTypes[] = new SwapTypes[10];
	private long _swapTypesCount = 0;
	private DataItem item = new DataItem();
}
