package org.adaptinet.sdk.processors;

import java.util.LinkedList;

import org.adaptinet.sdk.server.IServer;
import org.adaptinet.sdk.serverutils.Semaphore;
import org.adaptinet.sdk.socket.BaseSocketServer;
import org.adaptinet.sdk.socket.CallbackSocketListener;
import org.adaptinet.sdk.socket.CallbackSocketServer;



public class ProxyClient implements CallbackSocketListener {

	LinkedList<Object> messages = new LinkedList<Object>();

	CallbackSocketServer server = null;

	Semaphore sem = new Semaphore();

	static int staticport = 7070;

	public ProxyClient() {
		server = (CallbackSocketServer) BaseSocketServer
				.createInstance("CALLBACK");
		server.setSocketListener(this);
	}

	public void OnReceive(Object obj) {
		try {
			synchronized (messages) {
				messages.add(obj);
			}
			sem.semPost();
		} catch (Exception e) {
		}
	}

	public Object removeFirst() {

		sem.semWait();

		synchronized (messages) {
			return messages.removeFirst();
		}
	}

	public Object requestProxy() {

		while (true) {
			try {
				requestProxy(staticport);
			} catch (Exception e) {
				staticport++;
				if (staticport > 8000)
					return new Integer(0);
				continue;
			}
			break;
		}
		Integer ret = new Integer(staticport);
		staticport++;
		return ret;
	}

	public void requestProxy(int port) throws Exception {

		server.initialize(IServer.getServer(), staticport, 30);
	}
}