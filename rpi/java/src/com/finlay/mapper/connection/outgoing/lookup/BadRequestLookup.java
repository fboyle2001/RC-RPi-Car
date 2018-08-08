package com.finlay.mapper.connection.outgoing.lookup;

public class BadRequestLookup extends SpecificLookup {

	@Override
	public String getMessage(int specific) {
		switch(specific) {
		case 1:
			return "Invalid format";
		case 2:
			return "Non-integer speed given";
		case 3:
			return "LED output number must be specified";
		case 4:
			return "LED output number must be an integer";
		case 5:
			return "LED output number must be provisioned";
		case 6:
			return "LED output number out of range";
		default:
			return CodeMessageLookup.getDefaultMessage(400);
		}
	}

}
