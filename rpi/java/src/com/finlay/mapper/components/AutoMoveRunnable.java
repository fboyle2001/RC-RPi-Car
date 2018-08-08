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
		
		boolean forward = false;
		
		while(AutoMove.getInstance().isActive()) {
			double startTime = System.nanoTime();
			
			double distanceAhead = PiconZero.getInstance().calculateDistanceToObject();
			logger.debug("Distance to object is {} m", distanceAhead);
			
			if(distanceAhead < 0.2) {
				logger.debug("Auto Move turn right @ speed 70");
				forward = false;
				PiconZero.getInstance().stopMotion();
				PiconZero.getInstance().right(70);
				PiconZero.getInstance().setOutput(1, 1);
			} else {
				if(!forward) {
					logger.debug("Auto Move forward @ speed 70");
					forward = true;
					PiconZero.getInstance().stopMotion();
					PiconZero.getInstance().forward(70);
					PiconZero.getInstance().setOutput(1, 0);
				} else {
					logger.debug("Continue forward");
				}
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
		
		logger.info("Stopped Auto Move");
	}

}
