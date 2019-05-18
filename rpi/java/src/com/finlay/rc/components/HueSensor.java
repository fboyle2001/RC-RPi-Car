package com.finlay.rc.components;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.finlayboyle.hue.item.light.HueLightContainer;
import me.finlayboyle.hue.item.light.HueLightStateUpdate;

public class HueSensor {

	private static final Logger logger = LoggerFactory.getLogger(HueSensor.class);
	
	private static final double configurationPeriod = 12;
	private static final double configurationNodeCooldown = 0.25;

	private boolean configured;
	private double upperBound;
	private double lowerBound;
	private boolean stop;
	private boolean on;
	private HueLightContainer light;
	
	public HueSensor(HueLightContainer light) {
		this.configured = false;
		this.stop = false;
		this.light = light;
		this.on = false;
	}
	
	public boolean start() {
		if(!light.isReachable()) {
			logger.warn("Unable to reach " + light.getCustomName() + " light. Sensor not started.");
			return false;
		}
		
		if(on) {
			logger.warn("Unable to start as it has already been started");
			return false;
		}
		
		configure();
		startSensor();
		return true;
	}
	
	public void stop() {
		this.stop = true;
		logger.info("Sent stop signal");
	}
	
	private void startSensor() {
		logger.info("Sensor started");
		this.on = true;
		this.stop = false;
		
		new Thread(() -> {
			boolean currentState = light.getLight().getState().isOn();
			
			while(!stop) {
				double distance = PiconZero.getInstance().calculateDistanceSilently();
				
				if(distance < lowerBound) {
					logger.debug("d: " + distance);
					logger.info("Distance was below lower bound. Something blocked path. Toggling light.");

					currentState = !currentState;
					
					HueLightStateUpdate changes = new HueLightStateUpdate(light);
					changes.setOn(currentState);
					
					light.applyChange(changes.toJSON(), r -> {
						logger.debug("Applied light change request.");
					});
					
					logger.info("Sent light toggle request. Sleeping...");
					
					try {
						Thread.sleep(1800);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (distance > upperBound) {
					logger.debug("d: " + distance);
					logger.info("Distance exceeded upper bound. Likely an outlier. No action taken.");
				} else {
					//logger.debug("Distance, " + distance + ", satisfied the bounds. No action taken.");
				}
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			logger.info("Sensor stopped");
			this.on = false;
		}, "HueSensorThread").start();
	}

	private void configure() {
		this.configured = false;
		
		logger.info("Configuring sensor. Will take " + configurationPeriod + "s to become operational");
		logger.info("Collecting " + (configurationPeriod / configurationNodeCooldown) + " nodes");
		//Over the course of 12 seconds, take measurements and then calculate the expected distance and variance
		//Take 48 data points so one every 0.25s
		
		int nodes = 0;
		int maxNodes = (int) (configurationPeriod / configurationNodeCooldown);
		
		long sleepPeriod = (long) (configurationNodeCooldown * 1000);
		ArrayList<Double> distances = new ArrayList<>();
		
		while(nodes != maxNodes) {
			double distance = PiconZero.getInstance().calculateDistanceSilently();
			
			distances.add(distance);
			
			nodes++;
			
			try {
				Thread.sleep(sleepPeriod);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		logger.info("Collected " + nodes + " data points. Running statistical analysis.");
		
		int n = distances.size();
		double mean = distances.stream().mapToDouble(d -> d.doubleValue()).sum() / n;
		double squares = distances.stream().mapToDouble(d -> Math.pow(d.doubleValue(), 2)).sum();
		double sxx = squares - n * Math.pow(mean, 2);
		double variance = sxx / (n - 1);
		double sd = Math.sqrt(variance);
		
		logger.info("Mean: " + mean);
		logger.info("Standard Deviation: " + sd);
		
		this.upperBound = mean + 10 * sd;
		this.lowerBound = mean - 10 * sd;
		
		logger.info("Upper Bound: " + upperBound);
		logger.info("Lower Bound: " + lowerBound);
		
		if(lowerBound < 0) {
			logger.warn("Lower bound, " + lowerBound + ", is less than 0 so the sensor will never trigger.");
		}
		
		this.configured = true;
	}
	
	public boolean isConfigured() {
		return configured;
	}
	
}
