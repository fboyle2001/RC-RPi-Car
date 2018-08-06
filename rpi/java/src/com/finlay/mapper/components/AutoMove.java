package com.finlay.mapper.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoMove {

	private static final Logger logger;
	
	static {
		logger = LoggerFactory.getLogger(AutoMove.class);
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
			logger.info("Automove already active");
			return;
		}
		
		autoMoveThread = new Thread(new AutoMoveRunnable(), "RPi-AutoMove");
		autoMoveThread.start();
	}
	
	public void stop() {
		if(!active) {
			logger.info("Automove is not active");
			return;
		}
		
		logger.info("Stopping automove");
		this.active = false;
	}
	
	public boolean isActive() {
		return active;
	}
	
}
