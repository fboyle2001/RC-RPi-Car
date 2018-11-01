package com.finlay.rc.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.rc.components.PiconZero;
import com.finlay.rc.connection.MessageReceivedEvent;
import com.finlay.rc.connection.outgoing.JSONOutgoingMessage;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class SensorHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(SensorHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.SENSOR_MEASURE_DISTANCE) {
			return;
		}
		
		logger.info("Handling message");
		
		double distance = PiconZero.getInstance().calculateDistanceToObject();
		
		if(distance == -1) {
			JSONOutgoingMessage response = new JSONOutgoingMessage.Builder()
					.setStatusCode(500)
					.setStatusMessage("Failed to read distance")
					.setRequestType(RequestType.SENSOR_MEASURE_DISTANCE)
					.build();
			
			event.getConnection().send(response.toJson());
			logger.info("Sent response with error message");
			return;
		}
		
		JSONOutgoingMessage response = new JSONOutgoingMessage.Builder()
				.setStatusCode(200)
				.setRequestType(RequestType.SENSOR_MEASURE_DISTANCE)
				.setKeyValue("distance", distance)
				.build();
		
		event.getConnection().send(response.toJson());
		logger.info("Sent response with distance");
	}
	
}
