package com.finlay.rc.components.mpu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MPURepeatReadingRunnable implements Runnable {
	
	private static final Logger logger;
	
	static {
		logger = LoggerFactory.getLogger(MPURepeatReadingRunnable.class);
	}
	
	private MPUReadingHandler resultHandler;
	
	public MPURepeatReadingRunnable(MPUReadingHandler resultHandler) {
		this.resultHandler = resultHandler;
	}
	
	@Override
	public void run() {
		long delay = 100;
		logger.info("Starting with delay of " + delay + " ms");
		
		while(MPURepeatReader.getInstance().isActive()) {
			AccelerometerReading reading = MPU6050.getInstance().getSensorData();
			logger.debug("Took reading: " + reading.getAsMap().toString());
			resultHandler.process(reading);
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
