package org.battlebot.client.message.stomp;

import org.battlebot.connection.StatusMessageType;

@FunctionalInterface
public interface StatusListener {
	
	void onStatusMessage(Object message, StatusMessageType messageType);
}
