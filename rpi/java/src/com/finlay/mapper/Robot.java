package com.finlay.mapper;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import com.finlay.mapper.components.AutoMove;
import com.finlay.mapper.components.PiconZero;
import com.finlay.mapper.components.PiconZeroOutputType;
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

public class Robot {

	private static final Logger logger = LoggerFactory.getLogger(Robot.class);
	
	protected static Robot instance;
	
	public static Robot getInstance() {
		return instance;
	}
	
	public static boolean isHardwareConnected() {
		return instance.hardware;
	}
	
	private boolean hardware;
	private boolean started;
	private ConfigurationFile config;
	private SocketServer server;
	
	public Robot(boolean hardware) {
		this.started = false;
		this.hardware = hardware;
	}
	
	public void start() {
		logger.info("Logger started, lowest log level is {}", System.getProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY));
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
		logger.info("Added shutdown hook");

		if(hardware) {
			GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
			logger.info("Set pin provider to BCM");
		} else {
			logger.info("--no-hardware option enabled, pin provider not set");
		}
		
		PiconZero.getInstance().setOutputType(0, PiconZeroOutputType.DIGITAL);
		logger.info("Provisioned LED pin");
		
		AutoMove.getInstance();
		logger.info("Woken AutoMove");
		
		this.server = new SocketServer(port);
		server.start();
		this.started = true;
		
		logger.info("Completed start up");
	}
	
	public boolean isStarted() {
		return started;
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
			this.config = ConfigurationFile.createConfigurationOf(getClass(), configDetails);
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
		
		logger.info("Stopping AutoMove");
		AutoMove.getInstance().stop();
		
		logger.info("Stopping PiconZero");
		PiconZero.getInstance().stopMotion();
		
		logger.info("Shutdown complete");
		
	}
}
