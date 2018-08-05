package com.finlay.mapper.connection;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.connection.message.JSONMessage;

import lib.finlay.core.events.EventManager;

public class SocketServer extends WebSocketServer {
	
	private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
	
	private boolean connected;
	
	public SocketServer(int port) {
		super(new InetSocketAddress(port));
		this.connected = false;
	}
	
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		logger.info("New connection received");
		
		if(connected) {
			logger.info("Rejected as already connected to client");
			JSONMessage message = new JSONMessage.Builder().setStatusCode(503).build();
			System.out.println(message.toJson());
			conn.send(message.toJson());
			return;
		}
		
		logger.info("Connection accepted");
		this.connected = true;
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		logger.info("Connection terminated. Code {}, reason {}", code, reason);
		this.connected = false;
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		if(message == "" || message == null) {
			logger.warn("Received empty message");
			return;
		}

		logger.info("Received {}", message);
		MessageProcessor.process(message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		logger.error("Error encountered");
		logger.error(ex.getMessage());
	}

	@Override
	public void onStart() {
		logger.info("Server has started");
		EventManager.start();
	}
	
	public static void main(String[] args) {
		SocketServer server = new SocketServer(5612);
		server.start();
	}

}
