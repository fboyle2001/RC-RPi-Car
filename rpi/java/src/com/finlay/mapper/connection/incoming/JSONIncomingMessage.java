package com.finlay.mapper.connection.incoming;

public class JSONIncomingMessage {

	private String key;
	private JSONIncomingRequest request;
	
	public JSONIncomingMessage() {}
	
	public String getKey() {
		return key;
	}
	
	public JSONIncomingRequest getRequest() {
		return request;
	}
	
}
