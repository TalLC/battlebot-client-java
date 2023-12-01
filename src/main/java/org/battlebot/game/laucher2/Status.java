package org.battlebot.game.laucher2;

public class Status<T> {

	private T value;
	private long timeSending;
	private boolean pending;
	private long timeActive;
	private long timeReceive;
	
	public Status(T value){
		this.timeSending = System.currentTimeMillis();
		this.pending = true;
		this.value = value;
	}
	
	public boolean acknowlagement(T value, long time) {
		this.value = value;
		this.timeActive = time;
		this.timeReceive = System.currentTimeMillis();
		this.pending = false;
		return true;
	}
	
	public boolean isPending() { return this.pending;}
	public long deliveryTime() { return this.timeActive - this.timeSending;}
	public long receiveTime() { return this.timeReceive - this.timeActive;}
	public long activeTime( ) { return System.currentTimeMillis() - this.timeReceive;}
	public T value() {return this.value;}
}
