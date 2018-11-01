package com.finlay.rc.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.rc.connection.MessageReceivedEvent;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class ShutdownEventHandler {

	private static final Logger logger = LoggerFactory.getLogger(ShutdownEventHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.SHUTDOWN) {
			return;
		}
		
		logger.info("Handling message");
		logger.info("Received shutdown command");
		
		System.exit(0);
	}
	
}
