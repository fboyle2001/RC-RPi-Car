package com.finlay.mapper.connection.outgoing.lookup;

public enum CodeMessageLookup {

	SUCCESS(200, "Success", null),
	CREATED(201, "Auth key created", null),
	BAD_REQUEST(400, "Bad request; check parameters", new BadRequestLookup()),
	FORBIDDEN(403, "Forbidden; incorrect auth key", null),
	NOT_FOUND(404, "Operation not found", null),
	GONE(410, "Server gone", null),
	SERVER_ERROR(500, "Internal server error; check log", null),
	UNAVAILABLE(503, "Server already has a client", null);
	
	private int code;
	private String message;
	private SpecificLookup lookup;
	
	private CodeMessageLookup(int code, String message, SpecificLookup lookup) {
		this.code = code;
		this.message = message;
		this.lookup = lookup;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public SpecificLookup getLookup() {
		return lookup;
	}
	
	public static String getDefaultMessage(int code) {
		for(CodeMessageLookup item : values()) {
			if(item.getCode() == code) {
				return item.getMessage();
			}
		}
		
		return "Undefined";
	}
	
	public static SpecificLookup getSpecific(int code) {
		for(CodeMessageLookup item : values()) {
			if(item.getCode() == code) {
				return item.getLookup();
			}
		}
		
		return null;
	}
	
}
