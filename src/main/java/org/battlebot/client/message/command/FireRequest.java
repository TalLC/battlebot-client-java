package org.battlebot.client.message.command;

public class FireRequest {
	private int angle;

	public FireRequest(int ang) {
		super();
		this.angle = ang;
	}

	public int getAngle() {
		return angle;
	};
}
