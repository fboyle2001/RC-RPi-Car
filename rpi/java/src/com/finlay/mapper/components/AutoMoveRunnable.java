package com.finlay.mapper.components;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoMoveRunnable implements Runnable {
	
	private static final Logger logger;
	
	static {
		logger = LoggerFactory.getLogger(AutoMoveRunnable.class);
	}

	@Override
	public void run() {
		logger.info("Automove started");
	
		int lastSpeed = -1;
		
		while(AutoMove.getInstance().isActive()) {
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
			boolean pause = delta < 166666667;
			
			logger.debug("Iteration took {} ns, pausing {}", delta, pause);
			
			if(pause) {
				double pauseTime = 166666667 - delta;
				long pauseTimeMs = TimeUnit.MILLISECONDS.convert((long) pauseTime, TimeUnit.NANOSECONDS);
				
				try {
					Thread.sleep(pauseTimeMs);
				} catch (InterruptedException e) {
					logger.warn("Pause failed");
				}
			}
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

}
