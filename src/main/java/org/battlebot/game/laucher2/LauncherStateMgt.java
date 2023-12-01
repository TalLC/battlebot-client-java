package org.battlebot.game.laucher2;

import org.battlebot.config.Configuration;
import org.battlebot.game.BotConfiguration;
import org.battlebot.game.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LauncherStateMgt {
	private static final Logger LOGGER = LoggerFactory.getLogger(LauncherStateMgt.class);
	public static void main(String[] args) throws Exception {
		LOGGER.info("D�marrage du bot");
		String serverHost = "localhost";
		Configuration cfg = new Configuration("http://"  + serverHost, 8000,
				serverHost, 1883, "user","password",
				serverHost, 61613, "user","password");
		final GameManager gameMgr = new GameManager(cfg);
		if(gameMgr.register("team-id-1", "Warlock")) {
			final Bot bot = new Bot(gameMgr);
			gameMgr.startGame(bot,bot,bot);
		}
	}

}
