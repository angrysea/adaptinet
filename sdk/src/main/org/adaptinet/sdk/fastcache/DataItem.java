/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */

package org.adaptinet.sdk.fastcache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

import org.adaptinet.sdk.adaptinetex.FastCacheException;

public class DataItem {

	public static final int DATAITEMEMPTY = 0;
	public static final int DATAITEMNULL = 1;
	public static final int DATAITEMI2 = 2;
	public static final int DATAITEMI4 = 3;
	public static final int DATAITEMR4 = 4;
	public static final int DATAITEMR8 = 5;
	public static final int DATAITEMDATE = 7;
	public static final int DATAITEMSSTR = 8;
	public static final int DATAITEMERROR = 10;
	public static final int DATAITEMBOOL = 11;
	public static final int DATAITEMDATAITEM = 12;
	public static final int DATAITEMDECIMAL = 14;
	public static final int DATAITEMI1 = 16;
	public static final int DATAITEMUI1 = 17;
	public static final int DATAITEMUI2 = 18;
	public static final int DATAITEMUI4 = 19;
	public static final int DATAITEMI8 = 20;
	public static final int DATAITEMUI8 = 21;
	public static final int DATAITEMINT = 22;
	public static final int DATAITEMUINT = 23;
	public static final int DATAITEMVOID = 24;
	public static final int DATAITEMLONG = 25;
	public static final int DATAITEMPTR = 26;
	public static final int DATAITEMLPSTR = 30;
	public static final int DATAITEMINT_PTR = 37;
	public static final int DATAITEMUINT_PTR = 38;
	public static final int DATAITEMLONG_PTR = 39;
	public static final int DATAITEMDOUBLE_PTR = 64;
	public static final int DATAITEMVECTOR = 0x1000;
	public static final int DATAITEMARRAY = 0x2000;
	public static final int DATAITEMBYREF = 0x4000;
	public static final int DATAITEMRESERVED = 0x8000;
	public static final int DATAITEMILLEGAL = 0xffff;
	public static final int DATAITEMILLEGALMASKED = 0xfff;
	public static final int DATAITEMTYPEMASK = 0xfff;

	public native byte toByte() throws FastCacheException;

	public native boolean toBoolean() throws FastCacheException;

	public native short toShort() throws FastCacheException;

	public native int toInt() throws FastCacheException;

	public native float toFloat() throws FastCacheException;

	public native double toDouble() throws FastCacheException;

	public native int toError() throws FastCacheException;

	public native long toDate() throws FastCacheException;

	public native String toString();

	public native DataArray toDataArray() throws FastCacheException;

	public native void putEmpty() throws FastCacheException;

	public native void putBoolean(boolean in) throws FastCacheException;

	public native void putByte(byte in) throws FastCacheException;

	public native void putChar(char in) throws FastCacheException;

	public native void putShort(short in) throws FastCacheException;

	public native void putLong(long in) throws FastCacheException;

	public native void putInt(int in) throws FastCacheException;

	public native void putFloat(float in) throws FastCacheException;

	public native void putDouble(double in) throws FastCacheException;

	public native void putError(int in) throws FastCacheException;

	public native void putDate(long in) throws FastCacheException;

	public void putDate(Date in) throws FastCacheException {
		if (in != null) {
			putDate(in.getTime());
		}
	}

	public native void putString(String in) throws FastCacheException;

	public void putString(char[] in) throws FastCacheException {
		if (in != null) {
			putString(in.toString());
		}
	}

	public void putCharArray(char[] in) throws FastCacheException {
		if (in != null) {
			putString(new String(in));
		}
	}

	public native void putDataArray(DataArray in) throws FastCacheException;

	public native byte getByte() throws FastCacheException;

	public native char getChar() throws FastCacheException;

	public native boolean getBoolean() throws FastCacheException;

	public native int getInt() throws FastCacheException;

	public native short getShort() throws FastCacheException;

	public native long getLong() throws FastCacheException;

	public native float getFloat() throws FastCacheException;

	public native double getDouble() throws FastCacheException;

	public native long getError() throws FastCacheException;

	public native long getDateasLong() throws FastCacheException;

	public Date getDate() throws FastCacheException {
		return new Date(getDateasLong());
	}

	public native String getString() throws FastCacheException;

	public char[] getCharArray() throws FastCacheException {
		return toString().toCharArray();
	}

	public native void getDataArray(DataArray da) throws FastCacheException;

	public native void noParam() throws FastCacheException;

	public native short getDataType() throws FastCacheException;

	public native void DataItemClear() throws FastCacheException;

	private native void release() throws FastCacheException;

	protected native void init() throws FastCacheException;

	public native Object clone();

	public native void changeType(short in) throws FastCacheException;

	public native byte[] Save() throws IOException,
			FastCacheException;

	public native void Load(ByteArrayInputStream is) throws IOException;

	public native DataItem cloneIndirect() throws FastCacheException;

	public native int unMarshal(byte[] in) throws FastCacheException;

	public native byte[] marshal() throws FastCacheException;

	public void putDataItemArray(DataItem[] in) throws FastCacheException {
		throw new FastCacheException("Not implemented");
	}

	public DataItem[] getDataItemArray() throws FastCacheException {
		throw new FastCacheException("Not implemented");
	}

	public void putByteArray(Object in) throws FastCacheException {
		throw new FastCacheException("Not implemented");
	}

	public void putDataItemArrayRef(DataItem[] in) throws FastCacheException {
		throw new ClassCastException("Not implemented");
	}

	public DataItem[] getDataItemArrayRef() throws FastCacheException {
		throw new ClassCastException("Not implemented");
	}

	public DataItem[] toDataItemArray() throws FastCacheException {
		throw new ClassCastException("Not implemented");
	}

	public Object toByteArray() throws FastCacheException {
		throw new ClassCastException("Not implemented");
	}

	public void changeType(int in) throws FastCacheException {
		changeType((short) in);
	}

	public DataItem() {
		try {
			init();
			putEmpty();
		} catch (FastCacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DataItem(int in) throws FastCacheException {
		init();
		putInt(in);
	}

	public DataItem(double in) throws FastCacheException {
		init();
		putDouble(in);
	}

	public DataItem(boolean in) throws FastCacheException {
		init();
		putBoolean(in);
	}

	public DataItem(String in) throws FastCacheException {
		init();
		putString(in);
	}

	protected void finalize() throws FastCacheException {
		delete();
	}

	public void delete() throws FastCacheException {
		synchronized (this) {
			if (dataitem != 0) {
				release();
				dataitem = 0;
			}
		}
	}

	public void writeObject(byte[] b) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(b);
			Load(bis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] readObject() throws FastCacheException, IOException {
		return Save();
	}

	static {
		System.loadLibrary("fastcache");
	}

	private long dataitem = 0;
}
