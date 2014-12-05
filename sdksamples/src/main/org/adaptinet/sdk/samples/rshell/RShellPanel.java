package org.adaptinet.sdk.samples.rshell;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

public class RShellPanel extends JPanel {

	private static final long serialVersionUID = 2929482097689626805L;

	private JFrame frame = null;

	public RShellPanel() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertNode(String address) {
		nodeList.addItem(address);
	}

	public void init(String title) {

		frame = new JFrame(title);
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocation(50, 50);
		frame.setSize(400, 150);

		try {

			java.net.URL loc = getClass().getResource("RShellPanel.gif");
			ImageIcon logo = new ImageIcon(loc);
			frame.setIconImage(logo.getImage());
		} catch (Exception e) {
		}

		if (nodeList.getItemCount() > 0) {
			nodeList.setSelectedIndex(0);
		}
		frame.setVisible(true);
	}

	private void jbInit() throws Exception {

		jLabel1.setText("Send To");

		toPanel.setLayout(new FlowLayout());
		jPanel1.setLayout(borderLayout1);
		jLabel2.setText("       ");

		nodeList.setEditable(true);
		nodeList.setAlignmentX(Component.LEFT_ALIGNMENT);
		/*
		 * nodeList.addItemListener(new java.awt.event.ItemListener() { public
		 * void itemStateChanged(ItemEvent e) { boxMethod_itemStateChanged(e); }
		 * });
		 */
		Command.setText("Execute");
		Command.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_Command(e);
			}
		});
		Command.setToolTipText("Execute command on selected node");

		jTextField1.setPreferredSize(new Dimension(120, 21));
		jTextField1.setText("jTextField1");
		jEditorPane1.setText("jEditorPane1");
		toPanel.add(jLabel1, null);
		toPanel.add(nodeList, null);
		toPanel.add(jLabel2, null);
		toPanel.add(jTextField1, null);
		toPanel.add(Command, null);

		this.setLayout(new BorderLayout());
		this.add(toPanel, BorderLayout.NORTH);
		this.add(jPanel1, BorderLayout.CENTER);
		jPanel1.add(jEditorPane1, BorderLayout.CENTER);

	}

	void do_Exit(ActionEvent e) {
		processor.shutdown();
	}

	void do_Command(ActionEvent e) {
		processor.doCommand((String) nodeList.getSelectedItem());
	}

	void setProcessor(RShell processor) {
		this.processor = processor;
	}

	public void setCommandText(String text) {
		jEditorPane1.setText(text);
	}

	RShell processor = null;

	JButton Command = new JButton();

	JPanel toPanel = new JPanel();

	JLabel jLabel1 = new JLabel();

	JPanel jPanel1 = new JPanel();

	BorderLayout borderLayout1 = new BorderLayout();

	JLabel jLabel2 = new JLabel();

	JComboBox<String> nodeList = new JComboBox<String>();

	JTextField jTextField1 = new JTextField();

	JEditorPane jEditorPane1 = new JEditorPane();
}