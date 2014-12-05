/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */
package org.adaptinet.sdk.samples.fastcache;

import org.adaptinet.sdk.adaptinetex.FastCacheException;
import org.adaptinet.sdk.fastcache.CacheServer;
import org.adaptinet.sdk.fastcache.DataItem;

public class Sample1 {

	public native int intMethod(int n);

	public native boolean booleanMethod(boolean bool);

	public native String stringMethod(String text);

	public native int intArrayMethod(int[] intArray);

	public static void main(String[] args) throws FastCacheException {
		try {
			System.loadLibrary("fastcache");
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}

		DataItem item = new DataItem("This is a test of the JNI methods.");
		
		String key = "ess.RTC_CashFlow.1433";
		CacheServer server = new CacheServer();
		server.putValue(key, item);
		
		DataItem item2 = server.getValue(key);
		
		System.out.println("Item 1: " + item.getString());
		System.out.println("Item 2: " + item2.getString());
		item.delete();
		item2.delete();
		server.delete();
	}
}
