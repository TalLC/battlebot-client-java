package org.battlebot.game.laucher2;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import org.battlebot.client.message.mqtt.ScannerDetection;

public class EnvironmentAnalyser {
	public final static int POOL_SIZE = 4;
	private ForkJoinPool threadPool;
	private ScannerDetection detection;
	private float timestamp;
	private Set<ObjectPosition> obstacles;
	private Optional<ObjectPosition> foe;
	private Set<ObjectPosition> dangers;
	
	public EnvironmentAnalyser(ScannerDetection detection) {
		this.threadPool = new ForkJoinPool(POOL_SIZE);
		this.detection = detection;
		this.obstacles = new HashSet<ObjectPosition>();
		this.dangers = new HashSet<ObjectPosition>();
		this.foe = Optional.empty();;
	}
	
	public void analyse() {
		timestamp = detection.getTimestamp();
		
		detection.getData().stream().map(ObjectPosition::new).forEach((position) -> {
			switch(position.getType()) {
				case "tile" : 
					this.dangers.add(position);
					break;
				case "rock" :
				case "tree" :
					this.obstacles.add(position);
					break;
				case "bot" : 
					this.foe = Optional.of(position);
					break;
				default : 
					
			}
		});
		
	}

	public float getTimestamp() {
		return timestamp;
	}
	
	public Set<ObjectPosition> getObstacles() {
		return obstacles;
	}

	public Optional<ObjectPosition> getFoe() {
		return foe;
	}

	public Set<ObjectPosition> getDangers() {
		return dangers;
	}

	@Override
	public String toString() {
		return "EnvironmentAnalyser foe=" + foe.isPresent() + ", obstacles=" + obstacles.size() + ", dangers=" + dangers.size();
	}
	
	
}
