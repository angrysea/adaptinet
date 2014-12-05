package org.adaptinet.sdk.messaging;

import org.adaptinet.sdk.server.IServer;

public final class Header {

	Message message = null;

	public Header() {
	}

	public Header(IServer server) {
		this(server, false);
	}

	public Header(IServer server, boolean bSecure) {
		message = new Message(server, false);
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
