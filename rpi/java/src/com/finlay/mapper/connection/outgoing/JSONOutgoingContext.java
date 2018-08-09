package com.finlay.mapper.connection.outgoing;

public class JSONOutgoingContext {

	private boolean response;
	private int requestType;
	
	public JSONOutgoingContext(boolean response, int requestType) {
		this.response = response;
		this.requestType = requestType;
	}

	public boolean isResponse() {
		return response;
	}
	
	public int getRequestType() {
		return requestType;
	}
	
}
