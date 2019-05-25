package com.finlay.rc.console;

import java.util.Scanner;

import com.finlay.rc.connection.MessageProcessor;
import com.finlay.rc.connection.incoming.JSONIncomingMessage;
import com.finlay.rc.handlers.RequestType;

public class ConsoleRunnable implements Runnable {

	@Override
	public void run() {
		ConsoleWebSocket socket = new ConsoleWebSocket();
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("Waiting...");
			String input = scanner.nextLine().trim().toLowerCase();
			
			if(input.equals("measure")) {
				MessageProcessor.process(socket, new JSONIncomingMessage.Builder()
						.setType(RequestType.SENSOR_MEASURE_DISTANCE).build().toJSON());
			}
			
			if(input.equals("shutdown")) {
				MessageProcessor.process(socket, new JSONIncomingMessage.Builder()
						.setType(RequestType.SHUTDOWN).build().toJSON());
				break;
			}
			
			if(input.equals("acc")) {
				MessageProcessor.process(socket, new JSONIncomingMessage.Builder()
						.setType(RequestType.GET_CURRENT_ACC_READING).build().toJSON());
			}
		}
		
		scanner.close();
		System.out.println("Shutdown console thread");
	}
	
}
