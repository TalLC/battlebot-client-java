package org.battlebot.client.message.command;

public class FireRequest {
	private float angle;

	public FireRequest(float ang) {
		super();
		this.angle = ang;
	}

	public float getAngle() {
		return angle;
	};
}
