package org.battlebot.connection;

import java.lang.Thread.State;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.battlebot.client.message.mqtt.MqttConnectionId;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MqttConnexionIdListener implements IMqttMessageListener, Future<String>{
	private static final Logger LOGGER = LoggerFactory.getLogger(MqttConnexionIdListener.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final BlockingQueue<MqttConnectionId> reply = new ArrayBlockingQueue<>(1);
	private volatile State state = State.WAITING;
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		byte[] payload = message.getPayload();
		String msg = new String(payload);
		LOGGER.debug("Receive message from scanner MQTT destination " + msg );
		reply.put(MAPPER.readValue(msg, MqttConnectionId.class));
		state = State.TERMINATED;
	}
	

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		state = State.BLOCKED;
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
		final MqttConnectionId replyOrNull = reply.poll(timeout, unit);
        if (replyOrNull == null) {
            throw new TimeoutException();
        }
        return replyOrNull.getData().getValue();
	}


}
