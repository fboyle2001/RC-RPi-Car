package com.finlay.rc.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.rc.components.PiconZero;
import com.finlay.rc.components.motion.AssistedTurn;
import com.finlay.rc.components.motion.AutoMove;
import com.finlay.rc.connection.MessageReceivedEvent;
import com.finlay.rc.connection.outgoing.JSONOutgoingMessage;

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
		
		AutoMove.getInstance().stop();
		
		logger.info("Stopped auto move");
		
		AssistedTurn.overrideHalt();
		
		logger.info("Stopped assisted turn");
		
		PiconZero.getInstance().stopMotion();
		
		logger.info("Stopped motion");
		
		JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
				.setStatusCode(200)
				.setRequestType(RequestType.OVERRIDE_HALT)
				.build();
		
		event.getConnection().send(message.toJson());
	}
	
}
