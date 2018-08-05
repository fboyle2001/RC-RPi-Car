package com.finlay.mapper.connection.message;

public class JSONMessageStatus {

	private int code;
	private String message;
	
	public JSONMessageStatus() {}
	
	protected JSONMessageStatus(int code, String message) {
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
