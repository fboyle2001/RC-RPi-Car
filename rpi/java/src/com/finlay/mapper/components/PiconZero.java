package com.finlay.mapper.components;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.Robot;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.RaspiBcmPin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import lib.finlay.core.io.ConfigurationFile;

public class PiconZero {
	
	private static final Logger logger = LoggerFactory.getLogger(PiconZero.class);
	
	private static PiconZero instance = null;
	
	public static final PiconZero getInstance() {
		if(instance == null) {
			instance = new PiconZero();
		}
		
		return instance;
	}
	
	private int retries;
	private int speedOfSound;
	private GpioPinDigitalMultipurpose ultrasonicSensor;
	private I2CDevice device;
	
	private PiconZero() {
		try {
			this.retries = ConfigurationFile.getConfigurationOf(Robot.class).getInteger("retries");
		} catch (NumberFormatException e) {
			logger.warn("Invalid number of retries, defaulting to 10");
			this.retries = 10;
		}

		try {
			this.speedOfSound = ConfigurationFile.getConfigurationOf(Robot.class).getInteger("speedOfSound");
		} catch (NumberFormatException e) {
			logger.warn("Invalid number for speed of sound, defaulting to 343");
			this.speedOfSound = 343;
		}
		
		this.ultrasonicSensor = GpioFactory.getInstance().provisionDigitalMultipurposePin(RaspiBcmPin.GPIO_20, PinMode.DIGITAL_OUTPUT);
		
		try {
			this.device = I2CFactory.getInstance(I2CBus.BUS_1).getDevice(0x22);
		} catch (IOException | UnsupportedBusNumberException e) {
			logger.error("Unable to get PiconZero hardware interface");
			logger.error("{}", e);
		}
	}
	
	private void setMotorSpeed(int motor, int value) {
		if(motor >= 0 && motor <= 1 && value >= -128 && value < 128) {
			for(int i = 0; i < retries; i++) {
				try {
					device.write(motor, (byte) value);
					logger.info("Set motor {} speed to {}", motor, value);
					return;
				} catch (IOException e) {
					logger.info("Trying to set motor speed, attempt {} of {}", i + 1, retries);
				}
			}
		} else {
			logger.warn("Motor speed excessive got {} expected -128 <= speed < 128", value);
			return;
		}
		
		logger.warn("Unable to set motor speed");
	}
	
	public void forward(int speed) {
		logger.info("Attempting to set forward speed to {}", speed);
		setMotorSpeed(0, speed);
		setMotorSpeed(1, -speed);
	}
	
	public void reverse(int speed) {
		logger.info("Attempting to set reverse speed to {}", speed);
		setMotorSpeed(0, -speed);
		setMotorSpeed(1, speed);
	}
	
	public void right(int speed) {
		logger.info("Attempting to set right speed to {}", speed);
		setMotorSpeed(0, speed);
		setMotorSpeed(1, speed);
	}
	
	public void left(int speed) {
		logger.info("Attempting to set left speed to {}", speed);
		setMotorSpeed(0, -speed);
		setMotorSpeed(1, -speed);
	}
	
	public void stopMotion() {
		logger.info("Attempting to halt motion");
		setMotorSpeed(0, 0);
		setMotorSpeed(1, 0);
	}
	
	public double calculateDistanceToObject() {
		logger.info("Attempting to calculate distance to object");
		ultrasonicSensor.setMode(PinMode.DIGITAL_OUTPUT);
		ultrasonicSensor.high();
		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			logger.error("Unable to read distance");
			logger.error("{}", e);
			return -1;
		}
		
		ultrasonicSensor.low();
		ultrasonicSensor.setMode(PinMode.DIGITAL_INPUT);
		
		long lastLow;
		
		do {
			lastLow = System.nanoTime();
		} while (ultrasonicSensor.isLow());

		long lastHigh;
		
		do {
			lastHigh = System.nanoTime();
		} while (ultrasonicSensor.isHigh());
		
		long duration = lastHigh - lastLow;
		
		double durationInSeconds = duration;
		durationInSeconds /= Math.pow(10, 9);
		durationInSeconds /= 2;
		
		return durationInSeconds * speedOfSound;
	}
	
}
