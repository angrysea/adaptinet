/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */
package org.adaptinet.loggerutils;

import java.io.PrintStream;
import java.io.Serializable;

import org.adaptinet.fastcachejex.LoggerException;

public class LogEntry implements Serializable {

	private static final long serialVersionUID = 1L;
	private PrintStream ps = null;
	public boolean saved;
	public int messageHandle;
	public int severity;
	public int facility;
	public int errorCode;
	public String errorMessage;
	public String extraText;
	public long entryTime;
	public String clsName = getClass().getName();

	public LogEntry() {
		saved = false;
		messageHandle = -1;
		severity = -1;
		facility = -1;
		errorCode = -1;
		entryTime = System.currentTimeMillis();
	}

	public LogEntry(boolean bSaved, int iMessageHandle, int iSeverity,
			int iFacility, int iErrorCode, String strErrorMessage,
			String strExtraText) {
		saved = bSaved;
		messageHandle = iMessageHandle;
		severity = iSeverity;
		facility = iFacility;
		errorCode = iErrorCode;
		entryTime = System.currentTimeMillis();
		errorMessage = strErrorMessage;
		extraText = strExtraText;
	}

	public String toString() {
		return "[LogEntry:saved=" + saved + ",messageHandle=" + messageHandle
				+ ",severity=" + severity + ",facility=" + facility
				+ ",errorCode=" + errorCode + ",errorMessage=" + errorMessage
				+ ",extraText=" + extraText + ",entryTime=" + entryTime
				+ ",clsName=" + clsName + "]";
	}

	public void println() {
		System.out.println("LogEntry:");
		// System.out.println("\tsaved=" + saved);
		// System.out.println("\tmessageHandle=" + messageHandle);
		System.out.println("\tseverity="
				+ LoggerException.getSeverityText(severity));
		System.out.println("\tfacility="
				+ LoggerException.getFacilityText(facility));
		System.out.println("\terrorCode=" + errorCode);
		System.out.println("\terrorMessage=" + errorMessage);
		System.out.println("\textraText=" + extraText);
		System.out.println("\tentryTime=" + dateAsString());
		System.out.println("");
	}

	private String toXML() {
		StringBuffer strBuff = new StringBuffer(512);
		strBuff.append("<Record>");
		strBuff.append("<Handle>");
		strBuff.append("</Handle>");
		strBuff.append("<DateTime>"
				+ new java.util.Date(System.currentTimeMillis()).toString()
				+ "</DateTime>");
		strBuff.append("<Severity>" + severity + "</Severity>");
		strBuff.append("<Facility>" + facility + "</Facility>");
		strBuff.append("<ErrorCode>" + errorCode + "</ErrorCode>");

		if (errorMessage != null) {
			errorMessage = errorMessage.replace('<', ' ');
			errorMessage = errorMessage.replace('>', ' ');
			errorMessage = errorMessage.replace('&', ' ');
			strBuff.append("<ErrorMsg>" + errorMessage + "</ErrorMsg>");
		}

		if (extraText != null) {
			extraText = extraText.replace('<', ' ');
			extraText = extraText.replace('>', ' ');
			extraText = extraText.replace('&', ' ');
			strBuff.append("<ExtraText>" + extraText + "</ExtraText>");
		}
		strBuff.append("</Record>\n");
		String retval = strBuff.toString();
		return retval.replace('\n', ' '); // Newlines not allowed in xml log
	}

	public String dateAsString() {
		return new java.util.Date(entryTime).toString();
	}

	public void LogMessage(String hostName, int port) {
		try {
			/*
			if (ps == null)
				ps = XMLServer.getLogStream();
			*/
			ps.println(this.toXML());

		} catch (Exception e) {
			// can't exactly log here because this is the LogEntry that is
			// failing...
			e.printStackTrace();
		}
	}
}