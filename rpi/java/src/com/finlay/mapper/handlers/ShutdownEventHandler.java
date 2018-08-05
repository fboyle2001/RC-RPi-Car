package com.finlay.mapper.handlers;

import com.finlay.mapper.RobotMain;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;
import lib.finlay.core.events.usable.SystemExitEvent;

@EventListener
public class ShutdownEventHandler {

	@EventMethod
	public void onShutdown(SystemExitEvent event) {
		RobotMain.getInstance().stop();
	}
	
}
