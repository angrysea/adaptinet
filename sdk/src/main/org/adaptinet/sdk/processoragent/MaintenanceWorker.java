package org.adaptinet.sdk.processoragent;

import org.adaptinet.sdk.messaging.Address;
import org.adaptinet.sdk.messaging.Message;
import org.adaptinet.sdk.messaging.Messenger;
import org.adaptinet.sdk.server.IServer;


public final class MaintenanceWorker extends Worker{

	public MaintenanceWorker(Address address) {
		super(address);
	}

	public void doPing() {
		Message message = null;

		try {
			message = new Message(address.getURL() + "/" + 
					ProcessorFactory.MAINTENANCE + "/ping",
					IServer.getServer());
			message.getReplyTo().setProcessor(ProcessorFactory.MAINTENANCE);
			bresponded = false;
			starttime = System.currentTimeMillis();
			Messenger.sendMessage(message);
		} catch (Exception e) {
			starttime = 0;
			endtime = 0;
		}
	}
}
