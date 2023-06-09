package org.battlebot.connection.startermessage;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.stomp.BotMovingSpeedMessage;
import org.battlebot.connection.StatusMessageType;

public class StompBotTurningSpeed extends AbstractFutureStompMessage<BotMovingSpeedMessage, Float>{
	
	public StompBotTurningSpeed(StompConnection cnx) {
		super(cnx, StatusMessageType.turning_speed_status.name() , BotMovingSpeedMessage.class);
	}
}
