package org.battlebot.game.laucher2;

import java.util.Optional;

import org.battlebot.client.message.SimpleDataMessage;
import org.battlebot.client.message.mqtt.ScannerDetection;
import org.battlebot.client.message.mqtt.ScannerDetectionListener;
import org.battlebot.client.message.stomp.StatusListener;
import org.battlebot.connection.StartGameCallback;
import org.battlebot.connection.StatusMessageType;
import org.battlebot.game.BotConfiguration;
import org.battlebot.game.GameManager;

public class Bot implements StartGameCallback, StatusListener,ScannerDetectionListener{
	private GameManager mgr;
	private ProcedureHelper procedure;
	private StatusManager actionMgr;
	private BotConfiguration config;
	
	public Bot(GameManager mgr) {
		this.mgr = mgr;
	}
	
	@Override
	public void onScannerMessage(ScannerDetection detection) {
		//analyse de l'environnement
		EnvironmentAnalyser analyser = new EnvironmentAnalyser(detection);
		analyser.analyse();
		System.out.println(analyser);
		System.out.println(actionMgr);
		//priorité 1 detection ennemi
		if(analyser.getFoe().isPresent()) {
			System.out.println("Ennemi dectecte");
			//on tire dessus (au centre de la cilbe)
			procedure.engagementEnnemi(analyser.getFoe().get().getMiddle());
		}
		
		//est ce qu'on avance
		if(actionMgr.movingStatus().value()) {
			obstacleDetection(analyser);
		}else if (!actionMgr.movingStatus().value().equals("stop")){
			//on est en train de tourner
			Optional<ObjectPosition> danger = procedure.detectionObjectProcheSurTrajectoire(analyser.getDangers());
			Optional<ObjectPosition> obstacleProche = procedure.detectionObjectProcheSurTrajectoire(analyser.getObstacles());
			if(danger.isEmpty() && obstacleProche.isEmpty()) {
				//pas de danger ni d'obstacle on s'arrete de tourner et on avance
				procedure.deplacementStandard();
			}else {
				System.out.println("On tourne et on a detecte un objet proche");
			}
		}else if(!obstacleDetection(analyser)){
			//statique pas d'obstacle
			procedure.deplacementStandard();
		}else {
			System.out.println("Quelque chose aurait du être détecté");
		}
	}
	
	public boolean obstacleDetection(EnvironmentAnalyser analyser) {
		boolean res = false;
		
		//detection de danger qui se trouve proche et sur le chemin
		Optional<ObjectPosition> danger = procedure.detectionObjectProcheSurTrajectoire(analyser.getDangers());
		if(danger.isPresent()) {
			System.out.println("Danger imminent dectecte");
			ObjectPosition position = danger.get();
			//on s'arrete et on tourne du coté ou il y a le moins de danger
			procedure.evitementUrgence(position);
			res = true;
		}else {
			//evitement d'obstacle
			Optional<ObjectPosition> obstacle = procedure.detectionObjectProcheSurTrajectoire(analyser.getObstacles());
			if(obstacle.isPresent()) {
				System.out.println("Obstacle proche dectecte");
				ObjectPosition position = obstacle.get();
				//on s'arrete et on tourne du coté ou il y a le moins d'osbtacle
				procedure.evitementUrgence(position);
				res = true;
			}else {
				obstacle = procedure.detectionObjectSurTrajectoire(analyser.getObstacles());
				if(obstacle.isPresent()) {
					System.out.println("Obstacle detecte");
					ObjectPosition position = obstacle.get();
					//on s'arrete et on tourne du coté ou il y a le moins d'osbtacle
					procedure.evitementObstacle(position);
					res = true;
				}
			}
		}
		
		return res;
	}

	@Override
	public void onStatusMessage(Object message, StatusMessageType messageType) {
		SimpleDataMessage simpleMsg = (SimpleDataMessage) message;
		System.out.println(message + " " + messageType);
		actionMgr.confirmAction(messageType, simpleMsg.getData().getValue(), Math.round(simpleMsg.getTimestamp()*1000));
	}

	@Override
	public void onStart(BotConfiguration botConfig){
		//enregistrement de la configuration du bot
		config = botConfig;
		//on commence par avancé
		actionMgr = new StatusManager(mgr, config);
		procedure = new ProcedureHelper(actionMgr);
		procedure.detection();
	}
	
}
