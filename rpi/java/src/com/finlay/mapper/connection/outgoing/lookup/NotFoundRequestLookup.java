package com.finlay.mapper.connection.outgoing.lookup;

public class NotFoundRequestLookup extends SpecificLookup {

	@Override
	public String getMessage(int specific) {
		switch(specific) {
		case 1:
			return "Hardware is not connected; action not performed";
		default:
			return CodeMessageLookup.getDefaultMessage(404);
		}
	}

}
