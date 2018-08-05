package com.finlay.mapper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.connection.MessageReceivedEvent;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class TemplateHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(TemplateHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.SENSOR_MEASURE_DISTANCE) {
			return;
		}
		
		logger.info("Handling message");
	}
	
}
