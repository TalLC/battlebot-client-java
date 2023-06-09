package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class BotStunningStatusMessage extends SimpleDataMessage<Boolean> {
		
	@Override
	public String toString() {
		return "Stunning : " + getData().getValue();
	}
}
