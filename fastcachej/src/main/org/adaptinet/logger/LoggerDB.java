/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */

package org.adaptinet.logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.sql.*;

import org.adaptinet.fastcachejex.*;
import org.adaptinet.loggerutils.*;

public class LoggerDB implements ILogger {
	public LoggerDB(String strUID, String strPWD, String strDSN, String strDB)
			throws LoggerException {
		dataSetName = strDSN;
		login = strUID;
		password = strPWD;
		databaseName = strDB;

		try {
			init();
		} catch (LoggerException e) {
			throw e;
		}
	}

	private final void init() throws LoggerException {
		String strClassName = new String();
		try {
			if (System.getProperty("java.vendor").equals("Microsoft Corp.")) {
				strClassName = "com.ms.jdbc.odbc.JdbcOdbcDriver";
			} else {
				strClassName = "sun.jdbc.odbc.JdbcOdbcDriver";
			}

			Class.forName(strClassName);
			databaseURL = new String("JDBC:ODBC:") + dataSetName;
			logConnection = DriverManager.getConnection(databaseURL, login,
					password);

			if (lastMsgHandle == -1) {
				Statement s = logConnection.createStatement();
				ResultSet r = s
						.executeQuery("SELECT MAX(messageHandle) from LogEntryTable");

				if (r.next()) {
					lastMsgHandle = r.getInt(1);
				}

				r.close();
				s.close();
			}

		} catch (SQLException e) {
			String xT = ":";
			while (e != null) {
				xT += e.getMessage();
				xT += e.getErrorCode();
				e = e.getNextException();
			}
			throw new LoggerException(LoggerException.SEVERITY_ERROR,
					LoggerException.LOG_SQLERR, xT);
		} catch (ClassNotFoundException e) {
			throw new LoggerException(LoggerException.SEVERITY_ERROR,
					LoggerException.LOG_SQLERR, "Could not create class "
							+ strClassName);
		}
	}

	public int logMessage(LogEntry le) throws LoggerException {
		PreparedStatement PrStmt = null;
		try {
			PrStmt = logConnection.prepareStatement(strDBinsert);
			PrStmt.setInt(DBCOL_messageHandle, ++lastMsgHandle);
			PrStmt.setInt(DBCOL_severity, le.severity);
			PrStmt.setInt(DBCOL_facility, le.facility);
			PrStmt.setInt(DBCOL_errorCode, le.errorCode);

			if (le.errorMessage != null) {
				PrStmt.setString(DBCOL_errorMessage, le.errorMessage);
			} else {
				PrStmt.setString(DBCOL_errorMessage, " ");
			}

			if (le.extraText != null) {
				PrStmt.setString(DBCOL_extraText, le.extraText);
			} else {
				PrStmt.setString(DBCOL_extraText, " ");
			}

			PrStmt.setString(DBCOL_entryTime,
					new java.util.Date(le.entryTime).toString());
			int rowNum = PrStmt.executeUpdate();
			if (rowNum < 0) {
				System.out.println("Failed SQL INSERT Command. Row Number = "
						+ rowNum);
			}
		} catch (SQLException e) {
			String xT = ":";
			while (e != null) {
				xT += e.getMessage();
				xT += e.getErrorCode();
				e = e.getNextException();
			}
			throw new LoggerException(LoggerException.SEVERITY_ERROR,
					LoggerException.LOG_SQLERR, xT);
		}
		return lastMsgHandle;
	}

	public LogEntry getLogEntryHandle(int messageHandle) throws LoggerException {
		LogEntry tmpEntry = null;

		try {
			ResultSet rs;
			String DBselect = "SELECT * FROM LogEntryTable WHERE messageHandle = "
					+ messageHandle;
			Statement stmt = logConnection.createStatement();
			rs = stmt.executeQuery(DBselect);

			if (rs.next()) {
				tmpEntry = new LogEntry();
				tmpEntry.saved = true;
				tmpEntry.messageHandle = rs.getInt(DBCOL_messageHandle);
				tmpEntry.severity = rs.getInt(DBCOL_severity);
				tmpEntry.facility = rs.getInt(DBCOL_facility);
				tmpEntry.errorCode = rs.getInt(DBCOL_errorCode);
				tmpEntry.errorMessage = rs.getString(DBCOL_errorMessage);
				tmpEntry.extraText = rs.getString(DBCOL_extraText);
				DateFormat df = DateFormat.getDateInstance();
				java.util.Date date = df.parse(rs.getString(DBCOL_entryTime));
				tmpEntry.entryTime = date.getTime();
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			String xT = ":";
			while (e != null) {
				xT += e.getMessage();
				xT += e.getErrorCode();
				e = e.getNextException();
			}
			throw new LoggerException(LoggerException.SEVERITY_ERROR,
					LoggerException.LOG_SQLERR, xT);
		} catch (ParseException e) {
			throw new LoggerException(LoggerException.SEVERITY_ERROR,
					LoggerException.LOG_SQLERR, e.toString());
		}

		return tmpEntry;
	}

	public int getLastMessageHandle() {
		return -1;
	}

	private String dataSetName;
	private String login;
	private String password;
	@SuppressWarnings("unused")
	private String databaseName;

	private String databaseURL;
	private Connection logConnection;
	private int lastMsgHandle = -1;

	static final private String strDBinsert = "INSERT INTO LogEntryTable ( messageHandle, severity, facility, errorCode, errorMessage, extraText, entryTime ) VALUES(?,?,?,?,?,?,?) ";
	static final private int DBCOL_messageHandle = 1;
	static final private int DBCOL_severity = 2;
	static final private int DBCOL_facility = 3;
	static final private int DBCOL_errorCode = 4;
	static final private int DBCOL_errorMessage = 5;
	static final private int DBCOL_extraText = 6;
	static final private int DBCOL_entryTime = 7;
}
