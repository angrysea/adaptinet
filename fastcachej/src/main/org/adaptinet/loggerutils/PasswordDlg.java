/**
 *	Copyright (C), 2012 Adaptinet.org
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *  
 */

package org.adaptinet.loggerutils;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PasswordDlg extends JDialog {

	private static final long serialVersionUID = 1L;
	JPanel panel1 = new JPanel();
	JLabel lblUserName = new JLabel();
	FlowLayout flowLayout1 = new FlowLayout();
	JTextField txtUserName = new JTextField();
	JButton btnOK = new JButton();
	JButton btnCancel = new JButton();
	JLabel lblPass = new JLabel();
	JPasswordField txtPassword = new JPasswordField();

	public PasswordDlg(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			pack();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public PasswordDlg() {
		this(null, "", false);
	}

	public String getUsername() {
		return txtUserName.getText();
	}

	public String getPassword() {
		return new String(txtPassword.getPassword());
	}

	void jbInit() throws Exception {
		this.setModal(true);
		this.setResizable(false);
		panel1.setLayout(flowLayout1);
		lblUserName.setText("Username:");
		txtUserName.setPreferredSize(new Dimension(150, 21));
		btnOK.setText("OK");
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOK_actionPerformed(e);
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancel_actionPerformed(e);
			}
		});
		lblPass.setText("Password:");
		txtPassword.setPreferredSize(new Dimension(150, 21));
		getContentPane().add(panel1);
		panel1.add(lblUserName, null);
		panel1.add(txtUserName, null);
		panel1.add(lblPass, null);
		panel1.add(txtPassword, null);
		panel1.add(btnOK, null);
		panel1.add(btnCancel, null);
		this.getRootPane().setDefaultButton(this.btnOK);
		this.txtPassword.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER)
					btnOK.doClick();
			}
		});
		this.txtUserName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER)
					txtPassword.requestFocus();
			}
		});
		this.txtUserName.setFocusable(true);
	}

	void btnOK_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	void btnCancel_actionPerformed(ActionEvent e) {
		this.txtUserName.setText("");
		this.setVisible(false);
	}
}