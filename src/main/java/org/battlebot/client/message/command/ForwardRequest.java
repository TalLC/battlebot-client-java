package org.battlebot.client.message.command;

public class ForwardRequest {
	private String action;

	public ForwardRequest(String action) {
		super();
		this.action = action;
	}

	public String getAction() {
		return action;
	};
}
