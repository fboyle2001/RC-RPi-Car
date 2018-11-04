package com.finlay.rcsim.ui;

import processing.core.PVector;

public abstract class StationaryObject {

	protected SimulationApplet parent;
	protected PVector position;
	protected boolean visible;
	
	public StationaryObject(SimulationApplet parent, PVector position, boolean visible) {
		this.parent = parent;
		this.position = position;
		this.visible = visible;
	}
	
	public abstract void drawLogic();
	
	public void draw() {
		if(isVisible()) {
			drawLogic();
		}
	}
	
	public void update() {}
	
	public PVector getPosition() {
		return position;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
}
