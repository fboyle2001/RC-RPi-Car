package com.finlay.mapper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.Robot;
import com.finlay.mapper.connection.MessageReceivedEvent;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;
import lib.finlay.core.events.usable.SystemExitEvent;

@EventListener
public class ShutdownEventHandler {

	private static final Logger logger = LoggerFactory.getLogger(ShutdownEventHandler.class);
	
	@EventMethod
	public void onShutdownEvent(SystemExitEvent event) {
		Robot.getInstance().stop();
	}

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
