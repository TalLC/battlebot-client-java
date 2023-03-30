package org.battlebot.config;

public class Configuration {

	private String restServer;
	private int restPort;
	private String mqttServer;
	private int mqttPort;
	private String mqttUsername;
	private String mqttPassword;
	private String stompServer;
	private int stompPort;
	private String stompUsername;
	private String stompPassword;
	
	public Configuration() {
		this("http://localhost", 8000,
			 "localhost", 1883, "user","password",
			 "localhost", 61613, "user","password");
	}
	
	public Configuration(String serverHost) {
		this("http://"  + serverHost, 8080,
			serverHost, 1883, "user","password",
			serverHost, 61613, "user","password");
	}
	
	public Configuration(String commandRestServerHost,
						 int commandRestServerPort,
						 String scannerMqttServerHost,
						 int scannerMqttServerPort,
						 String scannerMqttUsername,
						 String scannerMqttPassword,
						 String statusStompServerHost,
						 int statusStompServerPort,
						 String statusStompUsername,
						 String statusStompPassword) {
		restServer = commandRestServerHost;
		restPort = commandRestServerPort;
		mqttServer = scannerMqttServerHost;
		mqttPort = scannerMqttServerPort;
		mqttUsername = scannerMqttUsername;
		mqttPassword = scannerMqttPassword;
		stompServer = statusStompServerHost;
		stompPort =  statusStompServerPort;
		stompUsername =  statusStompUsername;
		stompPassword =  statusStompPassword;
	}

	public String getRestServer() {
		return restServer;
	}

	public int getRestPort() {
		return restPort;
	}

	public String getMqttServer() {
		return mqttServer;
	}

	public int getMqttPort() {
		return mqttPort;
	}

	public String getMqttUsername() {
		return mqttUsername;
	}

	public String getMqttPassword() {
		return mqttPassword;
	}

	public String getStompServer() {
		return stompServer;
	}

	public int getStompPort() {
		return stompPort;
	}

	public String getStompUsername() {
		return stompUsername;
	}

	public String getStompPassword() {
		return stompPassword;
	}
}
