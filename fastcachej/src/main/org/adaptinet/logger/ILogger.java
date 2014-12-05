/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */
package org.adaptinet.logger;

import org.adaptinet.fastcachejex.*;
import org.adaptinet.loggerutils.*;


public interface ILogger {
	public int logMessage(LogEntry le) throws LoggerException;

	public int getLastMessageHandle();

	public LogEntry getLogEntryHandle(int messageHandle) throws LoggerException;
}
