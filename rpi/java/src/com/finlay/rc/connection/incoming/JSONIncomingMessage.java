package com.finlay.rc.connection.incoming;

public class JSONIncomingMessage {

	private String authKey;
	private JSONIncomingRequest request;
	
	public JSONIncomingMessage() {}
	
	public String getAuthKey() {
		return authKey;
	}
	
	public JSONIncomingRequest getRequest() {
		return request;
	}
	
}
