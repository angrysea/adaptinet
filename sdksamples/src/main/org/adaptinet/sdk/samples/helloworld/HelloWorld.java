package org.adaptinet.sdk.samples.helloworld;

import javax.swing.*;

import org.adaptinet.sdk.messaging.*;
import org.adaptinet.sdk.processoragent.Processor;

public class HelloWorld extends Processor
{
    boolean packFrame = false;
    HelloWorldFrame frame = null;

    // This method is called by the server for initialization
    public void init()
    {
        frame = new HelloWorldFrame();

        if (packFrame)
            frame.pack();
        else
            frame.validate();
        frame.setVisible(true);
        frame.setPlugin(this);

        // Set the title of the frame to include my hostname and port
        Address address = new Address(this.server);
        frame.setTitle("Hello World - " + address.getHost() + ":" +
            address.getPort());
    }

    // This method is called by the server for cleanup
    public void cleanup()
    {
    }

    // This method is called by the Frame when the Say Hello button
    // is pressed.
    public void sayHelloButton(String to)
    {
        // Create the argument list for the call to another server
        Object[] args = new Object[1];

        // Get the Address for my local machine by passing in the
        // server object
        Address address = new Address(this.server);
        args[0] = "Hello, my name is " + address.getHost() + ":" +
            address.getPort() + ".";

        // The Message object defines who we are going to talk to.  The
        // URI format is as follows:
        //      http://ip-or-host-name:port/plugin/method
        Message message = new Message("http://" + to + "/HelloWorld/Hello",
            this.server);

        try
        {
            // Go ahead and send the message
            postMessage(message, args);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    // This method is called by other servers running the
    // HelloWorld plugin.
    public void Hello(String text)
    {
        // Pop up a dialog showing the text sent to me
        JOptionPane.showMessageDialog(null, "[Message] " + text,
            "New Message Received", JOptionPane.INFORMATION_MESSAGE);
    }
}