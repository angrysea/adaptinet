// Title:        GetBasketConstituentResponse
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

public class GetBasketConstituentResponse implements FastCacheDVOBase  {
	public long getBasketConstituentCount() { 
		return _BasketConstituentCount;
	}
	public BasketConstituent[] getBasketConstituentArray() { 
		return _BasketConstituent;
	}
	public void setBasketConstituentArray(BasketConstituent newValue[]) { 
		_BasketConstituent=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator getBasketConstituentIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_BasketConstituent==null) return false;
				if(last<0) {
					last = _BasketConstituent.length;
					while(--last>-1&&_BasketConstituent[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _BasketConstituent[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_BasketConstituent[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public BasketConstituent getBasketConstituent(int idx) { 
		return (BasketConstituent)_BasketConstituent[idx];
	}
	public void setBasketConstituent(BasketConstituent newValue) { 
		if(_BasketConstituent!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_BasketConstituent.length;__I_A++) {
				if(_BasketConstituent[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_BasketConstituent.length;
				BasketConstituent array[] = new BasketConstituent[__OPEN_A+10];
				System.arraycopy(_BasketConstituent,0,array,0,_BasketConstituent.length);
				_BasketConstituent = array;
			}
			_BasketConstituent[__OPEN_A] = newValue;
		}
		else {
			_BasketConstituent = new BasketConstituent[10];
			_BasketConstituent[0] = newValue;
		}
		_BasketConstituentCount++;
	}
	public ReturnResponse getReturnResponse() { 
		return _ReturnResponse;
	}
	public void setReturnResponse(ReturnResponse newValue) { 
		_ReturnResponse = newValue;
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
		if(_BasketConstituent!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _BasketConstituentCount);
			for(int __I_A=0;__I_A<_BasketConstituentCount;__I_A++) {
				daChild.setDataArray(__I_A, _BasketConstituent[__I_A].writeCache());
			}
			item.putDataArray(daChild);
		}
		else {
			item.putEmpty();
		}
		da.setDataItem(sa_idx++, item);
		item.putDataArray(_ReturnResponse.writeCache());
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
		_BasketConstituentCount = daItem.getElements();
		if(_BasketConstituentCount>0) {
			_BasketConstituent = new BasketConstituent [(int)_BasketConstituentCount];
			for(int __I_A=0;__I_A<_BasketConstituentCount;__I_A++) {
				_BasketConstituent[__I_A] = new BasketConstituent();
				daItem.getDataArray(__I_A, daElement);
				_BasketConstituent[__I_A].readCache(daElement);
			}
		}
		in.getDataItem(sa_idx++, item);
		item.getDataArray(daItem);
		_ReturnResponse.readCache(daItem);
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
	private BasketConstituent _BasketConstituent[] = new BasketConstituent[10];
	private long _BasketConstituentCount = 0;
	private ReturnResponse _ReturnResponse =  new ReturnResponse();
	private DataItem item = new DataItem();
}
