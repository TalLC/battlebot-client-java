package org.battlebot.client.message.stomp;

public class GameStatusMessage {
	private String msg_type;
	private String source;
	private String data;

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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Status [" + msg_type +"] : "+ data;
	}
}
