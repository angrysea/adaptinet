package org.adaptinet.sdk.fastcache;

import org.adaptinet.sdk.adaptinetex.FastCacheException;

public class DataArrayIterator {
	private DataItem dataItem = null;
	private DataArray dataArray = null;
	private DataArray daEntry = null;
	private DataItem entry = null;
	private long count = 0;
	private long current = 0;

	public DataArrayIterator() {

		try {
			dataItem = new DataItem();
			dataArray = new DataArray();
			daEntry = new DataArray();
			entry = new DataItem();
		} catch (FastCacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void load(byte[] buffer) {

		try {
			current = 0;
			dataItem.DataItemClear();
			dataItem.unMarshal(buffer);
			if ((dataItem.getDataType() & DataItem.DATAITEMARRAY) == DataItem.DATAITEMARRAY) {
				dataItem.getDataArray(dataArray);
				count = dataArray.getElements();
			}
		} catch (FastCacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean hasNext() {
		return current < count ? true : false;
	}

	public boolean next(FastCacheDVOBase obj) {
		boolean idxFound = false;
		try {
			if (current < count) {
				dataArray.getDataItem(current++, entry);
				short type = entry.getDataType();
				if ((type & DataItem.DATAITEMARRAY) == DataItem.DATAITEMARRAY) {
					entry.getDataArray(daEntry);
					obj.readCache(daEntry);
				}
			}
		} catch (FastCacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idxFound;
	}

}
