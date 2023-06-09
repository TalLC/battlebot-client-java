package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class GameStatusMessage extends SimpleDataMessage<Boolean> {
		
	@Override
	public String toString() {
		return "Status ["  + getMsg_type() +"] : " + getData().getValue();
	}
}
