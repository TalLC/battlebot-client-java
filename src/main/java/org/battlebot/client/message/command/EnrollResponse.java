package org.battlebot.client.message.command;

public class EnrollResponse {
	public final static String OK = "ok";
	private String status;
	private String message;
	private String bot_id;
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
	public String getBot_id() {
		return bot_id;
	}
	public void setBot_id(String bot_id) {
		this.bot_id = bot_id;
	}
	
}
