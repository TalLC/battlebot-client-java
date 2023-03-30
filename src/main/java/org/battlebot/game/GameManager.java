package org.battlebot.game;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.activemq.transport.stomp.StompConnection;
import org.battlebot.client.message.mqtt.ScannerDetectionListener;
import org.battlebot.client.message.mqtt.StandardScannerMessageListener;
import org.battlebot.client.message.stomp.StatusListener;
import org.battlebot.config.Configuration;
import org.battlebot.connection.ConnectionManager;
import org.battlebot.connection.MqttConnexionIdListener;
import org.battlebot.connection.RestCommunication;
import org.battlebot.connection.StandardStatusMessageListener;
import org.battlebot.connection.StartGameCallback;
import org.battlebot.connection.StompConnectionIdListener;
import org.battlebot.connection.StompStartGame;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);
	ConnectionManager cnxMgr;
	RestCommunication rest;
	String botId;
	
	/**
	 * Initialisation du jeux
	 * @param config configuration 
	 */
	public GameManager(Configuration config) {
		cnxMgr = new ConnectionManager(config);
		rest = new RestCommunication(cnxMgr);
	}
	
	/**
	 * Enregistrement du bot dans la partie
	 * @param teamId nom de l'équipe
	 * @param botName nom du bot
	 * @return état de l'enreigistrement
	 * @throws Exception
	 */
	public boolean register(String teamId, String botName) throws Exception {
		LOGGER.info("Start bot registration, team : " + teamId + ", name : " + botName);
		//enrgistrement
		botId = rest.registerBot(teamId, botName);
		LOGGER.info("Receive bot id : " + botId);
		String stompCnxId = null;
		String mqttCnxId = null;
		
		//récupération du session Id
		String regId = rest.getRegistrationId(botId);
		LOGGER.info("Receive registration id : " + regId);
		
		//vérification de la connexion Stomp & Mqtt
		LOGGER.info("Check mqtt & stomp connection");
		StompConnection stompCnx = cnxMgr.getStatusStompConnection(botId);
		MqttConnexionIdListener mqttListener = new  MqttConnexionIdListener();
		MqttClient mqttClient = null;
		boolean res = false;
		try {
			StompConnectionIdListener stompListener = new StompConnectionIdListener(stompCnx);
			mqttClient = cnxMgr.getScannerMqttClient(botId, mqttListener);
			
			//rédupération des id de connexion
			stompCnxId = stompListener.get(5, TimeUnit.SECONDS);
			LOGGER.info("Get stomp validation id : " + stompCnxId);
			mqttCnxId = mqttListener.get(5, TimeUnit.SECONDS);
			LOGGER.info("Get mqtt validation id : " + mqttCnxId);
			
			res = rest.checkConnection(botId, regId, mqttCnxId, stompCnxId);
		}finally {
			stompCnx.close();
		} 
		
		return res;
	}
	
	/**
	 * Attente du démarrage du jeux
	 * @return
	 * @throws Exception
	 */
	public void startGame(StartGameCallback starter, StatusListener statusListener, ScannerDetectionListener scannerListener) throws Exception {
		StompConnection stompCnx = cnxMgr.getStatusStompConnection(botId);
		LOGGER.info("Wait game start ...");
		StompStartGame startGame = new StompStartGame(stompCnx);
		boolean res = startGame.get(60, TimeUnit.SECONDS);
		stompCnx.close();
		LOGGER.info("Game started");
		//En cas de démarrage on souscrit aux messages de status et de scanner
		if(res) {
			MqttClient mqttClient = cnxMgr.getScannerMqttClient(botId, new StandardScannerMessageListener(scannerListener));
			final StompConnection gameStompCnx = cnxMgr.getStatusStompConnection(botId);
			StandardStatusMessageListener stdStatusListener = new StandardStatusMessageListener(gameStompCnx, statusListener, () -> {
				LOGGER.info("Fin de partie");
				try {
					gameStompCnx.close();
				} catch (IOException e1) {
					LOGGER.error("Fermeture de la connection STOMP", e1);
				}
				try {
					cnxMgr.closeScannerMqttClient(mqttClient, botId);
				} catch (MqttException e) {
					LOGGER.error("Fermeture de la connection MQTT", e);
				}
			});
			stdStatusListener.start();
			starter.onStart();
			
		}
	}
	
	/**
	 * Déclenchement d'une action tourner à gauche
	 * @return
	 * @throws Exception
	 */
	public boolean actionTurnLeft() throws Exception {
		return rest.turn(botId, "left");
	}
	
	
	/**
	 * Déclenchement d'une action tourner à droite
	 * @return
	 * @throws Exception
	 */
	public boolean actionTurnRight() throws Exception {
		return rest.turn(botId, "right");
	}
	
	
	/**
	 * Déclenchement d'une action d'arreter de tourner
	 * @return
	 * @throws Exception
	 */
	public boolean actionStopTurn() throws Exception {
		return rest.turn(botId, "stop");
	}
	
	
	/**
	 * Tir selon un angle donné
	 * @param angle angle de tir
	 * @return
	 * @throws Exception
	 */
	public boolean actionFire(int angle) throws Exception {
		return rest.shoot(botId, angle);
	}
	
	/**
	 * Déclenchement d'une action d'avancer
	 * @return
	 * @throws Exception
	 */
	public boolean actionMoveForward() throws Exception {
		return rest.move(botId, "start");
	}
	
	/**
	 * Déclenchement d'une actionde s'arreter
	 * @return
	 * @throws Exception
	 */
	public boolean actionMoveStop() throws Exception {
		return rest.move(botId, "stop");
	}
}
