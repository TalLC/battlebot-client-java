package org.battlebot.connection;

import org.battlebot.game.BotConfiguration;

public interface StartGameCallback {

	void onStart(BotConfiguration botConfig);
}
