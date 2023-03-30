package org.battlebot.game;

import org.battlebot.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);
	public static void main(String[] args) throws Exception {
		LOGGER.info("Démarrage du bot");
		Configuration cfg = new Configuration();
		final GameManager gameMgr = new GameManager(cfg);
		
		if(gameMgr.register("team-id-1", "Warlock")) {
			gameMgr.startGame(
			()-> {
				
				try {
					gameMgr.actionTurnRight();
					Thread.sleep(1000000);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					for(int i=1; i<1000; i++) {
						gameMgr.actionMoveForward();
						Thread.sleep(1000);
						gameMgr.actionMoveStop();
						Thread.sleep(1000);
						gameMgr.actionTurnRight();
						Thread.sleep(1000);
						gameMgr.actionMoveForward();
						Thread.sleep(1000);
						gameMgr.actionFire(0);
						Thread.sleep(1000);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			},
			(status) -> {
				
			}, 
			(detection) -> {
				LOGGER.info("Detect : " + detection.toString());
			});
		}
	}

}
