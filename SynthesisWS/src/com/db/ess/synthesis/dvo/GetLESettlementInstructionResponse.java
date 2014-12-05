// Title:        GetLESettlementInstructionResponse
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

public class GetLESettlementInstructionResponse implements FastCacheDVOBase  {
	public ReturnResponse getreturnResponse() { 
		return _returnResponse;
	}
	public void setreturnResponse(ReturnResponse newValue) { 
		_returnResponse = newValue;
	}
	public long getsettlementInstructionsCount() { 
		return _settlementInstructionsCount;
	}
	public LESettlementInstruction[] getsettlementInstructionsArray() { 
		return _settlementInstructions;
	}
	public void setsettlementInstructionsArray(LESettlementInstruction newValue[]) { 
		_settlementInstructions=newValue;
	}
	@SuppressWarnings("rawtypes")
	public Iterator getsettlementInstructionsIterator() { 
		return new Iterator() {
			int cursor=0;
			int last=-1;
			public boolean hasNext() {
				if(_settlementInstructions==null) return false;
				if(last<0) {
					last = _settlementInstructions.length;
					while(--last>-1&&_settlementInstructions[last]==null);
					last++;
				}
				return cursor!=last;
			}
			public Object next() {
				try {
					return _settlementInstructions[cursor++];
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
			public void remove() {
				try {
					_settlementInstructions[cursor++]=null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException();
				}
			}
		};
	}
	public LESettlementInstruction getsettlementInstructions(int idx) { 
		return (LESettlementInstruction)_settlementInstructions[idx];
	}
	public void setsettlementInstructions(LESettlementInstruction newValue) { 
		if(_settlementInstructions!=null) {
			int __OPEN_A=-1;
			for(int __I_A=0;__I_A<_settlementInstructions.length;__I_A++) {
				if(_settlementInstructions[__I_A]==null) {
					__OPEN_A=__I_A;
					break;
				}
			}
			if(__OPEN_A<0) {
				__OPEN_A=_settlementInstructions.length;
				LESettlementInstruction array[] = new LESettlementInstruction[__OPEN_A+10];
				System.arraycopy(_settlementInstructions,0,array,0,_settlementInstructions.length);
				_settlementInstructions = array;
			}
			_settlementInstructions[__OPEN_A] = newValue;
		}
		else {
			_settlementInstructions = new LESettlementInstruction[10];
			_settlementInstructions[0] = newValue;
		}
		_settlementInstructionsCount++;
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
		if(_settlementInstructions!=null) {
			DataArray daChild = new DataArray(DataItem.DATAITEMARRAY, _settlementInstructionsCount);
			for(int __I_A=0;__I_A<_settlementInstructionsCount;__I_A++) {
				daChild.setDataArray(__I_A, _settlementInstructions[__I_A].writeCache());
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
		_settlementInstructionsCount = daItem.getElements();
		if(_settlementInstructionsCount>0) {
			_settlementInstructions = new LESettlementInstruction [(int)_settlementInstructionsCount];
			for(int __I_A=0;__I_A<_settlementInstructionsCount;__I_A++) {
				_settlementInstructions[__I_A] = new LESettlementInstruction();
				daItem.getDataArray(__I_A, daElement);
				_settlementInstructions[__I_A].readCache(daElement);
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
	private LESettlementInstruction _settlementInstructions[] = null;
	private long _settlementInstructionsCount = 0;
	private DataItem item = new DataItem();
}
