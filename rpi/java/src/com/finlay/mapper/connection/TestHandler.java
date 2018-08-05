package com.finlay.mapper.connection;

import com.finlay.mapper.connection.message.JSONMessage;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class TestHandler {

	@EventMethod
	public void onMessageReceived(MessageEvent event) {
		System.out.println("In event");
		JSONMessage message = event.getMessage();
		
		System.out.println(message.getStatus());
		
		message.getContent().forEach((key, value) -> {
			System.out.println(key + ": " + value);
		}); 
	}
	
}
