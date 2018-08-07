package com.finlay.mapper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.Robot;
import com.finlay.mapper.components.AutoMove;
import com.finlay.mapper.connection.MessageReceivedEvent;
import com.finlay.mapper.connection.outgoing.JSONOutgoingMessage;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class AutoHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AutoHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.AUTO_MOVE_START && event.getType() != RequestType.AUTO_MOVE_STOP) {
			return;
		}
		
		logger.info("Handling message");
		
		if(!Robot.isHardwareConnected()) {
			logger.info("Hardware is not connected. Informing user");
			JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
					.setStatusCode(404)
					.setStatusSpecific(1)
					.build();
			event.getConnection().send(message.toJson());
			return;
		}
		
		switch(event.getType()) {
		case AUTO_MOVE_START:
			logger.info("Received command to start auto move");
			AutoMove.getInstance().start();
			break;
		case AUTO_MOVE_STOP:
			logger.info("Received command to stop auto move");
			AutoMove.getInstance().stop();
			break;
		default:
			break;
		}
		
		JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
				.setStatusCode(200)
				.build();
		
		event.getConnection().send(message.toJson());
	}
	
}
