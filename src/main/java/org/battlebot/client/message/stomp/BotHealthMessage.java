package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class BotHealthMessage extends SimpleDataMessage<Integer> {
		
	@Override
	public String toString() {
		return "Health : " + getData().getValue();
	}
}
