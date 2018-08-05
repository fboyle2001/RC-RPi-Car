package com.finlay.mapper.connection.outgoing;

public class JSONOutgoingStatus {

	private int code;
	private String message;
	private int specific;
	
	public JSONOutgoingStatus() {}
	
	protected JSONOutgoingStatus(int code, String message, int specific) {
		this.code = code;
		this.message = message;
		this.specific = specific;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getSpecific() {
		return specific;
	}
	
	@Override
	public String toString() {
		return code + " (" + specific + "): " + message;
	}
	
}
