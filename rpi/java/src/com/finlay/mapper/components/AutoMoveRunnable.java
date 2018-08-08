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
		int loopsSinceTurn = 0;
		boolean turning = false;
		
		while(AutoMove.getInstance().isActive()) {
			double startTime = System.nanoTime();
			
			if(loopsSinceTurn >= 20) {
				logger.info("Unable to find a suitable place to continue moving towards");
				PiconZero.getInstance().stopMotion();
				break;
			}
			
			int speed = calculateSpeed();
			
			if(speed != -1) {
				if(lastSpeed != speed) {
					lastSpeed = speed;
					logger.info("Travelling forward at speed {}", speed);
					PiconZero.getInstance().forward(speed);
					turning = false;
				}
			} else {
				if(turning) {
					loopsSinceTurn++;
					continue;
				}
				
				logger.info("Turning");
				PiconZero.getInstance().right(70);
				loopsSinceTurn = 0;
				turning = true;
				lastSpeed = -1;
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
		
		if(distanceAhead > 0.15) {
			speed = 50;
		}
		
		return speed;
	}

}
