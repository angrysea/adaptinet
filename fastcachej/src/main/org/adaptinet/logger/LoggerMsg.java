/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */
package org.adaptinet.logger;

import java.util.*;

public class LoggerMsg extends java.lang.Object {
	public LoggerMsg(int msgType, String objContent) {
	}

	public String getMsgClassType() {
		String strClass = new String();
		if (objContent != null) {
			StringTokenizer classToken = new StringTokenizer(objContent, "^");
			strClass = classToken.nextToken();
		}
		return strClass;
	}

	public void fromString(String strObj) {
		StringTokenizer classToken = new StringTokenizer(strObj, "^");
		String strTmp;

		strTmp = classToken.nextToken();
		if (strTmp.equals(this.getClass().getName())) {
			strTmp = classToken.nextToken();
			msgType = new Integer(strTmp).intValue();

			objContent = classToken.nextToken();
		}
	}

	public String toString() {
		String strObj = new String();
		strObj = this.getClass().getName() + "^" + msgType + "^" + objContent;
		return strObj;
	}

	public int getMsgType() {
		return msgType;
	}

	public String getMsgBody() {
		return objContent;
	}

	private int msgType;
	private String objContent;

	public final static int BDCST_REQUEST = 0;
	public final static int LOG_MESSAGE = 1;
}