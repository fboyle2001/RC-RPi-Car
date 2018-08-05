package com.finlay.mapper.connection.incoming;

import java.util.Map;

public class JSONIncomingRequest {

	private int type;
	private Map<String, Object> data;
	
	public JSONIncomingRequest() {}
	
	public Map<String, Object> getData() {
		return data;
	}
	
	public String getString(String key) {
		return data.get(key).toString();
	}
	
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}
	
	public int getInteger(String key) throws NumberFormatException {
		return Integer.parseInt(getString(key));
	}
	
	public double getDouble(String key) throws NumberFormatException {
		return Double.parseDouble(getString(key));
	}
	
	public int getType() {
		return type;
	}
	
}
