package org.adaptinet.sdk.serverutils;

public interface EventHandler {
	void handleTimerEvent(Object data, long time);
}