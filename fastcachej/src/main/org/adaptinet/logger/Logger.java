/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */

package org.adaptinet.logger;

import java.util.Enumeration;
import java.util.Hashtable;

import org.adaptinet.fastcachejex.LoggerException;
import org.adaptinet.loggerutils.LogEntry;

public class Logger {
	public Logger(String strLogFile) throws LoggerException {
		logFile = strLogFile;
		try {
			logger = new LoggerFile(strLogFile);
			lastMessageHandle = getLastMessageHandle();
		} catch (Exception e) {
			throw new LoggerException(LoggerException.SEVERITY_ERROR,
					LoggerException.FACILITY_LOGGER, e.getMessage());
		}
	}

	public Logger(String strDSN, String strLogin, String strPassword,
			String strDB) throws LoggerException {
		try {
			logger = new LoggerDB(strLogin, strPassword, strDSN, strDB);
			lastMessageHandle = getLastMessageHandle();
		} catch (LoggerException e) {
			throw e;
		}
	}

	public String getMessage(int messageHandle) throws LoggerException {
		String theMessage = InvMsgHnd;

		LogEntry anEntry = getLogEntry(messageHandle);
		if (anEntry != null) {
			theMessage = toString(anEntry);
		}

		return theMessage;
	}

	public String getLastMessage() throws LoggerException {
		String theMessage = NoLastMsg;
		LogEntry anEntry = getLastLogEntry();

		if (anEntry != null) {
			theMessage = getMessage(anEntry.messageHandle);
		}

		return theMessage;
	}

	public int logMessage(LogEntry anEntry) throws LoggerException {
		anEntry.messageHandle = ++lastMessageHandle;
		// anEntry.println();

		try {
			if (bCached) {
				cacheLogEntry(anEntry);
			} else {
				writeLogEntry(anEntry);
			}
		} catch (LoggerException e) {
			throw e;
		}
		return anEntry.messageHandle;
	}

	public int logMessage(int severity, int facility, int errorCode,
			String errorMessage, String extraText) throws LoggerException {
		LogEntry anEntry = new LogEntry();

		anEntry.messageHandle = ++lastMessageHandle;
		anEntry.severity = severity;
		anEntry.facility = facility;
		anEntry.errorCode = errorCode;
		anEntry.errorMessage = errorMessage;
		anEntry.extraText = extraText;

		anEntry.println();

		try {
			if (bCached) {
				cacheLogEntry(anEntry);
			} else {
				writeLogEntry(anEntry);
			}
		} catch (LoggerException e) {
			throw e;
		}

		return anEntry.messageHandle;
	}

	public int logMessage(String message) throws LoggerException {
		try {
			return logMessage(LoggerException.SEVERITY_SUCCESS, // Severity=SUCCESS
					LoggerException.FACILITY_LOGGER, // Facility=LOGGER
					LoggerException.LOG_INFOMSG, // ErrorCode=LOG_INFOMSG
					message, null);
		} catch (LoggerException e) {
			throw e;
		}
	}

	private final String toString(LogEntry anEntry) {
		String theMessage;
		theMessage = LoggerException.getSeverityText(anEntry.severity);
		theMessage += LoggerException.getFacilityText(anEntry.facility);
		theMessage += "[" + new java.util.Date(anEntry.entryTime).toString()
				+ "]";
		theMessage += anEntry.errorMessage;
		if (anEntry.extraText != null) {
			theMessage += " <" + anEntry.extraText + ">";
		}
		return theMessage;
	}

	public final int getLastMessageHandle() {
		if (lastMessageHandle == -1) {
			try {
				lastMessageHandle = logger.getLastMessageHandle();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		return lastMessageHandle;
	}

	private final LogEntry getLogEntry(int messageHandle)
			throws LoggerException {
		LogEntry tmpEntry = null;

		if (bCached) {
			tmpEntry = (LogEntry) logEntries.get(new Integer(messageHandle));
		}

		if (tmpEntry == null) {
			try {
				tmpEntry = logger.getLogEntryHandle(messageHandle);
			} catch (LoggerException e) {
				throw e;
			}
		}
		return tmpEntry;
	}

	private final int cacheLogEntry(LogEntry anEntry) {
		int messageHandle;

		messageHandle = anEntry.messageHandle;
		logEntries.put(new Integer(messageHandle), anEntry);
		putLastLogEntry(anEntry);

		return messageHandle;
	}

	private final int writeLogEntry(LogEntry anEntry) throws LoggerException {
		int iHandle;

		try {
			iHandle = logger.logMessage(anEntry);
			putLastLogEntry(anEntry);
		} catch (LoggerException LE) {
			throw LE;
		}

		return iHandle;
	}

	private final void putLastLogEntry(LogEntry anEntry) {
		lastEntry = anEntry;
	}

	private final LogEntry getLastLogEntry() {
		return lastEntry;
	}

	@SuppressWarnings("unused")
	private final void removeLastLogEntry() {
		lastEntry = null;
	}

	@SuppressWarnings("unused")
	private final void flushCache() throws LoggerException {
		LogEntry anEntry;

		for (Enumeration<Integer> en = logEntries.keys(); en.hasMoreElements();) {
			anEntry = (LogEntry) logEntries.remove(en.nextElement());

			try {
				logger.logMessage(anEntry);
			} catch (LoggerException e) {
				String xT = ":";
				while (e != null) {
					xT += e.getMessage();
					xT += e.getErrorCode();
				}
				throw new LoggerException(LoggerException.SEVERITY_ERROR,
						LoggerException.LOG_SQLERR, xT);
			}
		}
	}

	public void SetCache(boolean bToggle) {
		bCached = bToggle;
	}

	static final private String InvMsgHnd = "[LOG]Invalid Message Handle";
	static final private String NoLastMsg = "[LOG]No Last Message Available";

	private ILogger logger;
	private Hashtable<Integer, LogEntry> logEntries = new Hashtable<Integer, LogEntry>();
	private LogEntry lastEntry;

	private int lastMessageHandle = -1;
	@SuppressWarnings("unused")
	private int m_maxCachedEntries = 0;
	@SuppressWarnings("unused")
	private int m_minCachedEntries = 0;
	@SuppressWarnings("unused")
	private long m_storedWallclockTime = 0;

	@SuppressWarnings("unused")
	private String logFile;
	private boolean bCached = false;
};
