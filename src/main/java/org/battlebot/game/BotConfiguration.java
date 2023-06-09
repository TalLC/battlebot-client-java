package org.battlebot.game;

public class BotConfiguration {
	private int health;
	private float movingSpeed;
	private float turningSpeed;
	private int weaponCooldown;
	
	
	public BotConfiguration(int health, float movingSpeed, float turningSpeed, int weaponCooldown) {
		this.health = health;
		this.movingSpeed = movingSpeed;
		this.turningSpeed = turningSpeed;
		this.weaponCooldown = weaponCooldown;
	}


	public int health() {
		return health;
	}


	public float movingSpeed() {
		return movingSpeed;
	}


	public float turningSpeed() {
		return turningSpeed;
	}


	public int weaponCooldown() {
		return weaponCooldown;
	}
	
	@Override
	public String toString() {
		return "BotConfiguration [health=" + health + ", movingSpeed=" + movingSpeed + ", turningSpeed=" + turningSpeed
				+ ", weaponCooldown=" + weaponCooldown + "]";
	}
}
