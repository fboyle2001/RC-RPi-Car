package com.finlay.mapper.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.connection.incoming.JSONIncomingMessage;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import lib.finlay.core.events.EventManager;

public class MessageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
	private static final Gson gson = new Gson();
	
	public static void process(String message) {
		JSONIncomingMessage incoming;
		
		try {
			incoming = gson.fromJson(message, JSONIncomingMessage.class);
		} catch (JsonParseException ex) {
			logger.error("Unable to parse message");
			logger.error(ex.getMessage());
			return;
		}
		
		if(incoming == null || incoming.getKey() == null) {
			logger.error("Unable to parse message");
			return;
		}
		
		logger.info("Parsed message successfully");
		EventManager.callEvent(new MessageEvent(incoming));
	}

}
