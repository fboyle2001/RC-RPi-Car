package com.finlay.mapper.connection;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.Robot;
import com.finlay.mapper.connection.incoming.JSONIncomingMessage;
import com.finlay.mapper.connection.outgoing.JSONOutgoingMessage;
import com.finlay.mapper.handlers.RequestType;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import lib.finlay.core.events.EventManager;

public class MessageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
	private static final Gson gson = new Gson();
	
	private static String authKey;
	
	public static void process(WebSocket connection, String message) {
		JSONIncomingMessage incoming;
		JSONOutgoingMessage badRequest = new JSONOutgoingMessage.Builder()
				.setStatusCode(400)
				.setStatusMessage("Invalid format")
				.build();
		
		try {
			incoming = gson.fromJson(message, JSONIncomingMessage.class);
		} catch (JsonParseException ex) {
			connection.send(badRequest.toJson());
			logger.error("Unable to parse message");
			logger.error("{}", ex);
			return;
		}
		
		if(incoming == null || incoming.getAuthKey() == null) {
			connection.send(badRequest.toJson());
			logger.error("Unable to parse message");
			return;
		}
		
		if(!incoming.getAuthKey().equals(authKey)) {
			JSONOutgoingMessage forbidden = new JSONOutgoingMessage.Builder().setStatusCode(403).build();
			connection.send(forbidden.toJson());
			logger.warn("Invalid auth key given");
			return;
		}
		
		logger.info("Parsed message successfully");
		
		RequestType type = RequestType.getByType(incoming.getRequest().getType());
		
		if(type.doesRequireHardware() && !Robot.isHardwareConnected()) {
			logger.info("Hardware is not connected. Informing user");
			JSONOutgoingMessage out = new JSONOutgoingMessage.Builder()
					.setStatusCode(404)
					.setStatusMessage("Hardware is not connected; action not performed")
					.build();
			connection.send(out.toJson());
			return;
		}
		
		EventManager.callEvent(new MessageReceivedEvent(type, incoming, connection));
	}

	public static void setAuthKey(String authKey) {
		logger.info("Updated auth key");
		MessageProcessor.authKey = authKey;
	}

}
