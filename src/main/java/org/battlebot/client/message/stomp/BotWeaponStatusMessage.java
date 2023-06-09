package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class BotWeaponStatusMessage extends SimpleDataMessage<Boolean> {
		
	@Override
	public String toString() {
		return "Weapon can shoot : " + getData().getValue();
	}
}
