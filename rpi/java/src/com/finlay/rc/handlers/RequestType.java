package com.finlay.rc.handlers;

public enum RequestType {

	MOTION_FORWARD(1, true),
	MOTION_REVERSE(2, true),
	MOTION_RIGHT(3, true),
	MOTION_LEFT(4, true),
	MOTION_HALT(5, true),
	SENSOR_MEASURE_DISTANCE(6, true),
	TEST_CONNECTION(7, false),
	SHUTDOWN(8, false),
	AUTO_MOVE_START(9, true),
	AUTO_MOVE_STOP(10, true),
	OVERRIDE_HALT(11, true),
	LED_ON(12, true),
	LED_OFF(13, true),
	ASSISTED_LEFT(14, true),
	ASSISTED_RIGHT(15, true), 
	ASSISTED_U_TURN(16, true);
	
	private int type;
	private boolean requiresHardware;
	
	private RequestType(int type, boolean requiresHardware) {
		this.type = type;
		this.requiresHardware = requiresHardware;
	}
	
	public int getType() {
		return type;
	}
	
	public boolean doesRequireHardware() {
		return requiresHardware;
	}
	
	public static RequestType getByType(int type) {
		for(RequestType req : values()) {
			if(req.getType() == type) {
				return req;
			}
		}
		
		return null;
	}
	
}
