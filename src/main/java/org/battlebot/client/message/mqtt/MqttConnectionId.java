package org.battlebot.client.message.mqtt;

import org.battlebot.client.message.SimpleDataMessage;

public class MqttConnectionId extends SimpleDataMessage<String>{
	@Override
	public String toString() {
		return "Mqtt Connection ID : " + getData().getValue();
	}
}
