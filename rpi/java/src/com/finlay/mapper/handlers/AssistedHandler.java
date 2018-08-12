package com.finlay.mapper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.Robot;
import com.finlay.mapper.components.AssistedTurn;
import com.finlay.mapper.connection.MessageReceivedEvent;
import com.finlay.mapper.connection.outgoing.JSONOutgoingMessage;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class AssistedHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AssistedHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.ASSISTED_LEFT && event.getType() != RequestType.ASSISTED_RIGHT) {
			return;
		}
		
		if(!Robot.isHardwareConnected()) {
			logger.info("Hardware is not connected. Informing user");
			JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
					.setStatusCode(404)
					.setStatusSpecific(1)
					.build();
			event.getConnection().send(message.toJson());
			return;
		}
		
		if(!event.getMessage().getRequest().has("speed")) {
			return;
		}
		
		int speed;

		try {
			speed = (int) event.getMessage().getRequest().getDouble("speed");
		} catch (NumberFormatException e) {
			logger.warn("Non-integer speed; instead received {}", event.getMessage().getRequest().getString("speed"));
			JSONOutgoingMessage message = new JSONOutgoingMessage.Builder().setStatusCode(400).setStatusSpecific(2).build();
			event.getConnection().send(message.toJson());
			return;
		}
		
		logger.info("Handling message");
		
		if(event.getType() == RequestType.ASSISTED_LEFT) {
			AssistedTurn.turnLeft(speed, 5);
		} else if (event.getType() == RequestType.ASSISTED_RIGHT) {
			AssistedTurn.turnRight(speed, 5);
		}
		
		JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
				.setStatusCode(200)
				.build();
		
		event.getConnection().send(message.toJson());
	}
	
}