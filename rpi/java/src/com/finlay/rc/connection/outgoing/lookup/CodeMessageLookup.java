package com.finlay.rc.connection.outgoing.lookup;

public enum CodeMessageLookup {

	SUCCESS(200, "Success"),
	CREATED(201, "Auth key created"),
	BAD_REQUEST(400, "Bad request; check parameters"),
	FORBIDDEN(403, "Forbidden; incorrect auth key"),
	NOT_FOUND(404, "Operation not found"),
	GONE(410, "Server gone"),
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
