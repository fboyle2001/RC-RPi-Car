package com.finlay.mapper.components;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.Robot;

import lib.finlay.core.io.ConfigurationFile;

public class AutoMoveRunnable implements Runnable {
	
	private static final Logger logger;
	private static final double minTurnDistance;
	
	static {
		logger = LoggerFactory.getLogger(AutoMoveRunnable.class);
		minTurnDistance = ConfigurationFile.getConfigurationOf(Robot.class).getDouble("auto.minTurnDistance", 0.4);
	}

	@Override
	public void run() {
		logger.info("Automove started");
		
		boolean forward = false;
		
		while(AutoMove.getInstance().isActive()) {
			double startTime = System.nanoTime();
			
			double distanceAhead = PiconZero.getInstance().calculateDistanceToObject();
			logger.debug("Distance to object is {} m", distanceAhead);
			
			if(distanceAhead < minTurnDistance) {
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
			// 20 Hz
			boolean pause = delta < 50000000;
			
			logger.debug("Iteration took {} ns, pausing {}", delta, pause);
			
			if(pause) {
				double pauseTime = 50000000 - delta;
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
