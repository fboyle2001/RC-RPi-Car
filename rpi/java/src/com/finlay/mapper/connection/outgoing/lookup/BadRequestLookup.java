package com.finlay.mapper.connection.outgoing.lookup;

public class BadRequestLookup extends SpecificLookup {

	@Override
	public String getMessage(int specific) {
		switch(specific) {
		case 1:
			return "Invalid format";
		case 2:
			return "Non-integer speed given";
		default:
			return CodeMessageLookup.getDefaultMessage(400);
		}
	}

}
