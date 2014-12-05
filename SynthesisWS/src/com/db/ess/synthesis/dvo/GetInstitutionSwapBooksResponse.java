// Title:        GetInstitutionSwapBooksResponse
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

public class GetInstitutionSwapBooksResponse implements FastCacheDVOBase  {
	public ReturnResponse getreturnResponse() { 
		return _returnResponse;
	}
	public void setreturnResponse(ReturnResponse newValue) { 
		_returnResponse = newValue;
	}
	public long getinstitutionSwapBooksCount() { 
		return _institutionSwapBooksCount;
	}
	public InstitutionSwapBook[] getinstitutionSwapBooksArray() { 
		return _institutionSwapBooks;
	}
	public void setinstitutionSwapBooksArray(InstitutionSwapBook newValue[]) { 
		_institutionSwapBooks=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator getinstitutionSwapBooksIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_institutionSwapBooks==null) return false;
				if(last<0) {
					last = _institutionSwapBooks.length;
					while(--last>-1&&_institutionSwapBooks[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _institutionSwapBooks[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_institutionSwapBooks[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public InstitutionSwapBook getinstitutionSwapBooks(int idx) { 
		return (InstitutionSwapBook)_institutionSwapBooks[idx];
	}
	public void setinstitutionSwapBooks(InstitutionSwapBook newValue) { 
		if(_institutionSwapBooks!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_institutionSwapBooks.length;__I_A++) {
				if(_institutionSwapBooks[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_institutionSwapBooks.length;
				InstitutionSwapBook array[] = new InstitutionSwapBook[__OPEN_A+10];
				System.arraycopy(_institutionSwapBooks,0,array,0,_institutionSwapBooks.length);
				_institutionSwapBooks = array;
			}
			_institutionSwapBooks[__OPEN_A] = newValue;
		}
		else {
			_institutionSwapBooks = new InstitutionSwapBook[10];
			_institutionSwapBooks[0] = newValue;
		}
		_institutionSwapBooksCount++;
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
		if(_institutionSwapBooks!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _institutionSwapBooksCount);
			for(int __I_A=0;__I_A<_institutionSwapBooksCount;__I_A++) {
				daChild.setDataArray(__I_A, _institutionSwapBooks[__I_A].writeCache());
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
		_institutionSwapBooksCount = daItem.getElements();
		if(_institutionSwapBooksCount>0) {
			_institutionSwapBooks = new InstitutionSwapBook [(int)_institutionSwapBooksCount];
			for(int __I_A=0;__I_A<_institutionSwapBooksCount;__I_A++) {
				_institutionSwapBooks[__I_A] = new InstitutionSwapBook();
				daItem.getDataArray(__I_A, daElement);
				_institutionSwapBooks[__I_A].readCache(daElement);
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
	private InstitutionSwapBook _institutionSwapBooks[] = null;
	private long _institutionSwapBooksCount = 0;
	private DataItem item = new DataItem();
}

