package org.battlebot.game.laucher2;

import java.util.Optional;
import java.util.Set;

public class ProcedureHelper {
	private final static float ANGLE_COLLISION = 20.0f;
	private final static float DISTANCE_COLLISION = 2.2f;
	private final static float ANGLE_DETECTION = 10.0f;
	private final static float DISTANCE_DETECTION = 3.5f;
	
	private StatusManager mgr;
	
	public ProcedureHelper(StatusManager mgr) {
		this.mgr = mgr;
	}
	
	public Optional<ObjectPosition> detectionObjectProcheSurTrajectoire(Set<ObjectPosition> objects){
		return objects.stream().filter(position -> 
			position.getDistance()<= DISTANCE_COLLISION
			&& (Math.abs(position.getFrom()) < ANGLE_COLLISION 
				|| Math.abs(position.getTo()) < ANGLE_COLLISION)
			)
			.findAny();
	}
	
	public Optional<ObjectPosition> detectionObjectSurTrajectoire(Set<ObjectPosition> objects){
		return objects.stream().filter(position -> 
			position.getDistance()<= DISTANCE_DETECTION
			&& (Math.abs(position.getFrom()) < ANGLE_DETECTION 
					|| Math.abs(position.getTo()) < ANGLE_DETECTION)
				)
			.findAny();
	}
	
	public void evitementUrgence(ObjectPosition position) {
		System.out.println("Evitement d'urgence");
		mgr.moveStop();
		if(position.getFrom()<0 && position.getTo()<0) { mgr.turnLeft();}
		else if(position.getFrom()>0 && position.getTo()>0) { mgr.turnRight();}
		else if(Math.abs(position.getFrom()) > Math.abs(position.getTo())){mgr.turnLeft();}
		else { mgr.turnRight();}		
	}
	
	public void evitementObstacle(ObjectPosition position) {
		System.out.println("Evitement obstacle");
		if(position.getFrom()<0 && position.getTo()<0) { mgr.turnLeft();}
		else if(position.getFrom()>0 && position.getTo()>0) { mgr.turnRight();}
		else if(Math.abs(position.getFrom()) > Math.abs(position.getTo())){mgr.turnLeft();}
		else { mgr.turnRight();}		
	}
	
	public void deplacementStandard() {
		System.out.println("Déplacement standard");
		if(!mgr.movingStatus().value()) {
			mgr.moveForward();
		}
		if(!mgr.turningStatus().value().equals("stop")) {
			mgr.turnStop();
		}
	}
	
	public void engagementEnnemi(float angle) {
		System.out.println("Engagement");
		mgr.fire(angle);
		if(Math.abs(angle)<10) {
			mgr.turnStop();
			mgr.moveForward();
		}else if(angle < 0) {
			mgr.turnRight();
			mgr.moveStop();
		}else{
			mgr.turnLeft();
			mgr.moveStop();
		}
		
	}
	
	public void detection() {
		System.out.println("Deplacement detection");
		mgr.turnRight();
	}
}
