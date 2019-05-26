package com.finlay.rc.components.motion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoMove {

	private static final Logger logger;
	
	static {
		logger = LoggerFactory.getLogger(AutoMove.class);
		instance = new AutoMove();
	}
	
	private static AutoMove instance;
	
	public static AutoMove getInstance() {
		return instance;
	}
	
	private boolean active;
	private Thread autoMoveThread;
	
	private AutoMove() {
		this.active = false;
	}
	
	public void start() {
		if(active) {
			logger.info("Auto Move already active");
			return;
		}
		
		this.active = true;
		
		autoMoveThread = new Thread(new AutoMoveRunnable(), "RPi-AutoMove");
		autoMoveThread.start();
	}
	
	public void stop() {
		if(!active) {
			logger.info("Auto Move is not active");
			return;
		}
		
		logger.info("Stopping Auto Move");
		this.active = false;
	}
	
	public boolean isActive() {
		return active;
	}
	
}
