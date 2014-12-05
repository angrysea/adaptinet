// Title:        GetSecurityIdMappingResponse
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

public class GetSecurityIdMappingResponse implements FastCacheDVOBase  {
	public long getsecurityIdMappingCount() { 
		return _securityIdMappingCount;
	}
	public String[] getsecurityIdMappingArray() { 
		return _securityIdMapping;
	}
	public void setsecurityIdMappingArray(String newValue[]) { 
		_securityIdMapping=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator getsecurityIdMappingIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_securityIdMapping==null) return false;
				if(last<0) {
					last = _securityIdMapping.length;
					while(--last>-1&&_securityIdMapping[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _securityIdMapping[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_securityIdMapping[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public String getsecurityIdMapping(int idx) { 
		return (String)_securityIdMapping[idx];
	}
	public void setsecurityIdMapping(String newValue) { 
		if(_securityIdMapping!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_securityIdMapping.length;__I_A++) {
				if(_securityIdMapping[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_securityIdMapping.length;
				String array[] = new String[__OPEN_A+10];
				System.arraycopy(_securityIdMapping,0,array,0,_securityIdMapping.length);
				_securityIdMapping = array;
			}
			_securityIdMapping[__OPEN_A] = newValue;
		}
		else {
			_securityIdMapping = new String[10];
			_securityIdMapping[0] = newValue;
		}
		_securityIdMappingCount++;
	}
	public ReturnResponse getreturnResponse() { 
		return _returnResponse;
	}
	public void setreturnResponse(ReturnResponse newValue) { 
		_returnResponse = newValue;
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
		if(_securityIdMapping!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _securityIdMappingCount);
			for(int __I_A=0;__I_A<_securityIdMappingCount;__I_A++) {
				daChild.setString(__I_A, _securityIdMapping[__I_A]);
			}
			item.putDataArray(daChild);
		}
		else {
			item.putEmpty();
		}
		da.setDataItem(sa_idx++, item);
		item.putDataArray(_returnResponse.writeCache());
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		DataArray daItem = new DataArray();
		in.getDataItem(sa_idx++, item);
		item.getDataArray(daItem);
		_securityIdMappingCount = daItem.getElements();
		if(_securityIdMappingCount>0) {
			_securityIdMapping = new String [(int)_securityIdMappingCount];
			for(int __I_A=0;__I_A<_securityIdMappingCount;__I_A++) {
				_securityIdMapping[__I_A] = new String(daItem.getString(__I_A));
			}
		}
		in.getDataItem(sa_idx++, item);
		item.getDataArray(daItem);
		_returnResponse.readCache(daItem);
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
	private String _securityIdMapping[] = null;
	private long _securityIdMappingCount = 0;
	private ReturnResponse _returnResponse =  new ReturnResponse();
	private DataItem item = new DataItem();
}

