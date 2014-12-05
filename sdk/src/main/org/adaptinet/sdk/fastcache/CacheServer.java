/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */
package org.adaptinet.sdk.fastcache;

import java.io.IOException;

import org.adaptinet.sdk.adaptinetex.FastCacheException;

public class CacheServer {

	public native boolean putValue(String key, DataItem value) throws FastCacheException;
	public native boolean removeValue(String key) throws FastCacheException;
	public native DataItem getValue(String key) throws FastCacheException;
	public native DataItem getChildren(String key) throws FastCacheException;
	protected native void init() throws FastCacheException;
	public native void destroy() throws FastCacheException;
	
	public CacheServer() {
		try {
			init();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public byte [] readObject(String key) throws FastCacheException, IOException {
		DataItem di = getChildren(key);
		byte [] buffer = di.marshal();
		di.DataItemClear();
		return buffer;
	}
	
	protected void finalize() throws FastCacheException {
		delete();
	}	
	
	public void delete() throws FastCacheException {

		synchronized (this) {
			if (cacheserver != 0) {
				destroy();
				cacheserver = 0;
			}
		}
	}

	public long getPhysicalCacheServer() {
		return cacheserver;
	}
	
	static {
		System.loadLibrary("fastcache");
	}

	private long cacheserver = 0;

}
