// Title:        GetCountriesResponse
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

public class GetCountriesResponse implements FastCacheDVOBase  {
	public ReturnResponse getreturnResponse() { 
		return _returnResponse;
	}
	public void setreturnResponse(ReturnResponse newValue) { 
		_returnResponse = newValue;
	}
	public long getcountryCount() { 
		return _countryCount;
	}
	public Country[] getcountryArray() { 
		return _country;
	}
	public void setcountryArray(Country newValue[]) { 
		_country=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator getcountryIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_country==null) return false;
				if(last<0) {
					last = _country.length;
					while(--last>-1&&_country[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _country[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_country[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public Country getcountry(int idx) { 
		return (Country)_country[idx];
	}
	public void setcountry(Country newValue) { 
		if(_country!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_country.length;__I_A++) {
				if(_country[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_country.length;
				Country array[] = new Country[__OPEN_A+10];
				System.arraycopy(_country,0,array,0,_country.length);
				_country = array;
			}
			_country[__OPEN_A] = newValue;
		}
		else {
			_country = new Country[10];
			_country[0] = newValue;
		}
		_countryCount++;
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
		if(_country!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _countryCount);
			for(int __I_A=0;__I_A<_countryCount;__I_A++) {
				daChild.setDataArray(__I_A, _country[__I_A].writeCache());
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
		_countryCount = daItem.getElements();
		if(_countryCount>0) {
			_country = new Country [(int)_countryCount];
			for(int __I_A=0;__I_A<_countryCount;__I_A++) {
				_country[__I_A] = new Country();
				daItem.getDataArray(__I_A, daElement);
				_country[__I_A].readCache(daElement);
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
	private Country _country[] = null;
	private long _countryCount = 0;
	private DataItem item = new DataItem();
}
