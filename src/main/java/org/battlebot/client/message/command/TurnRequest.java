package org.battlebot.client.message.command;

public class TurnRequest {
	private String direction;

	public TurnRequest(String direction) {
		super();
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	};
}
