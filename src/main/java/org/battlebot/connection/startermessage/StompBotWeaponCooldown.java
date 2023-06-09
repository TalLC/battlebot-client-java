package org.battlebot.connection.startermessage;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.stomp.BotWeaponCooldownMessage;
import org.battlebot.connection.StatusMessageType;

public class StompBotWeaponCooldown extends AbstractFutureStompMessage<BotWeaponCooldownMessage, Integer>{
	
	public StompBotWeaponCooldown(StompConnection cnx) {
		super(cnx, StatusMessageType.weapon_cooldown_ms.name() , BotWeaponCooldownMessage.class);
	}
}
