package com.finlay.rcsim.ui;

import processing.core.PVector;

public abstract class MoveableObject extends StationaryObject {

	protected PVector velocity;
	protected PVector acceleration;
	protected float velocityLimit;

	public MoveableObject(SimulationApplet parent, PVector position, boolean visible) {
		this(parent, position, visible, -1);
	}
	
	public MoveableObject(SimulationApplet parent, PVector position, boolean visible, float velocityLimit) {
		super(parent, position, visible);
		this.velocityLimit = velocityLimit;
		this.velocity = new PVector(0, 0);
		this.acceleration = new PVector(0, 0);
	}
	
	@Override
	public void update() {
		super.update();
		
		velocity = velocity.add(acceleration);
		
		if(velocityLimit != -1) {
			velocity.limit(velocityLimit);
		}
		
		position = position.add(velocity);
	}
	
	public PVector getAcceleration() {
		return acceleration;
	}
	
	public PVector getVelocity() {
		return velocity;
	}

}
