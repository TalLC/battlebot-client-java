package org.battlebot.game.laucher2;

import java.util.LinkedList;

import org.battlebot.connection.StatusMessageType;
import org.battlebot.game.BotConfiguration;
import org.battlebot.game.GameManager;

public class StatusManager {
	public final static int STATUS_CACHE = 5;

	private BotConfiguration config;
	private LinkedList<Status> lastStatus;
	private GameManager gm;
	private MovingStatus movingStatus;
	private TurningStatus turningStatus;
	private FireStatus fireStatus;
	private boolean isStunned;

	public StatusManager(GameManager gm, BotConfiguration config) {
		this.gm = gm;
		this.config = config;
		this.lastStatus = new LinkedList<>();
		movingStatus = new MovingStatus(false);
		turningStatus = new TurningStatus("stop");
		isStunned = false;
	}

	public void moveForward() {
		if (!isStunned) {
			movingStatus = new MovingStatus(true);
			try {
				this.gm.actionMoveForward();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void moveStop() {
		if (!isStunned) {
			movingStatus = new MovingStatus(false);
			try {
				this.gm.actionMoveStop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void turnLeft() {
		if (!isStunned) {
			turningStatus = new TurningStatus("left");
			try {
				this.gm.actionTurnLeft();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void turnRight() {
		if (!isStunned) {
			turningStatus = new TurningStatus("right");
			try {
				this.gm.actionTurnRight();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void turnStop() {
		if (!isStunned) {
			turningStatus = new TurningStatus("stop");
			try {
				this.gm.actionStopTurn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void fire(float angle) {
		if (!isStunned && (fireStatus == null || fireStatus.value())) {
			fireStatus = new FireStatus(false);
			try {
				this.gm.actionFire(angle);
			} catch (Exception e) {
				System.out.println("Impossible de tirer");
			}
		}
	}

	private void storeStatus(Status status) {
		if (this.lastStatus.size() == STATUS_CACHE) {
			lastStatus.poll();
		}
		lastStatus.add(status);
	}

	public long averageDeliveryDelay() {
		long res = 0;
		int size = this.lastStatus.size();
		if (size > 0) {
			long sum = lastStatus.stream().map(status -> status.deliveryTime()).reduce(0L, Long::sum);
			res = sum / size;
		}
		return res;
	}

	public long averageSendingDelay() {
		long res = 0;
		int size = this.lastStatus.size();
		if (size > 0) {
			long sum = lastStatus.stream().map(status -> status.receiveTime()).reduce(0L, Long::sum);
			res = sum / size;
		}
		return res;
	}

	public void confirmAction(StatusMessageType type, Object value, long time) {
		switch (type) {
		case moving_status:
			if (movingStatus.acknowlagement((Boolean) value, time)) {
				storeStatus(movingStatus);
			} else {
				// on peut être stoned
				movingStatus = new MovingStatus((Boolean) value);
				movingStatus.acknowlagement((Boolean) value, time);
			}

			break;
		case turning_speed_status:
			if (turningStatus.acknowlagement((String) value, time)) {
				storeStatus(turningStatus);
			} else {
				// on peut être stoned
				turningStatus = new TurningStatus((String) value);
				turningStatus.acknowlagement((String) value, time);
			}

			break;
		case weapon_can_shoot:
			fireStatus.acknowlagement((Boolean) value, time);
			break;
		case stunning_status:
			// on passe à l'arret
			if ((Boolean) value) {
				stunned();
			} else {
				unStunned();
			}
		default:
			;
		}
	}

	private void stunned() {
		System.out.println("Gel");
		long time = System.currentTimeMillis();
		isStunned = true;
		movingStatus = new MovingStatus(false);
		movingStatus.acknowlagement(false, time);
		turningStatus = new TurningStatus("stop");
		turningStatus.acknowlagement("stop", time);
	}

	private void unStunned() {
		isStunned = false;
		System.out.println("Dégel");
	}

	public MovingStatus movingStatus() {
		return movingStatus;
	}

	public TurningStatus turningStatus() {
		return turningStatus;
	}

	public FireStatus fireStatus() {
		return fireStatus;
	}

	@Override
	public String toString() {
		return "StatusManager [movingStatus=" + movingStatus.value() + ", turningStatus=" + turningStatus.value() + ", fireStatus="
				+ (fireStatus == null ? true : fireStatus.value()) + ", isStunned=" + isStunned + "]";
	}
}
