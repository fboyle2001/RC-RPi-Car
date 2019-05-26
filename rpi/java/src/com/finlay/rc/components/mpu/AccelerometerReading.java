package com.finlay.rc.components.mpu;

import java.util.HashMap;
import java.util.Map;

// aX, aY, aZ are in multiples of g (9.81)
// gX, gY, gZ are in degrees per second
public class AccelerometerReading {

	private double accelerationX;
	private double accelerationY;
	private double accelerationZ;

	private double gyroX;
	private double gyroY;
	private double gyroZ;

	public AccelerometerReading(double aX, double aY, double aZ, double gX, double gY, double gZ) {
		this.accelerationX = aX;
		this.accelerationY = aY;
		this.accelerationZ = aZ;

		this.gyroX = gX;
		this.gyroY = gY;
		this.gyroZ = gZ;
	}

	public double getAccelerationX() {
		return accelerationX;
	}

	public double getAccelerationY() {
		return accelerationY;
	}

	public double getAccelerationZ() {
		return accelerationZ;
	}

	public double getGyroX() {
		return gyroX;
	}

	public double getGyroY() {
		return gyroY;
	}

	public double getGyroZ() {
		return gyroZ;
	}

	public Map<String, Object> getAsMap() {
		HashMap<String, Object> entries = new HashMap<>(6);

		entries.put("aX", this.accelerationX);
		entries.put("aY", this.accelerationY);
		entries.put("aZ", this.accelerationZ);
		entries.put("gX", this.gyroX);
		entries.put("gY", this.gyroY);
		entries.put("gZ", this.gyroZ);

		return entries;
	}

}