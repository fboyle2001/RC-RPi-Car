package com.finlay.rc.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.rc.components.PiconZero;
import com.finlay.rc.connection.MessageReceivedEvent;
import com.finlay.rc.connection.outgoing.JSONOutgoingMessage;

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
		
		int speed = 0;
		
		if(event.getType() != RequestType.MOTION_HALT) {
			try {
				speed = (int) event.getMessage().getRequest().getDouble("speed");
			} catch (NumberFormatException e) {
				logger.warn("Non-integer speed; instead received {}", event.getMessage().getRequest().getString("speed"));
				JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
						.setStatusCode(400)
						.setStatusMessage("Non-integer speed given")
						.build();
				event.getConnection().send(message.toJson());
				return;
			}
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
