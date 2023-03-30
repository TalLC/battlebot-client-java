package org.battlebot.client.message.mqtt;

@FunctionalInterface
public interface ScannerDetectionListener {
	
	void onScannerMessage(ScannerDetection detection);
}
