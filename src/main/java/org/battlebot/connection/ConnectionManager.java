package org.battlebot.connection;

import org.apache.activemq.transport.stomp.Stomp.Headers.Subscribe;
import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.battlebot.config.Configuration;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
	public final static String STOMP_ROOT_DESTINATION = "BATTLEBOT.BOT.";
	public final static String MQTT_ROOT_DESTINATION = "BATTLEBOT/BOT/";
	private Configuration config;
	private MqttClient mqttClient;
	public ConnectionManager(Configuration cfg) {
		config = cfg;
	}
	
	
	public  HttpPost httpPost(String url){
		return new HttpPost(config.getRestServer()+":" + config.getRestPort() + url);
	}
	
	public  HttpGet httpGet(String url){
		return new HttpGet(config.getRestServer()+":" + config.getRestPort() + url);
	}
	
	
	public  HttpPatch httpPatch(String url){
		return new HttpPatch(config.getRestServer()+":" + config.getRestPort() + url);
	}
	
	public StompConnection getStatusStompConnection(String botId) throws Exception {
		StompConnection stompConnection = new StompConnection();
		stompConnection.open(config.getStompServer(), config.getStompPort());
		stompConnection.connect(config.getStompUsername(), config.getStompPassword());
		stompConnection.subscribe(STOMP_ROOT_DESTINATION + botId, Subscribe.AckModeValues.AUTO);
		
		LOGGER.info("Start listening stomp queue " +  STOMP_ROOT_DESTINATION + botId);
		return stompConnection;
	}

	
	public MqttClient getScannerMqttClient(String botId, IMqttMessageListener callback) throws Exception {
		if(mqttClient == null) {
			mqttClient = new MqttClient("tcp://" + config.getMqttServer() + ":" + config.getMqttPort(),botId, new MemoryPersistence());
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(config.getMqttUsername());
			connOpts.setPassword(config.getMqttPassword().toCharArray());
			mqttClient.connect(connOpts);
			 mqttClient.subscribe(MQTT_ROOT_DESTINATION + botId, callback);
			 mqttClient.setManualAcks(false);
			 LOGGER.info("Start listening mqtt queue " +  MQTT_ROOT_DESTINATION + botId);
		 }else {
			 mqttClient.unsubscribe(MQTT_ROOT_DESTINATION + botId);
			 mqttClient.subscribe(MQTT_ROOT_DESTINATION + botId, callback);
		 }
		
		 return mqttClient;
	}
	
	public void closeScannerMqttClient(MqttClient client, String botId) throws MqttException {
		 LOGGER.info("End listening mqtt queue " +  MQTT_ROOT_DESTINATION + botId);
		client.unsubscribe(MQTT_ROOT_DESTINATION + botId);
		client.disconnect(1000);
		client.close(true);
	}
	
	
}
