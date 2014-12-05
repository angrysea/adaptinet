/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */

package org.adaptinet.sdk.fastcache;

import java.util.Date;

import org.adaptinet.sdk.adaptinetex.FastCacheException;

public class DataArray {

	public native Object clone();

	private native void destroy() throws FastCacheException;

	public native void DataArrayFree() throws FastCacheException;

	public native int getDataType() throws FastCacheException;

	public native long getElements() throws FastCacheException;

	public native int getElemSize() throws FastCacheException;

	public native void fromCharArray(char ja[]) throws FastCacheException;

	public native void fromIntArray(int ja[]) throws FastCacheException;

	public native void fromShortArray(short ja[]) throws FastCacheException;

	public native void fromDoubleArray(double ja[]) throws FastCacheException;

	public native void fromStringArray(String ja[]) throws FastCacheException;

	public native void fromByteArray(byte ja[]) throws FastCacheException;

	public native void fromFloatArray(float ja[]) throws FastCacheException;

	public native void fromBooleanArray(boolean ja[])
			throws FastCacheException;

	public native void fromDataItemArray(DataItem ja[])
			throws FastCacheException;

	public native char[] toCharArray() throws FastCacheException;

	public native int[] toIntArray() throws FastCacheException;

	public native short[] toShortArray() throws FastCacheException;

	public native double[] toDoubleArray() throws FastCacheException;

	public native String[] toStringArray() throws FastCacheException;

	public native byte[] toByteArray() throws FastCacheException;

	public native float[] toFloatArray() throws FastCacheException;

	public native boolean[] toBooleanArray() throws FastCacheException;

	public native DataItem[] toDataItemArray() throws FastCacheException;

	public native char getChar(long sa_idx) throws FastCacheException;

	public native void setChar(long sa_idx, char c) throws FastCacheException;

	public native void getChars(long sa_idx, int nelems, char ja[], int ja_start)
			throws FastCacheException;

	public native void setChars(long sa_idx, int nelems, char ja[], int ja_start)
			throws FastCacheException;

	public native int getInt(long sa_idx1) throws FastCacheException;

	public native void setInt(long sa_idx1, int c) throws FastCacheException;

	public native void getInts(long sa_idx, int nelems, int ja[], int ja_start)
			throws FastCacheException;

	public native void setInts(long sa_idx, int nelems, int ja[], int ja_start)
			throws FastCacheException;

	public native short getShort(long sa_idx1) throws FastCacheException;

	public native void setShort(long sa_idx1, short c)
			throws FastCacheException;

	public native void getShorts(long sa_idx, int nelems, short ja[],
			int ja_start) throws FastCacheException;

	public native void setShorts(long sa_idx, int nelems, short ja[],
			int ja_start) throws FastCacheException;

	public native double getDouble(long sa_idx) throws FastCacheException;

	public native long getDateasLong(long sa_idx) throws FastCacheException;

	public native void setDateasLong(long sa_idx1, long c)
			throws FastCacheException;

	public void setDate(long sa_idx1, Date c) throws FastCacheException {
		setDateasLong(sa_idx1, c.getTime());
	}

	public Date getDate(long sa_idx) throws FastCacheException {
		return new Date(getDateasLong(sa_idx));
	}

	public native void setDouble(long sa_idx, double c)
			throws FastCacheException;

	public native void getDoubles(long sa_idx, int nelems, double ja[],
			int ja_start) throws FastCacheException;

	public native void setDoubles(long sa_idx, int nelems, double ja[],
			int ja_start) throws FastCacheException;

	public native String getString(long sa_idx) throws FastCacheException;

	public char[] getCharArray(long sa_idx) throws FastCacheException {
		String s = getString(sa_idx);
		return s.toCharArray();
	}

	public native void setString(long sa_idx, String c)
			throws FastCacheException;

	public native void getStrings(long sa_idx, int nelems, String ja[],
			int ja_start) throws FastCacheException;

	public native void setStrings(long sa_idx, int nelems, String ja[],
			int ja_start) throws FastCacheException;

	public native byte getByte(long sa_idx) throws FastCacheException;

	public native void setByte(long sa_idx, byte c) throws FastCacheException;

	public native void getBytes(long sa_idx, int nelems, byte ja[], int ja_start)
			throws FastCacheException;

	public native void setBytes(long sa_idx, int nelems, byte ja[], int ja_start)
			throws FastCacheException;

	public native float getFloat(long sa_idx) throws FastCacheException;

	public native void setFloat(long sa_idx, float c)
			throws FastCacheException;

	public native void getFloats(long sa_idx, int nelems, float ja[],
			int ja_start) throws FastCacheException;

	public native void setFloats(long sa_idx, int nelems, float ja[],
			int ja_start) throws FastCacheException;

	public native boolean getBoolean(long sa_idx) throws FastCacheException;

	public native void setBoolean(long sa_idx, boolean c)
			throws FastCacheException;

	public native void getBooleans(long sa_idx, int nelems, boolean ja[],
			int ja_start) throws FastCacheException;

	public native void setBooleans(long sa_idx, int nelems, boolean ja[],
			int ja_start) throws FastCacheException;

	public native void getDataItem(long sa_idx, DataItem di)
			throws FastCacheException;

	public native void getDataArray(long sa_idx, DataArray da)
			throws FastCacheException;

	public native void setDataItem(long sa_idx, DataItem c)
			throws FastCacheException;

	public native void setDataArray(long sa_idx, DataArray c)
			throws FastCacheException;

	public native void getDataItems(long sa_idx, int nelems, DataItem ja[],
			int ja_start) throws FastCacheException;

	public native void setDataItems(long sa_idx, int nelems, DataItem ja[],
			int ja_start) throws FastCacheException;

	protected native void init(int vt, long celems) throws FastCacheException;

	public DataArray() throws FastCacheException {
		init(DataItem.DATAITEMEMPTY, 0);
	}

	public DataArray(int vt) throws FastCacheException {
		init(vt, 0);
	}

	public DataArray(int vt, long celems) throws FastCacheException {
		init(vt, celems);
	}

	public DataArray(String s) throws FastCacheException {
		char[] ca = s.toCharArray();
		init(DataItem.DATAITEMUI1, ca.length);
		fromCharArray(ca);
	}

	public long getPhysicalDataArray() {
		return dataarray;
	}

	public String asString() throws FastCacheException {
		if (getDataType() != DataItem.DATAITEMUI1) {
			return null;
		}
		char ja[] = toCharArray();
		return new String(ja);
	}

	protected void finalize() throws FastCacheException {
		delete();
	}

	public void delete() throws FastCacheException {

		synchronized (this) {
			if (dataarray != 0) {
				destroy();
				dataarray = 0;
			}
		}
	}

	public String toString() {
		String s = "";
		try {
			long len = getElements();
			if (len == 1) {
				for (long i = 0; i < len; i++) {
					DataItem v = new DataItem();
					getDataItem(i, v);

					if (((v.getDataType() & DataItem.DATAITEMTYPEMASK) | DataItem.DATAITEMARRAY) == v
							.getDataType()) {
						return s + "[" + v.toDataArray().toString() + "]";
					} else {
						s += " " + v.toString();
					}
				}
			}
		} catch (FastCacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	static {
		System.loadLibrary("fastcache");
	}

	private long dataarray = 0;
}