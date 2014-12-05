// Title:        GetSwapAuditResponse
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

public class GetSwapAuditResponse implements FastCacheDVOBase  {
	public ReturnResponse getreturnResponse() { 
		return _returnResponse;
	}
	public void setreturnResponse(ReturnResponse newValue) { 
		_returnResponse = newValue;
	}
	public long getswapAuditsCount() { 
		return _swapAuditsCount;
	}
	public SwapAudit[] getswapAuditsArray() { 
		return _swapAudits;
	}
	public void setswapAuditsArray(SwapAudit newValue[]) { 
		_swapAudits=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator getswapAuditsIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_swapAudits==null) return false;
				if(last<0) {
					last = _swapAudits.length;
					while(--last>-1&&_swapAudits[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _swapAudits[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_swapAudits[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public SwapAudit getswapAudits(int idx) { 
		return (SwapAudit)_swapAudits[idx];
	}
	public void setswapAudits(SwapAudit newValue) { 
		if(_swapAudits!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_swapAudits.length;__I_A++) {
				if(_swapAudits[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_swapAudits.length;
				SwapAudit array[] = new SwapAudit[__OPEN_A+10];
				System.arraycopy(_swapAudits,0,array,0,_swapAudits.length);
				_swapAudits = array;
			}
			_swapAudits[__OPEN_A] = newValue;
		}
		else {
			_swapAudits = new SwapAudit[10];
			_swapAudits[0] = newValue;
		}
		_swapAuditsCount++;
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
		if(_swapAudits!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _swapAuditsCount);
			for(int __I_A=0;__I_A<_swapAuditsCount;__I_A++) {
				daChild.setDataArray(__I_A, _swapAudits[__I_A].writeCache());
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
		_swapAuditsCount = daItem.getElements();
		if(_swapAuditsCount>0) {
			_swapAudits = new SwapAudit [(int)_swapAuditsCount];
			for(int __I_A=0;__I_A<_swapAuditsCount;__I_A++) {
				_swapAudits[__I_A] = new SwapAudit();
				daItem.getDataArray(__I_A, daElement);
				_swapAudits[__I_A].readCache(daElement);
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
	private SwapAudit _swapAudits[] = null;
	private long _swapAuditsCount = 0;
	private DataItem item = new DataItem();
}
