package org.battlebot.connection.startermessage;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.stomp.BotHealthMessage;
import org.battlebot.connection.StatusMessageType;

public class StompBotHealth extends AbstractFutureStompMessage<BotHealthMessage, Integer>{
	
	public StompBotHealth(StompConnection cnx) {
		super(cnx, StatusMessageType.health_status.name() , BotHealthMessage.class);
	}
}
