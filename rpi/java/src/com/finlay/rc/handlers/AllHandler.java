package com.finlay.rc.handlers;

import com.finlay.rc.Robot;
import com.finlay.rc.components.PiconZero;
import com.finlay.rc.connection.MessageReceivedEvent;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class AllHandler {

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(!Robot.isHardwareConnected()) {
			return;
		}
		
		PiconZero.getInstance().flashDigitalOutput(0, 200);
	}
	
}
