package com.finlay.mapper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.components.AutoMove;
import com.finlay.mapper.components.PiconZero;
import com.finlay.mapper.connection.MessageReceivedEvent;
import com.finlay.mapper.connection.outgoing.JSONOutgoingMessage;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class OverrideHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(OverrideHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.OVERRIDE_HALT) {
			return;
		}
		
		logger.info("Handling message");
		
		PiconZero.getInstance().stopMotion();
		
		logger.info("Stopped motion");
		
		AutoMove.getInstance().stop();
		
		logger.info("Stopped auto move");
		
		JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
				.setStatusCode(200)
				.build();
		
		event.getConnection().send(message.toJson());
	}
	
}
