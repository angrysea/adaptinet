/**
 *	Copyright (C), 2012 Adaptinet.org 
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */

package org.adaptinet.sdk.adaptinetex;

import org.adaptinet.sdk.adaptinetex.AdaptinetException;

public class FastCacheException extends AdaptinetException {
	private static final long serialVersionUID = 1L;

	public FastCacheException(int hr) {
		super(hr);
	}

	public FastCacheException(String description) {
		super(-1);
		setExtraText(description);
	}

	public FastCacheException(int hr, String description) {
		super(hr);
		setExtraText(description);
	}

	public FastCacheException(int sev, int code) {
		super(sev, FACILITY_COM, code);
	}

	public FastCacheException(int sev, int code, String description) {
		super(sev, FACILITY_COM, code);
		setExtraText(description);
	}
}
