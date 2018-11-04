package com.finlay.rcsim.ui;

import processing.core.PVector;

public class CarObject extends MoveableObject {

	public CarObject(SimulationApplet parent, PVector position) {
		super(parent, position, true, 2); //need to calculate velocity limit
		this.acceleration = new PVector(-0.2F, -0.2F);
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
	
}
