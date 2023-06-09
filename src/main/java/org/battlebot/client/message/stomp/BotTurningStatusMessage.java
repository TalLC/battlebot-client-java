package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class BotTurningStatusMessage extends SimpleDataMessage<String> {
		
	@Override
	public String toString() {
		return "Is bot turning : " + getData().getValue();
	}
}
