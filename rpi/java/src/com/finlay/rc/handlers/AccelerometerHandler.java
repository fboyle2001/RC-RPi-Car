package com.finlay.rc.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.rc.components.MPU6050;
import com.finlay.rc.components.MPU6050.AccelerometerReading;
import com.finlay.rc.connection.MessageReceivedEvent;
import com.finlay.rc.connection.outgoing.JSONOutgoingMessage;
import com.finlay.rc.connection.outgoing.JSONOutgoingMessage.Builder;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class AccelerometerHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AccelerometerHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.GET_CURRENT_ACC_READING) {
			return;
		}
		
		logger.info("Handling message");
		
		Builder message = new JSONOutgoingMessage.Builder().setStatusCode(200);
		
		switch(event.getType()) {
		case GET_CURRENT_ACC_READING:
			AccelerometerReading reading = MPU6050.getInstance().getSensorData();
			message.addContent(reading.getAsMap());
			break;
		default:
			return;
		}
		
		event.getConnection().send(message.build().toJson());
	}
	
}
