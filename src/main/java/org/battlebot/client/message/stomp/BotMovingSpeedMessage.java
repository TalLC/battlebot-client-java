package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class BotMovingSpeedMessage extends SimpleDataMessage<Float> {
		
	@Override
	public String toString() {
		return "Moviong Speed : " + getData().getValue();
	}
}
