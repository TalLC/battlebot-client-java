package org.battlebot.connection.startermessage;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.stomp.StompConnectionId;
import org.battlebot.connection.StatusMessageType;

public class StompConnectionIdListener extends AbstractFutureStompMessage<StompConnectionId, String>{
	public StompConnectionIdListener(StompConnection cnx) {
		super(cnx, StatusMessageType.stomp_id.name() , StompConnectionId.class);
	}
}
