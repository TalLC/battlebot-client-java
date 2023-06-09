package org.battlebot.client.message.stomp;

import org.battlebot.client.message.SimpleDataMessage;

public class BotWeaponCooldownMessage extends SimpleDataMessage<Integer> {
		
	@Override
	public String toString() {
		return "Weapon cooldown delay (ms) : " + getData().getValue();
	}
}
