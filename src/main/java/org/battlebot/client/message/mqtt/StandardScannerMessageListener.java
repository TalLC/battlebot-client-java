package org.battlebot.client.message.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StandardScannerMessageListener implements IMqttMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(StandardScannerMessageListener.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private ScannerDetectionListener listener;

	public StandardScannerMessageListener(ScannerDetectionListener listener) {
		this.listener = listener;
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		byte[] payload = message.getPayload();
		String msg = new String(payload);
		LOGGER.debug("Receive message from scanner MQTT destination " + msg );
		JsonNode node = MAPPER.readTree(msg);
		if(node.get("msg_type").asText().equals("object_detection")) {
			try {
			ScannerDetection detection = MAPPER.readValue(msg, ScannerDetection.class);
			listener.onScannerMessage(detection);
			}catch(Exception e) {
				LOGGER.error("Lecture message MQTT", e);
			}
		}
	}

}
