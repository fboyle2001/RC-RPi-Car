package com.finlay.rc.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.rc.components.mpu.AccelerometerReading;
import com.finlay.rc.components.mpu.MPU6050;
import com.finlay.rc.components.mpu.MPURepeatReader;
import com.finlay.rc.connection.MessageReceivedEvent;
import com.finlay.rc.connection.outgoing.JSONOutgoingMessage;
import com.finlay.rc.connection.outgoing.JSONOutgoingMessage.Builder;
import com.finlay.rc.connection.outgoing.lookup.CodeMessageLookup;

import lib.finlay.core.events.EventListener;
import lib.finlay.core.events.EventMethod;

@EventListener
public class AccelerometerHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AccelerometerHandler.class);

	@EventMethod
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getType() != RequestType.GET_CURRENT_ACC_READING && event.getType() != RequestType.START_REPEAT_ACC_READER
				&& event.getType() != RequestType.STOP_REPEAT_ACC_READER) {
			return;
		}
		
		logger.info("Handling message");
		
		Builder message = new JSONOutgoingMessage.Builder().setStatusCode(200);
		
		switch(event.getType()) {
		case GET_CURRENT_ACC_READING:
			AccelerometerReading reading = MPU6050.getInstance().getSensorData();
			message.addContent(reading.getAsMap());
			break;
		case START_REPEAT_ACC_READER:
			boolean started = MPURepeatReader.getInstance().start(r -> {
				Builder response = new JSONOutgoingMessage.Builder()
						.setStatusCode(200)
						.setRequestType(RequestType.START_REPEAT_ACC_READER)
						.setKeyValue("reading", r.getAsMap());
				event.getConnection().send(response.build().toJson());
			});
			
			if(!started) {
				message.setStatusCode(CodeMessageLookup.SERVER_ERROR);
			}
			
			break;
		case STOP_REPEAT_ACC_READER:
			MPURepeatReader.getInstance().stop();
			break;
		default:
			return;
		}
		
		event.getConnection().send(message.build().toJson());
	}
	
}
