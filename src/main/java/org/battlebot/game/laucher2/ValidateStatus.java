package org.battlebot.game.laucher2;

public class ValidateStatus<T> extends Status<T>{

	
	public ValidateStatus(T value) {
		super(value);
	}

	public boolean acknowlagement(T value, long time) {
		boolean res;
		if(value.equals(super.value())) {
			res = super.acknowlagement(value, time);
		}else {
			System.out.println("Status not corresponding");
			res = true;
		}
		return res;
	}
}
