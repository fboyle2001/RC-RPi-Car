package com.finlay.mapper.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoMoveRunnable implements Runnable {
	
	private static final Logger logger;
	
	static {
		logger = LoggerFactory.getLogger(AutoMoveRunnable.class);
	}
	
	private boolean active;
	
	public AutoMoveRunnable() {
		this.active = true;
	}

	@Override
	public void run() {
		logger.info("Automove started");
	
		int lastSpeed = -1;
		
		while(active) {
			double startTime = System.nanoTime();
			
			int speed = calculateSpeed();

			if(speed != -1) {
				if(lastSpeed != speed) {
					lastSpeed = speed;
					logger.info("Travelling forward at speed {}", speed);
					PiconZero.getInstance().forward(speed);
				}
			} else {
				logger.info("Calculating turn");
				// TODO: need to make a working version for this to be done
			}
			
			double endTime = System.nanoTime();
			double delta = endTime - startTime;
			logger.debug("Iteration took {} ns", delta);
		}
		
		logger.info("Stopped automove");
	}
	
	private int calculateSpeed() {
		double distanceAhead = PiconZero.getInstance().calculateDistanceToObject();
		
		logger.info("Forward distance is {} m", distanceAhead);
		
		int speed = -1;
		
		if(distanceAhead > 1) {
			speed = 50;
		} else if (distanceAhead > 0.2) {
			speed = 40;
		} else if (distanceAhead > 0.1) {
			speed = 20;
		}
		
		return speed;
	}
	
	public void stop() {
		this.active = false;
	}

}
