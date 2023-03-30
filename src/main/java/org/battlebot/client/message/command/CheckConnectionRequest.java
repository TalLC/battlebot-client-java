package org.battlebot.client.message.command;

public class CheckConnectionRequest {
	String rest_id;
	public String getRest_id() {
		return rest_id;
	}

	public void setRest_id(String rest_id) {
		this.rest_id = rest_id;
	}

	public String getMqtt_id() {
		return mqtt_id;
	}

	public void setMqtt_id(String mqtt_id) {
		this.mqtt_id = mqtt_id;
	}

	public String getStomp_id() {
		return stomp_id;
	}

	public void setStomp_id(String stomp_id) {
		this.stomp_id = stomp_id;
	}

	String mqtt_id;
	String stomp_id;
	
	public CheckConnectionRequest(String rest_id, String mqtt_id, String stomp_id) {
		super();
		this.rest_id = rest_id;
		this.mqtt_id = mqtt_id;
		this.stomp_id = stomp_id;
	}

}
