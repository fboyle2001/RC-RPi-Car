package com.finlay.mapper.handlers;

public enum RequestType {

	MOTION_FORWARD(1),
	MOTION_REVERSE(2),
	MOTION_RIGHT(3),
	MOTION_LEFT(4),
	MOTION_HALT(5),
	SENSOR_MEASURE_DISTANCE(6),
	TEST_CONNECTION(7),
	SHUTDOWN(8),
	AUTO_MOVE_START(9),
	AUTO_MOVE_STOP(10);
	
	private int type;
	
	private RequestType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
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
