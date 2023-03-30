package org.battlebot.connection;

import java.lang.Thread.State;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.stomp.StatusMessage;
import org.battlebot.client.message.stomp.StompConnectionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StompConnectionIdListener extends StompMessageListener implements Future<String>{
	private static final Logger LOGGER = LoggerFactory.getLogger(StompConnectionIdListener.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final BlockingQueue<StompConnectionId> reply = new ArrayBlockingQueue<>(1);
	private volatile State state = State.WAITING;
	
	public StompConnectionIdListener(StompConnection cnx) {
		super(cnx);
		start();
	}

	@Override
	public void onMessage(String message) throws Exception {
		LOGGER.debug("Receive message from status STOMP destination " + message );
		reply.put(MAPPER.readValue(message, StompConnectionId.class));
		state = State.TERMINATED;
		cleanUp();
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
	public String get() throws InterruptedException, ExecutionException {
		return reply.take().getData().getValue();
	}

	@Override
	public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		final StompConnectionId replyOrNull = reply.poll(timeout, unit);
        if (replyOrNull == null) {
            throw new TimeoutException();
        }
        return replyOrNull.getData().getValue();
	}

	private void cleanUp() {
		this.stop();
	}
}
