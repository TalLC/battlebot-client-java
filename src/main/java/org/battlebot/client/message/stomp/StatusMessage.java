package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class StatusMessage extends SimpleDataMessage<String> {
	
	@Override
	public String toString() {
		return "Status : "+ getData().getValue();
	}
}
