package org.battlebot.game;

import org.battlebot.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);
	public static void main(String[] args) throws Exception {
		LOGGER.info("Démarrage du bot");
		String serverHost = "localhost";
		Configuration cfg = new Configuration("http://"  + serverHost, 8000,
				serverHost, 1883, "user","password",
				serverHost, 61613, "user","password");
		final GameManager gameMgr = new GameManager(cfg);
		
		if(gameMgr.register("team-id-1", "Warlock")) {
			gameMgr.startGame(
			(BotConfiguration botConfig)-> {
				try {
					gameMgr.actionTurnRight();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			},
			(message,type) -> {
				LOGGER.info("Message[" + type.name() + "] : " + message.toString());
			}, 
			(detection) -> {
				LOGGER.info("Detect : " + detection.toString());
				
			});
		}
	}

}
