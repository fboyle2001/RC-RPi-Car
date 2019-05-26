package com.finlay.rc.components.mpu;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class MPU6050 {

	private static final int MPU_DEVICE   = 0x68;
	private static final int PWR_MGMT_1   = 0x6B;
	private static final int SMPLRT_DIV   = 0x19;
	private static final int CONFIG       = 0x1A;
	private static final int GYRO_CONFIG  = 0x1B;
	private static final int INT_ENABLE   = 0x38;
	private static final int ACCEL_XOUT_H = 0x3B;
	private static final int ACCEL_YOUT_H = 0x3D;
	private static final int ACCEL_ZOUT_H = 0x3F;
	private static final int GYRO_XOUT_H  = 0x43;
	private static final int GYRO_YOUT_H  = 0x45;
	private static final int GYRO_ZOUT_H  = 0x47;

	private static final Logger logger = LoggerFactory.getLogger(MPU6050.class);
	
	private static MPU6050 instance = null;
	
	public static final MPU6050 getInstance() {
		if(instance == null) {
			instance = new MPU6050();
		}
		
		return instance;
	}
	
	private I2CDevice device;
	
	private MPU6050() {
		try {
			this.device = I2CFactory.getInstance(I2CBus.BUS_1).getDevice(MPU_DEVICE);
		} catch (IOException | UnsupportedBusNumberException e) {
			logger.error("Unable to get MPU6050 hardware interface");
			logger.error("{}", e);
		}
		
		try {
			device.write(SMPLRT_DIV, (byte) 7);
		} catch (IOException e) {
			logger.error("Unable to write to SMPLRT_DIV");
			logger.error("{}", e);
		}
		
		try {
			device.write(PWR_MGMT_1, (byte) 1);
		} catch (IOException e) {
			logger.error("Unable to write to PWR_MGMT_1");
			logger.error("{}", e);
		}
		try {
			device.write(CONFIG, (byte) 0);
		} catch (IOException e) {
			logger.error("Unable to write to CONFIG");
			logger.error("{}", e);
		}
		try {
			device.write(GYRO_CONFIG, (byte) 24);
		} catch (IOException e) {
			logger.error("Unable to write to GYRO_CONFIG");
			logger.error("{}", e);
		}
		try {
			device.write(INT_ENABLE, (byte) 1);
		} catch (IOException e) {
			logger.error("Unable to write to INT_ENABLE");
			logger.error("{}", e);
		}
	}
	
	private int readRawData(int address) {
		int high;
		
		try {
			high = device.read(address);
		} catch (IOException e) {
			logger.error("Unable to read high");
			logger.error("{}", e);
			return 0;
		}
		
		int low;
		
		try {
			low = device.read(address + 1);
		} catch (IOException e) {
			logger.error("Unable to read low");
			logger.error("{}", e);
			return 0;
		}
		
		int value = ((high << 8) | low);
		
		if(value > 32768) {
			value = value - 65536;
		}
		
		return value;
	}
	
	public AccelerometerReading getSensorData() {
		double aX = readRawData(ACCEL_XOUT_H) / 16384;
		double aY = readRawData(ACCEL_YOUT_H) / 16384;
		double aZ = readRawData(ACCEL_ZOUT_H) / 16384;

		double gX = readRawData(GYRO_XOUT_H) / 131;
		double gY = readRawData(GYRO_YOUT_H) / 131;
		double gZ = readRawData(GYRO_ZOUT_H) / 131;
		
		return new AccelerometerReading(aX, aY, aZ, gX, gY, gZ);
	}
	
}
