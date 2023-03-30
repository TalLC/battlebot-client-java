package org.battlebot.client.message.command;

public class CheckConnectionResponse {
	public final static String OK = "ok";
	private String status;
	private String message;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
