/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */

package org.adaptinet.loggerutils;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import org.adaptinet.fastcachejex.LoggerException;

public class BroadcastMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int ADD = 1;
	public static final int REMOVE = 2;
	public static final int DEFAULT_REQUEST_PORT = 5050;
	public static final String DEFAULT_HOST = "localhost";
	public static final int ACTIVE = 1;
	public static final int INACTIVE = 2;

	public BroadcastMessage(int facility, int severity, String strHost,
			int iPort) throws LoggerException {
		host = strHost;
		port = iPort;

		try {
			init(facility, severity);
		} catch (LoggerException e) {
			System.err.println(e);
			throw e;
		}
	}

	public BroadcastMessage(int facility, int severity) throws LoggerException {
		try {
			init(facility, severity);
		} catch (LoggerException e) {
			System.err.println(e);
			throw e;
		}
	}

	private void init(int facility, int severity) throws LoggerException {
		if (facility >= LoggerException.MIN_FACILITY
				&& facility <= LoggerException.MAX_FACILITY) {
			m_iFacility = facility;
		} else {
			throw new LoggerException(1, 1, "Invalid Facility");
		}

		if (severity >= LoggerException.MIN_SEVERITY
				&& severity <= LoggerException.MAX_SEVERITY) {
			m_iSeverity = severity;
		} else {
			throw new LoggerException(1, 1, "Invalid Severity");
		}

		m_lRequestTime = System.currentTimeMillis();
	}

	public void SetStatus(int iStatus) throws LoggerException {
		if (iStatus == ACTIVE || iStatus == INACTIVE) {
			m_iStatus = iStatus;
		} else {
			throw new LoggerException(1, 1, "Invalid Status");
		}
	}

	public int GetStatus() {
		return m_iStatus;
	}

	public String toString() {
		return "[BroadcastMessage:m_iFacility=" + this.m_iFacility
				+ ",m_iSeverity=" + this.m_iSeverity + ",m_lRequestTime="
				+ this.m_lRequestTime + "]";
	}

	public void setReturnHost(String strHost) {
		host = strHost;
	}

	public String getReturnHost() {
		return host;
	}

	public void setPort(int iPort) {
		port = iPort;
	}

	public int getReturnPort() {
		return port;
	}

	public int getSeverity() {
		return m_iSeverity;
	}

	public int getFacility() {
		return m_iFacility;
	}

	public int getAction() {
		return m_iAction;
	}

	public void setAction(int iAction) throws LoggerException {
		if (iAction == ADD || iAction == REMOVE)
			m_iAction = iAction;
		else {
			throw new LoggerException(1, 1,
					"Invalid Action.  must be 1 [ADD] or 2 [REMOVE]");
		}

	}

	public void send(int iPort, String sHost, int iAction)
			throws LoggerException {

		if (iAction == ADD || iAction == REMOVE) {
			setAction(iAction);
			try {
				Socket socket = new Socket(sHost, iPort);

				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						socket.getOutputStream());
				objectOutputStream.writeObject(this);
				objectOutputStream.flush();
				objectOutputStream.close();
				socket.close();
			} catch (Exception e) {
				throw new LoggerException(1, 1, e.getMessage());
			}
		} else
			throw new LoggerException(1, 1, "Invalid broadcast request action.");
	}

	public boolean equals(Object obj) {
		BroadcastMessage br = (BroadcastMessage) obj;

		if (br.m_iFacility == this.m_iFacility
				&& br.m_iSeverity == this.m_iSeverity
				&& br.host.equals(this.host) && br.port == this.port) {
			return true;
		} else {
			return false;
		}
	}

	private int m_iFacility = -1;
	private int m_iSeverity = -1;
	private long m_lRequestTime = 0;
	private String host;
	private int port;
	private int m_iAction;
	private int m_iStatus = ADD;
}
