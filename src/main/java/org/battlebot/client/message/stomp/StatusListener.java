package org.battlebot.client.message.stomp;

@FunctionalInterface
public interface StatusListener {
	
	void onStatusMessage(StatusMessage status);
}
