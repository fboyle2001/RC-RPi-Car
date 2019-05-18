package com.finlay.rcsim.ui;

import processing.core.PVector;

@SuppressWarnings("unused")
public class CarObject extends MoveableObject {

	private static final PVector zeroMotorAcceleration = new PVector(-2, 2);
	private static final PVector oneMotorAcceleration = new PVector(2, 2);
	
	private PVector leftAcceleration;
	private PVector rightAcceleration;
	
	public CarObject(SimulationApplet parent, PVector position) {
		super(parent, position, true, 2); //need to calculate velocity limit
		this.leftAcceleration = new PVector();
		this.rightAcceleration = new PVector();
	}

	@Override
	public void drawLogic() {
		parent.color(128, 128, 128);
		parent.rect(position.x, position.y, 40, 50);
	}

	@Override
	public void update() {
		super.update();
	}
	
	public void setMotorSpeed(int motor, int value) {
		
	}
	
	public void forward(int speed) {
		setMotorSpeed(0, speed);
		setMotorSpeed(1, -speed);
	}
	
	public void reverse(int speed) {
		setMotorSpeed(0, -speed);
		setMotorSpeed(1, speed);
	}
	
	public void right(int speed) {
		setMotorSpeed(0, -speed);
		setMotorSpeed(1, -speed);
	}
	
	public void left(int speed) {
		setMotorSpeed(0, speed);
		setMotorSpeed(1, speed);
	}
	
	public void stopMotion() {
		setMotorSpeed(0, 0);
		setMotorSpeed(1, 0);
	}
	
}
