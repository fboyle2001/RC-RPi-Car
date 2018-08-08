package com.finlay.mapper.handlers;

import com.finlay.mapper.components.PiconZero;
import com.finlay.mapper.connection.MessageReceivedEvent;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class AllHandler {

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		PiconZero.getInstance().flashDigitalOutput(0, 200);
	}
	
}
