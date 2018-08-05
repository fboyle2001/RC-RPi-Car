package com.finlay.mapper.connection;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.connection.incoming.JSONIncomingMessage;
import com.finlay.mapper.connection.outgoing.JSONOutgoingMessage;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import lib.finlay.core.events.EventManager;

public class MessageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
	private static final Gson gson = new Gson();
	
	private static String authKey;
	
	public static void process(WebSocket connection, String message) {
		JSONIncomingMessage incoming;
		JSONOutgoingMessage badRequest = new JSONOutgoingMessage.Builder().setStatusCode(400).setStatusSpecific(1).build();
		
		try {
			incoming = gson.fromJson(message, JSONIncomingMessage.class);
		} catch (JsonParseException ex) {
			connection.send(badRequest.toJson());
			logger.error("Unable to parse message");
			logger.error(ex.getMessage());
			return;
		}
		
		if(incoming == null || incoming.getAuthKey() == null) {
			connection.send(badRequest.toJson());
			logger.error("Unable to parse message");
			return;
		}
		
		if(incoming.getAuthKey() != authKey) {
			JSONOutgoingMessage forbidden = new JSONOutgoingMessage.Builder().setStatusCode(403).build();
			connection.send(forbidden.toJson());
			logger.warn("Invalid auth key given");
			return;
		}
		
		logger.info("Parsed message successfully");
		EventManager.callEvent(new MessageEvent(incoming));
	}

	public static void setAuthKey(String authKey) {
		MessageProcessor.authKey = authKey;
	}

}
