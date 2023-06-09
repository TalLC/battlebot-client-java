package org.battlebot.connection.startermessage;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.stomp.BotTurningSpeedMessage;
import org.battlebot.connection.StatusMessageType;

public class StompBotMovingSpeed extends AbstractFutureStompMessage<BotTurningSpeedMessage, Float>{
	
	public StompBotMovingSpeed(StompConnection cnx) {
		super(cnx, StatusMessageType.moving_speed_status.name() , BotTurningSpeedMessage.class);
	}
}
