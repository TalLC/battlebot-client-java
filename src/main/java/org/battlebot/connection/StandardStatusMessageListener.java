package org.battlebot.connection;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.stomp.StatusListener;
import org.battlebot.client.message.stomp.StatusMessage;
import org.battlebot.game.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StandardStatusMessageListener extends StompMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private StatusListener listener;
	StopGameCallback stopCallback;

	public StandardStatusMessageListener(StompConnection cnx, StatusListener listener, StopGameCallback stopGame) {
		super(cnx);
		this.listener = listener;
		stopCallback = stopGame;
	}

	@Override
	public void onMessage(String message) throws Exception {
		JsonNode node = MAPPER.readTree(message);
		String type = node.get("msg_type").asText();
		if (!StatusMessageType.game_status.toString().equals(type)) {
			if (node.get("data").asText().equals("false")) {
				stop();
				stopCallback.onStop();
			} else {
				StatusMessage statusMessage = MAPPER.readValue(message, StatusMessage.class);
				LOGGER.info(statusMessage.toString());
				listener.onStatusMessage(statusMessage);
			}

		}
	}

}
