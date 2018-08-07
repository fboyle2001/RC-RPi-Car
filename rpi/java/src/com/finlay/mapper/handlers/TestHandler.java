package com.finlay.mapper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.connection.MessageReceivedEvent;
import com.finlay.mapper.connection.outgoing.JSONOutgoingMessage;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class TestHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(TestHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.TEST_CONNECTION) {
			return;
		}
		
		logger.info("Handling message");
		
		JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
				.setStatusCode(200)
				.addContent(event.getMessage().getRequest().getData())
				.build();
		
		event.getConnection().send(message.toJson());
		
		logger.info("Handled test connection message; replied with received parameters");
	}
	
}
