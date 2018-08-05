package com.finlay.mapper.connection;

import com.finlay.mapper.connection.message.JSONMessage;

import lib.finlay.core.events.Event;

public class MessageEvent extends Event {

	private JSONMessage message;
	
	public MessageEvent(JSONMessage message) {
		this.message = message;
	}
	
	public JSONMessage getMessage() {
		return message;
	}
	
}
