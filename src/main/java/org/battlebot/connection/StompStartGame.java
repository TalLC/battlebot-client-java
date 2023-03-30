package org.battlebot.connection;

import java.lang.Thread.State;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.stomp.GameStatusMessage;
import org.battlebot.client.message.stomp.StatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StompStartGame extends StompMessageListener implements Future<Boolean>{
	private static final Logger LOGGER = LoggerFactory.getLogger(StompStartGame.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final BlockingQueue<GameStatusMessage> reply = new ArrayBlockingQueue<>(1);
	private volatile State state = State.WAITING;
	
	public StompStartGame(StompConnection cnx) {
		super(cnx);
		start();
	}

	@Override
	public void onMessage(String message) throws Exception {
		LOGGER.debug("Receive message from status STOMP destination " + message );
		JsonNode node = MAPPER.readTree(message);
		String type = node.get("msg_type").asText();
		if(StatusMessageType.game_status.name().equals(type)) {
			GameStatusMessage statMsg = MAPPER.readValue(message, GameStatusMessage.class);
			if(Boolean.valueOf(statMsg.getData())){
				reply.put(statMsg);
				state = State.TERMINATED;
				cleanUp();
			}else {
				LOGGER.debug("Game not yet started");
			}
		}else {
			LOGGER.warn("Message type " + type +" unexpected while waiting game start");
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		state = State.BLOCKED;
		cleanUp();
		return true;
	}

	@Override
	public boolean isCancelled() {
		return state == State.BLOCKED;
	}

	@Override
	public boolean isDone() {
		return state == State.TERMINATED;
	}

	@Override
	public Boolean get() throws InterruptedException, ExecutionException {
		return Boolean.valueOf(reply.take().getData());
	}

	@Override
	public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		final GameStatusMessage replyOrNull = reply.poll(timeout, unit);
        if (replyOrNull == null) {
            throw new TimeoutException();
        }
        return Boolean.valueOf(replyOrNull.getData());
	}

	private void cleanUp() {
		this.stop();
	}
}
