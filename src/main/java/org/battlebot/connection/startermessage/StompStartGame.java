package org.battlebot.connection.startermessage;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.stomp.GameStatusMessage;
import org.battlebot.connection.StatusMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StompStartGame extends AbstractFutureStompMessage<GameStatusMessage, Boolean>{
	private static final Logger LOGGER = LoggerFactory.getLogger(StompStartGame.class);
	
	public StompStartGame(StompConnection cnx) {
		super(cnx, StatusMessageType.game_status.name() , GameStatusMessage.class);
	}

	protected void proceedMessage(GameStatusMessage message) throws InterruptedException {
		if(message.getData().getValue()){
			super.proceedMessage(message);
		}else {
			LOGGER.debug("Game not yet started");
		}
	}
}
