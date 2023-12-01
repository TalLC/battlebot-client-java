package org.battlebot.game.laucher2;

import org.battlebot.client.message.mqtt.ScannerDetection.Data;

public class ObjectPosition {

	private float distance;
	private float from;
	private float to;
	private float middle; 
	private String type;
	private String name;
	
	public ObjectPosition(Data data) {
		this.distance = data.getDistance();
		this.from = data.getFrom();
		this.to = data.getTo();
		this.middle = (this.to + this.from)/2;
		this.type = data.getObject_type();
		this.name = data.getName();
	}
	
	public String getName() {
		return name;
	}
	public float getDistance() {
		return distance;
	}
	public float getFrom() {
		return from;
	}
	public float getTo() {
		return to;
	}
	public float getMiddle() {
		return middle;
	}
	public String getType() {
		return type;
	}
	@Override
	public String toString() {
		return type + "[distance=" + distance + ", from=" + from + ", to=" + to +"]";
	}

	
	
}
