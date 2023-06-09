package org.battlebot.connection;

import java.net.SocketTimeoutException;

import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StompMessageListener implements Runnable {
	public final static long RECEIVE_DELAY = 100;
	private static final Logger LOGGER = LoggerFactory.getLogger(StompMessageListener.class);
	
	 
	private StompConnection cnx;
	private boolean stop;
	
	public StompMessageListener(StompConnection cnx) {
		this.cnx = cnx;
	}
	
	public Thread start() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}
	
	public void stop() {
		this.stop = true;
	}
	
	public abstract void onMessage(String message) throws Exception;

	@Override
	public void run() {
		LOGGER.debug("Démarrage écoute message Stomp ", cnx);
		while(!stop) {
			try {
				StompFrame frame = cnx.receive(RECEIVE_DELAY);
				if(frame != null) {
					onMessage(frame.getBody());
				}
			} catch(SocketTimeoutException e) {
				//normal c'est pour pouvoir arreter l'écoute
			}catch (Exception e) {
				LOGGER.debug("Réception message stomp :", e);
				stop = true;
			}
		}
		LOGGER.debug("Arret écoute message Stomp ", cnx);
		
	}
}
