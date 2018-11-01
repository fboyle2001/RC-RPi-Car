package com.finlay.rc.connection;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.rc.connection.outgoing.JSONOutgoingMessage;

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
			JSONOutgoingMessage message = new JSONOutgoingMessage.Builder().setStatusCode(503).build();
			conn.send(message.toJson());
			return;
		}
		
		//could add some username and password auth but probably unnecessary
		
		String authKey = UUID.randomUUID().toString();
		MessageProcessor.setAuthKey(authKey);
		
		JSONOutgoingMessage message = new JSONOutgoingMessage.Builder().setStatusCode(201).setKeyValue("authKey", authKey).build();
		conn.send(message.toJson());
		
		logger.info("Connection accepted; auth key sent");
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
		MessageProcessor.process(conn, message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		logger.error("Error encountered in server");
		logger.error("{}", ex);
	}

	@Override
	public void onStart() {
		logger.info("Server has started");
	}

}
