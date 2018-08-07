package com.finlay.mapper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.Robot;
import com.finlay.mapper.components.PiconZero;
import com.finlay.mapper.connection.MessageReceivedEvent;
import com.finlay.mapper.connection.outgoing.JSONOutgoingMessage;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class MotionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(MotionHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.MOTION_FORWARD && event.getType() != RequestType.MOTION_REVERSE 
				&& event.getType() != RequestType.MOTION_RIGHT && event.getType() != RequestType.MOTION_LEFT 
				&& event.getType() != RequestType.MOTION_HALT) {
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
		
		int speed;
		
		try {
			speed = (int) event.getMessage().getRequest().getDouble("speed");
		} catch (NumberFormatException e) {
			logger.warn("Non-integer speed instead received {}", event.getMessage().getRequest().getString("speed"));
			JSONOutgoingMessage message = new JSONOutgoingMessage.Builder().setStatusCode(400).setStatusSpecific(2).build();
			event.getConnection().send(message.toJson());
			return;
		}
		
		switch(event.getType()) {
		case MOTION_FORWARD:
			PiconZero.getInstance().forward(speed);
			break;
		case MOTION_HALT:
			PiconZero.getInstance().stopMotion();
			break;
		case MOTION_LEFT:
			PiconZero.getInstance().left(speed);
			break;
		case MOTION_REVERSE:
			PiconZero.getInstance().reverse(speed);
			break;
		case MOTION_RIGHT:
			PiconZero.getInstance().right(speed);
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
