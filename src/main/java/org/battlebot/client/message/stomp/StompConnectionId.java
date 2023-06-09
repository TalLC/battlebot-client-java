package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class StompConnectionId extends SimpleDataMessage<String>{
	
	@Override
	public String toString() {
		return "Stomp Connection ID : " + getData().getValue();
	}
}
