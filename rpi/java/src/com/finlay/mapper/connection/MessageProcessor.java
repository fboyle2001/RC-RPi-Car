package com.finlay.mapper.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.connection.message.JSONMessage;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class MessageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
	private static final Gson gson = new Gson();
	
	public static void process(String message) {
		JSONMessage readable;
		
		try {
			readable = gson.fromJson(message, JSONMessage.class);
		} catch (JsonParseException ex) {
			logger.error("Unable to parse message");
			logger.error(ex.getMessage());
			return;
		}
		
		if(readable == null || readable.getStatus() == null) {
			logger.error("Unable to parse message");
			return;
		}
		
		logger.info("Parsed message successfully");
		System.out.println(readable.getStatus());
	}

}
