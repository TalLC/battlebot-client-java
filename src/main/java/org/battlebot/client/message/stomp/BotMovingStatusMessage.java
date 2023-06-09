package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class BotMovingStatusMessage extends SimpleDataMessage<Boolean> {
		
	@Override
	public String toString() {
		return "Is bot moving : " + getData().getValue();
	}
}
