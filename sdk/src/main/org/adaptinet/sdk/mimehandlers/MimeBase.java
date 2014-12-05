package org.adaptinet.sdk.mimehandlers;

import java.io.ByteArrayOutputStream;

import org.adaptinet.sdk.http.Request;
import org.adaptinet.sdk.server.IServer;


public interface MimeBase {
	public abstract ByteArrayOutputStream process(IServer server,
			Request request);

	public abstract int getStatus();

	public abstract void init(String u, IServer s);

	public String getContentType();
}
