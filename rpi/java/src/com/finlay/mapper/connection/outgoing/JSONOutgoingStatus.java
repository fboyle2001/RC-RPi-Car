package com.finlay.mapper.connection.outgoing;

public class JSONOutgoingStatus {

	private int code;
	private String message;
	private int specific;
	
	public JSONOutgoingStatus() {}
	
	protected JSONOutgoingStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return code + ": " + message;
	}
	
}
