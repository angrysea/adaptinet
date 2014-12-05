package org.adaptinet.sdk.samples.helloworld;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.adaptinet.sdk.processoragent.Processor;

public class HelloWorldFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	Processor plugin = null;

    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    JButton jButtonSayHello = new JButton();
    FlowLayout flowLayout1 = new FlowLayout();
    JTextField jTextServerName = new JTextField();
    JLabel jLabelSName = new JLabel();
    JLabel jLabel2 = new JLabel();

    public void setPlugin(Processor plugin)
    {
        this.plugin = plugin;
    }

    /**Construct the frame*/
    public HelloWorldFrame()
    {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**Component initialization*/
    private void jbInit() throws Exception
    {
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(390, 65));
        this.setTitle("Hello World");
        jPanel1.setLayout(flowLayout1);
        jButtonSayHello.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                jButtonSayHello_actionPerformed(e);
            }
        });
        jTextServerName.setPreferredSize(new Dimension(125, 21));
        jTextServerName.setText("localhost:8082");
        jLabelSName.setText("Talk to Transceiver:");
        jLabel2.setText("          ");
        contentPane.add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(jLabelSName, null);
        jPanel1.add(jTextServerName, null);
        jPanel1.add(jLabel2, null);
        jPanel1.add(jButtonSayHello, null);
        jButtonSayHello.setText("Say Hello");
    }

    // Overridden so we can exit when window is closed
    protected void processWindowEvent(WindowEvent e)
    {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        {
            // Shutdown the TransCeiver when the application is closed
            plugin.shutdown();
        }
    }

    // Called when the "SayHello" button is pressed
    void jButtonSayHello_actionPerformed(ActionEvent e)
    {
        ((HelloWorld)plugin).sayHelloButton(jTextServerName.getText());
   }
}