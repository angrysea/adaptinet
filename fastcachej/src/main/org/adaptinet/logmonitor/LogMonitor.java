/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */
package org.adaptinet.logmonitor;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class LogMonitor {

	private String logFile = null;
	BufferedReader reader = null;

	public LogMonitor(String log) throws IOException, FileNotFoundException {
		logFile = log;
		getLog();
	}

	public void getLog() throws FileNotFoundException, IOException {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(
				logFile)));
	}

	public void ShowTable() {
		JFrame frame = new JFrame("Log Monitor");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Column Names
		final String[] names = { "Facility", "Error", "Message", "Time" };
		final Object[][] data = {
				{ "Logger", "-1", "This is a test message.", "10/10/1999" },
				{ "Logger", "-1", "This is another test message.", "10/10/1999" } };

		// Create a model of the data.
		TableModel dataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			// These methods always need to be implemented.
			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return data.length;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			// The default implementations of these methods in
			// AbstractTableModel would work, but we can refine them.
			public String getColumnName(int column) {
				return names[column];
			}

			public Class<?> getColumnClass(int col) {
				return getValueAt(0, col).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return (col == 4);
			}

			public void setValueAt(Object aValue, int row, int column) {
				data[row][column] = aValue;
			}
		};

		JTable tableView = new JTable(dataModel);

		JScrollPane scrollpane = new JScrollPane(tableView);

		scrollpane.setPreferredSize(new Dimension(700, 300));
		frame.getContentPane().add(scrollpane);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			LogMonitor lm = new LogMonitor("c:\\workspace\\fastcachejlog.xml");
			lm.ShowTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
