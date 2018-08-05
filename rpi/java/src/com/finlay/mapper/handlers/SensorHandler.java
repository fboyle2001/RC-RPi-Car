package com.finlay.mapper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.components.PiconZero;
import com.finlay.mapper.connection.MessageReceivedEvent;
import com.finlay.mapper.connection.outgoing.JSONOutgoingMessage;

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
			JSONOutgoingMessage response = new JSONOutgoingMessage.Builder().setStatusCode(500).setStatusMessage("Failed to read distance").build();
			event.getConnection().send(response.toJson());
			logger.info("Sent response with error message");
			return;
		}
		
		JSONOutgoingMessage response = new JSONOutgoingMessage.Builder().setStatusCode(200).setKeyValue("distance", distance).build();
		event.getConnection().send(response.toJson());
		logger.info("Sent response with distance");
	}
	
}
