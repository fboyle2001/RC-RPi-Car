package com.finlay.rc.components.mpu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MPURepeatReader {

	private static final Logger logger;
	private static MPURepeatReader instance;
	
	static {
		logger = LoggerFactory.getLogger(MPURepeatReader.class);
		instance = new MPURepeatReader();
	}
	
	public static MPURepeatReader getInstance() {
		return instance;
	}
	
	private boolean active;
	private Thread repeatThread;
	
	private MPURepeatReader() {
		this.active = false;
		this.repeatThread = null;
	}
	
	public boolean start(MPUReadingHandler resultHandler) {
		if(active) {
			logger.warn("Unable to start as it is already active");
			return false;
		}
		
		this.active = true;
		this.repeatThread = new Thread(new MPURepeatReadingRunnable(resultHandler));
		repeatThread.start();
		
		return true;
	}
	
	public void stop() {
		if(!active) {
			logger.debug("Repeat readings not active so cannot stop");
			return;
		}
		
		this.active = false;
	}
	
	public boolean isActive() {
		return active;
	}
	
}
