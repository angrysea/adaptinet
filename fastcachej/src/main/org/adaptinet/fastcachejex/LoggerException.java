/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */

package org.adaptinet.fastcachejex;

import org.adaptinet.sdk.adaptinetex.AdaptinetException;

public class LoggerException extends AdaptinetException {
	private static final long serialVersionUID = 1L;

	public LoggerException(int sev, int code) {
		super(sev, FACILITY_LOGGER, code);
	}

	public LoggerException(int sev, int code, String xT) {
		super(sev, FACILITY_LOGGER, code);
		setExtraText(xT);
	}

	public final String getMessageInternal(int e) {
		String errorMessage = new String("[LOG]");

		switch (e) {
		case MONITOR_ACCESSDENIED:
			errorMessage += "Access Denied for Monitor";
			break;

		case LOG_INFOMSG:
			errorMessage += "Informational message being logged";
			break;

		case LOG_SQLERR:
			errorMessage += "SQL Error caught in Logger";
			break;

		default:
			errorMessage += "Unknown Logger error code";
			break;
		}

		return errorMessage;
	}

	// Public static members defining Error codes
	public final static int LOG_BASE = 0;
	public final static int LOG_INFOMSG = LOG_BASE + 1;
	public final static int LOG_SQLERR = LOG_BASE + 2;
	public final static int MONITOR_ACCESSDENIED = LOG_BASE + 3;
}