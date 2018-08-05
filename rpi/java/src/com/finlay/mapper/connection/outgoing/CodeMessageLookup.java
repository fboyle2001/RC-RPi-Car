package com.finlay.mapper.connection.outgoing;

public enum CodeMessageLookup {

	SUCCESS(200, "Success"),
	BAD_REQUEST(400, "Bad request; check parameters"),
	FORBIDDEN(403, "Forbidden"),
	NOT_FOUND(404, "Operation not found"),
	SERVER_ERROR(500, "Internal server error; check log"),
	UNAVAILABLE(503, "Server already has a client");
	
	private int code;
	private String message;
	
	private CodeMessageLookup(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public static String getDefaultMessage(int code) {
		for(CodeMessageLookup item : values()) {
			if(item.getCode() == code) {
				return item.getMessage();
			}
		}
		
		return "Undefined";
	}
	
}
