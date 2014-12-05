// Title:        PositionException
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

public class PositionException implements FastCacheDVOBase  {
	public long getspreadExceptionsCount() { 
		return _spreadExceptionsCount;
	}
	public Exceptions[] getspreadExceptionsArray() { 
		return _spreadExceptions;
	}
	public void setspreadExceptionsArray(Exceptions newValue[]) { 
		_spreadExceptions=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator getspreadExceptionsIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_spreadExceptions==null) return false;
				if(last<0) {
					last = _spreadExceptions.length;
					while(--last>-1&&_spreadExceptions[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _spreadExceptions[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_spreadExceptions[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public Exceptions getspreadExceptions(int idx) { 
		return (Exceptions)_spreadExceptions[idx];
	}
	public void setspreadExceptions(Exceptions newValue) { 
		if(_spreadExceptions!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_spreadExceptions.length;__I_A++) {
				if(_spreadExceptions[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_spreadExceptions.length;
				Exceptions array[] = new Exceptions[__OPEN_A+10];
				System.arraycopy(_spreadExceptions,0,array,0,_spreadExceptions.length);
				_spreadExceptions = array;
			}
			_spreadExceptions[__OPEN_A] = newValue;
		}
		else {
			_spreadExceptions = new Exceptions[10];
			_spreadExceptions[0] = newValue;
		}
		_spreadExceptionsCount++;
	}
	public long getrateExceptionsCount() { 
		return _rateExceptionsCount;
	}
	public Exceptions[] getrateExceptionsArray() { 
		return _rateExceptions;
	}
	public void setrateExceptionsArray(Exceptions newValue[]) { 
		_rateExceptions=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator getrateExceptionsIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_rateExceptions==null) return false;
				if(last<0) {
					last = _rateExceptions.length;
					while(--last>-1&&_rateExceptions[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _rateExceptions[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_rateExceptions[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public Exceptions getrateExceptions(int idx) { 
		return (Exceptions)_rateExceptions[idx];
	}
	public void setrateExceptions(Exceptions newValue) { 
		if(_rateExceptions!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_rateExceptions.length;__I_A++) {
				if(_rateExceptions[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_rateExceptions.length;
				Exceptions array[] = new Exceptions[__OPEN_A+10];
				System.arraycopy(_rateExceptions,0,array,0,_rateExceptions.length);
				_rateExceptions = array;
			}
			_rateExceptions[__OPEN_A] = newValue;
		}
		else {
			_rateExceptions = new Exceptions[10];
			_rateExceptions[0] = newValue;
		}
		_rateExceptionsCount++;
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
		if(_spreadExceptions!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _spreadExceptionsCount);
			for(int __I_A=0;__I_A<_spreadExceptionsCount;__I_A++) {
				daChild.setDataArray(__I_A, _spreadExceptions[__I_A].writeCache());
			}
			item.putDataArray(daChild);
		}
		else {
			item.putEmpty();
		}
		da.setDataItem(sa_idx++, item);
		if(_rateExceptions!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _rateExceptionsCount);
			for(int __I_A=0;__I_A<_rateExceptionsCount;__I_A++) {
				daChild.setDataArray(__I_A, _rateExceptions[__I_A].writeCache());
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
		_spreadExceptionsCount = daItem.getElements();
		if(_spreadExceptionsCount>0) {
			_spreadExceptions = new Exceptions [(int)_spreadExceptionsCount];
			for(int __I_A=0;__I_A<_spreadExceptionsCount;__I_A++) {
				_spreadExceptions[__I_A] = new Exceptions();
				daItem.getDataArray(__I_A, daElement);
				_spreadExceptions[__I_A].readCache(daElement);
			}
		}
		in.getDataItem(sa_idx++, item);
		item.getDataArray(daItem);
		_rateExceptionsCount = daItem.getElements();
		if(_rateExceptionsCount>0) {
			_rateExceptions = new Exceptions [(int)_rateExceptionsCount];
			for(int __I_A=0;__I_A<_rateExceptionsCount;__I_A++) {
				_rateExceptions[__I_A] = new Exceptions();
				daItem.getDataArray(__I_A, daElement);
				_rateExceptions[__I_A].readCache(daElement);
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
	private Exceptions _spreadExceptions[] = null;
	private long _spreadExceptionsCount = 0;
	private Exceptions _rateExceptions[] = null;
	private long _rateExceptionsCount = 0;
	private DataItem item = new DataItem();
}

