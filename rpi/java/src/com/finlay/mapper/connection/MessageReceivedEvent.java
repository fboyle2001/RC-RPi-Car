package com.finlay.mapper.connection;

import org.java_websocket.WebSocket;

import com.finlay.mapper.connection.incoming.JSONIncomingMessage;
import com.finlay.mapper.handlers.RequestType;

import lib.finlay.core.events.Event;

public class MessageReceivedEvent extends Event {

	private RequestType type;
	private JSONIncomingMessage message;
	private WebSocket connection;
	
	public MessageReceivedEvent(RequestType type, JSONIncomingMessage message, WebSocket connection) {
		this.type = type;
		this.message = message;
		this.connection = connection;
	}
	
	public RequestType getType() {
		return type;
	}
	
	public JSONIncomingMessage getMessage() {
		return message;
	}
	
	public WebSocket getConnection() {
		return connection;
	}
	
}
