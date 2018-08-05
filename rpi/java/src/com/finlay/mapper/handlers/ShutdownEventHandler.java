package com.finlay.mapper.handlers;

import com.finlay.mapper.Robot;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;
import lib.finlay.core.events.usable.SystemExitEvent;

@EventListener
public class ShutdownEventHandler {

	@EventMethod
	public void onShutdown(SystemExitEvent event) {
		Robot.getInstance().stop();
	}
	
}
