package com.finlay.rc.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.rc.components.HueSensor;
import com.finlay.rc.connection.MessageReceivedEvent;
import com.finlay.rc.connection.outgoing.JSONOutgoingMessage;
import com.finlay.rc.connection.outgoing.lookup.CodeMessageLookup;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;
import me.finlayboyle.hue.item.light.HueLightContainer;

@EventListener
public class HueHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(HueHandler.class);
	private static HueLightContainer light;
	private static HueSensor sensor;

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.BEGIN_HUE_SENSOR && event.getType() != RequestType.STOP_HUE_SENSOR) {
			return;
		}
		
		if(light == null) {
			light = new HueLightContainer.Builder()
					.setCustomName("Finlay's Room")
					.setSystemId(4)
					.build();
			logger.info("Created light");
		}
		
		if(sensor == null) {
			sensor = new HueSensor(light);
			logger.info("Created sensor");
		}
		
		logger.info("Handling message");
		
		JSONOutgoingMessage message = null;
		
		if(event.getType() == RequestType.BEGIN_HUE_SENSOR) {
			boolean started = sensor.start();
			
			if(started) {
				message = new JSONOutgoingMessage.Builder()
						.setStatusCode(CodeMessageLookup.SUCCESS.getCode())
						.build();
			} else {
				message = new JSONOutgoingMessage.Builder()
						.setStatusCode(CodeMessageLookup.SERVER_ERROR.getCode())
						.setStatusMessage("Already started or light is unreachable")
						.build();
			}
		} else if (event.getType() == RequestType.STOP_HUE_SENSOR) {
			sensor.stop();
			message = new JSONOutgoingMessage.Builder()
					.setStatusCode(CodeMessageLookup.SUCCESS.getCode())
					.build();
		} else {
			return;
		}
		
		event.getConnection().send(message.toJson());
	}
	
}
