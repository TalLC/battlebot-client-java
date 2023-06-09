package org.battlebot.connection.startermessage;

import java.lang.Thread.State;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.SimpleDataMessage;
import org.battlebot.connection.StompMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractFutureStompMessage<K extends SimpleDataMessage<T>, T> extends StompMessageListener implements Future<T>{
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFutureStompMessage.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final BlockingQueue<K> reply = new ArrayBlockingQueue<>(1);
	private String messageType;
	private Class<K> messageClass;
	private volatile State state = State.WAITING;
	
	public AbstractFutureStompMessage(StompConnection cnx, String messageType, Class<K> messageClass) {
		super(cnx);
		this.messageType = messageType;
		this.messageClass = messageClass;
		start();
	}

	@Override
	public void onMessage(String message) throws Exception {
		LOGGER.debug("Receive message from status STOMP destination " + message );
		JsonNode node = MAPPER.readTree(message);
		String type = node.get("msg_type").asText();
		if(messageType.equals(type)) {
			K msg = MAPPER.readValue(message, messageClass);
			proceedMessage(msg);
		}else {
			LOGGER.warn("Message type " + type +" unexpected while waiting " + messageType);
		}
	}
	
	protected void proceedMessage(K message) throws InterruptedException {
		reply.put(message);
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
	public T get() throws InterruptedException, ExecutionException {
		return reply.take().getData().getValue();
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		final K replyOrNull = reply.poll(timeout, unit);
        if (replyOrNull == null) {
            throw new TimeoutException();
        }
        return replyOrNull.getData().getValue();
	}

	private void cleanUp() {
		this.stop();
	}
}
