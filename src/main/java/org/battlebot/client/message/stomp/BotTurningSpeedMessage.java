package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class BotTurningSpeedMessage extends SimpleDataMessage<Float> {
		
	@Override
	public String toString() {
		return "Turning Speed : " + getData().getValue();
	}
}
