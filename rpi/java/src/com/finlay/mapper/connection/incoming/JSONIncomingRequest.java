package com.finlay.mapper.connection.incoming;

import java.util.Map;

public class JSONIncomingRequest {

	private int type;
	private Map<String, Object> data;
	
	public JSONIncomingRequest() {}
	
	public Map<String, Object> getData() {
		return data;
	}
	
	public int getType() {
		return type;
	}
	
}
