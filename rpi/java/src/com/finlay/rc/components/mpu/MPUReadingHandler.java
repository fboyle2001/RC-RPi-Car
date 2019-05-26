package com.finlay.rc.components.mpu;

@FunctionalInterface
public interface MPUReadingHandler {

	public void process(AccelerometerReading reading);
	
}
