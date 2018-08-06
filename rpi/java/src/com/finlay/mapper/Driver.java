package com.finlay.mapper;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.impl.SimpleLogger;

public class Driver {
	
	public static void main(String[] args) {
		Options options = new Options();
		
		Option noHardware = new Option("h", "no-hardware", false, "Enable if the server is not running with the necessary hardware");
		options.addOption(noHardware);
		
		Option logToFile = new Option("l", "log-to-file", true, "Enable to log to external file");
		options.addOption(logToFile);
		
		Option debug = new Option("d", "debug", false, "Enable to receive debug log messages");
		options.addOption(debug);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter helpFormatter = new HelpFormatter();
		CommandLine cmd;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			helpFormatter.printHelp("mapper-server", options);
			System.exit(1);
			return;
		}
		
		if(cmd.hasOption("log-to-file")) {
			String logFile = cmd.getOptionValue("log-to-file");
			System.setProperty(SimpleLogger.LOG_FILE_KEY, logFile);
			System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
			System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "yyyy-MM-dd HH:mm:ss:SSS Z");
		} else {
			System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");
		}
		
		if(cmd.hasOption("debug")) {
			System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "debug");
		} else {
			System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "info");
		}
		
		boolean hardware = !cmd.hasOption("no-hardware");
		
		Robot.instance = new Robot(hardware);
		Robot.instance.start();
	}
	
}
