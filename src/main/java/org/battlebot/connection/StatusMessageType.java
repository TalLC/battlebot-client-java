package org.battlebot.connection;

import org.battlebot.client.message.stomp.BotHealthMessage;
import org.battlebot.client.message.stomp.BotMovingSpeedMessage;
import org.battlebot.client.message.stomp.BotMovingStatusMessage;
import org.battlebot.client.message.stomp.BotStunningStatusMessage;
import org.battlebot.client.message.stomp.BotTurningSpeedMessage;
import org.battlebot.client.message.stomp.BotTurningStatusMessage;
import org.battlebot.client.message.stomp.BotWeaponCooldownMessage;
import org.battlebot.client.message.stomp.BotWeaponStatusMessage;
import org.battlebot.client.message.stomp.GameStatusMessage;
import org.battlebot.client.message.stomp.StompConnectionId;

public enum StatusMessageType {
		stomp_id(StompConnectionId.class),
		health_status(BotHealthMessage.class),
		game_status(GameStatusMessage.class),
		stunning_status(BotStunningStatusMessage.class),
		moving_status(BotMovingStatusMessage.class),
		turning_status(BotTurningStatusMessage.class),
		moving_speed_status(BotMovingSpeedMessage.class),
		turning_speed_status(BotTurningSpeedMessage.class),
		weapon_can_shoot(BotWeaponStatusMessage.class),
		weapon_cooldown_ms(BotWeaponCooldownMessage.class);
	
		private Class<?> associatedClass;
		
		private StatusMessageType(Class<?> associatedClass) {
			this.associatedClass = associatedClass;
		}
		
		public Class<?> associatedClass() {
			return associatedClass;
		}
		
}
