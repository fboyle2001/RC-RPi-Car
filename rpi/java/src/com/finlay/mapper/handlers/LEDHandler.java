package com.finlay.mapper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.components.PiconZero;
import com.finlay.mapper.components.PiconZeroOutputType;
import com.finlay.mapper.connection.MessageReceivedEvent;
import com.finlay.mapper.connection.outgoing.JSONOutgoingMessage;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class LEDHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(LEDHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.LED_OFF && event.getType() != RequestType.LED_ON) {
			return;
		}
		
		logger.info("Handling message");
		
		int value = event.getType() == RequestType.LED_ON ? 1 : 0;
		int output = -1;
		
		if(!event.getMessage().getRequest().has("outputNumber")) {
			logger.info("No output number given for LED event");
			JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
					.setStatusCode(400)
					.setStatusSpecific(3)
					.build();
			event.getConnection().send(message.toJson());
			return;
		}

		try {
			output = (int) event.getMessage().getRequest().getDouble("outputNumber");
		} catch (NumberFormatException e) {
			logger.warn("Non-integer output; instead received {}", event.getMessage().getRequest().getString("outputNumber"));
			JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
					.setStatusCode(400)
					.setStatusSpecific(4)
					.build();
			event.getConnection().send(message.toJson());
			return;
		}
		
		PiconZeroOutputType type = PiconZero.getInstance().getProvisionedOutputType(output);
		
		if(type == null) {
			JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
					.setStatusCode(400)
					.setStatusSpecific(5)
					.build();
			event.getConnection().send(message.toJson());
			return;
		}
		
		if(!type.isInputValid(output)) {
			JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
					.setStatusCode(400)
					.setStatusSpecific(6)
					.build();
			event.getConnection().send(message.toJson());
			return;
		}
		
		PiconZero.getInstance().setOutput(output, value);
		
		JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
				.setStatusCode(200)
				.build();
		
		event.getConnection().send(message.toJson());
	}
	
}
