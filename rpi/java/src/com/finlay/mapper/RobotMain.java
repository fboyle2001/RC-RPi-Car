package com.finlay.mapper;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlay.mapper.connection.SocketServer;
import com.finlay.mapper.connection.outgoing.JSONOutgoingMessage;
import com.finlay.mapper.connection.outgoing.lookup.CodeMessageLookup;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.RaspiPinNumberingScheme;

import lib.finlay.core.collections.TreeMapBuilder;
import lib.finlay.core.events.EventManager;
import lib.finlay.core.io.ConfigurationDetails;
import lib.finlay.core.io.ConfigurationFile;

public class RobotMain {

	private static final RobotMain instance;
	private static final Logger logger;
	
	static {
		//System.setProperty(SimpleLogger.LOG_FILE_KEY, "log_file.log");
		//System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
		//System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "yyyy-MM-dd HH:mm:ss:SSS Z");
		logger = LoggerFactory.getLogger(RobotMain.class);
		instance = new RobotMain();
	}
	
	public static RobotMain getInstance() {
		return instance;
	}
	
	private boolean started;
	private ConfigurationFile config;
	private SocketServer server;
	
	public RobotMain() {
		this.started = false;
	}
	
	public void start() {
		loadConfig();
		
		int port;
		
		try {
			port = config.getInteger("port");
		} catch (NumberFormatException e) {
			logger.warn("Invalid port number given, defaulting to 5612");
			port = 5612;
		}
		
		logger.info("Loaded configuration file");
		
		EventManager.start();
		logger.info("Event Manager started");

		GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
		logger.info("Set pin provider to BCM");
		
		this.server = new SocketServer(port);
		server.start();
		this.started = true;
		
		logger.info("Completed start up");
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public ConfigurationFile getConfig() {
		return config;
	}
	
	private void loadConfig() {
		TreeMap<String, String> defaultPairings = new TreeMapBuilder<String, String>()
				.put("port", "5612")
				.put("retries", "10")
				.put("speedOfSound", "343")
				.build();
		
		ConfigurationDetails configDetails = new ConfigurationDetails.Builder()
				.setDefaultPairings(defaultPairings)
				.setSeparator('=')
				.setFile(new File("config.properties"))
				.build();
		
		try {
			this.config = new ConfigurationFile(configDetails);
		} catch (IOException e) {
			logger.error("Unable to create config");
			logger.error("{}", e);
		}
	}
	
	public void stop() {
		logger.info("Server shutting down");
		
		JSONOutgoingMessage message = new JSONOutgoingMessage.Builder()
				.setStatusCode(CodeMessageLookup.GONE)
				.build();
		server.broadcast(message.toJson());
		
		try {
			server.stop();
		} catch (IOException | InterruptedException e) {
			logger.error("{}", e);
		}
		
		logger.info("Shutdown complete");
		
	}

	public static void main(String[] args) {
		getInstance().start();
	}
}
