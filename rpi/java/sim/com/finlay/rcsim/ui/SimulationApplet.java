package com.finlay.rcsim.ui;

import processing.core.PApplet;
import processing.core.PVector;

public class SimulationApplet extends PApplet {

	public static void main(String[] args) {
		PApplet.main("com.finlay.rcsim.ui.SimulationApplet");
	}
	
	CarObject car = new CarObject(this, new PVector(400, 740));
	
	@Override
	public void settings() {
		size(800, 800);
		
	}
	
	@Override
	public void setup() {
		frameRate(60);
		
	}
	
	@Override
	public void draw() {
		background(255);
		car.update();
		car.draw();
	}
	
}
