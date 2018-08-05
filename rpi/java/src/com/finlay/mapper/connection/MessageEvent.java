package com.finlay.mapper.connection;

import com.finlay.mapper.connection.incoming.JSONIncomingMessage;

import lib.finlay.core.events.Event;

public class MessageEvent extends Event {

	private JSONIncomingMessage message;
	
	public MessageEvent(JSONIncomingMessage message) {
		this.message = message;
	}
	
	public JSONIncomingMessage getMessage() {
		return message;
	}
	
}
