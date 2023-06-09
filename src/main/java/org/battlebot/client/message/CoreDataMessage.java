package org.battlebot.client.message;

public abstract class CoreDataMessage{
	private float timestamp;
	private String msg_type;
	private String source;

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public float getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(float timestamp) {
		this.timestamp = timestamp;
	}

}
