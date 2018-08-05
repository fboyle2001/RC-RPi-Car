package lib.finlay.core.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Handles configuration files for programs.
 * @author Finlay
 * @version 1.0
 * @since 1.0
 */
public final class ConfigurationFile {

	/*
	 * Static methods to retrieve configuration files to remove need for passing as arguments in construction.
	 */
	
	private static final HashMap<String, ConfigurationFile> storedFiles;
	
	/**
	 * Retrieves a configuration file if it exists. (Must first have been created)
	 * @param owner The class that owns the configuration file.
	 * @return The ConfigurationFile object associated with the specified class.
	 * @throws IllegalArgumentException Thrown if the configuration file does not exist.
	 * @see {@link #createConfigurationOf(Class, ConfigurationDetails)}
	 */
	public static ConfigurationFile getConfigurationOf(Class<?> owner) {
		if(!storedFiles.containsKey(owner.getName())) {
			throw new IllegalArgumentException(owner.getName() + " configuration file not found.");
		}
		
		return storedFiles.get(owner.getName());
	}
	
	/**
	 * Creates a configuration file for a specific class.
	 * @param owner The owner of the class which allows the file to be retrieved.
	 * @param details The details of the configuration file.
	 * @return A ConfigurationFile object that can be used.
	 * @throws IOException Thrown if the file cannot be created.
	 * @see {@link #getConfigurationOf(Class)}, {@link ConfigurationDetails}
	 */
	public static ConfigurationFile createConfigurationOf(Class<?> owner, ConfigurationDetails details) throws IOException {
		if(!storedFiles.containsKey(owner.getName())) {
			storedFiles.put(owner.getName(), new ConfigurationFile(details));
		}
		
		return getConfigurationOf(owner);
	}
	
	static {
		storedFiles = new HashMap<>();
	}
	
	/*
	 * Actual configuration file class
	 */
	
	private final KeyValueFile configFile;
	private final TreeMap<String, String> defaultPairings;
	
	private ConfigurationFile(File file, char separator, TreeMap<String, String> defaultPairings) throws IOException {
		if(!file.exists()) {
			file.createNewFile();
		}
		
		this.configFile = new KeyValueFile(file, separator);
		this.defaultPairings = defaultPairings;
		
		writeDefaults(false);
	}
	
	public ConfigurationFile(ConfigurationDetails details) throws IOException {
		this(details.getFile(), details.getSeparator(), details.getDefaultPairings());
	}
	
	/**
	 * Writes the default pairings to file.<br>
	 * <br>
	 * <u>Pairings are written if:</u>
	 * <ul>
	 * 	<li>The overwrite parameter is true.</li>
	 * 	<li>The default pairings are not empty.</li>
	 * 	<li>The configuration file (on disk) is empty.</li>
	 * </ul>
	 * @param overwrite Decides whether or not the file should be forcefully overwritten.
	 * @return True if the defaults are written, false otherwise.
	 * @throws IOException Thrown if the settings cannot be written.
	 */
	public boolean writeDefaults(boolean overwrite) throws IOException {
		if(!configFile.getPairings().isEmpty() && !overwrite) {
			return false;
		}
		
		if(defaultPairings.isEmpty()) {
			return false;
		}
		
		configFile.writePairings(defaultPairings);
		return true;
	}
	
	/**
	 * Changes, or adds, a pairing to the config and writes it to file.
	 * @param key The key of the pairing.
	 * @param value The value of the pairing.
	 * @throws IOException Thrown if the pairing cannot be written.
	 */
	public void updatePairing(String key, String value) throws IOException {
		configFile.addPairing(key, value);
	}

	/**
	 * Fetches the pairings from the disk and loads them into a map.
	 * @throws IOException Thrown if the pairings cannot be read.
	 */
	public void reloadPairings() throws IOException {
		configFile.getPairings(true);
	}
	
	/*
	 * Get values
	 */
	
	/**
	 * Retrieves a value from the cached values.<br>
	 * <strong>This does not update the values from the file.</strong><br>
	 * Use {@link #reloadPairings()} first instead.
	 * @param key The key of the pair.
	 * @return A string consisting of the value (or null if no value is found).
	 */
	public String get(String key) {
		return configFile.getCachedValue(key);
	}
	
	/**
	 * Gets a boolean value based on the key.
	 * @param key The key in key-value pair.
	 * @return A boolean of the string value based on the key.
	 * @see {@link #get(String)}
	 */
	/*
	 * This method could throw an unexpected exception because parseBoolean cannot except null yet get(key) could return null.
	 */
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(get(key));
	}
	
	/**
	 * Gets an integer based on the key.
	 * @param key The key in key-value pair.
	 * @return An integer representation of the value.
	 * @throws NumberFormatException Thrown if the string cannot be converted to an integer.
	 * @see {@link #get(String)}
	 */
	public int getInteger(String key) throws NumberFormatException {
		return Integer.parseInt(get(key));
	}
	
	/**
	 * Gets a short based on the key.
	 * @param key The key in key-value pair.
	 * @return A short representation of the value.
	 * @throws NumberFormatException Thrown if the string cannot be converted to a short.
	 * @see {@link #get(String)}
	 */
	public short getShort(String key) throws NumberFormatException {
		return Short.parseShort(get(key));
	}
	
	/**
	 * Gets a double based on the key.
	 * @param key The key in key-value pair.
	 * @return A double representation of the value.
	 * @throws NumberFormatException Thrown if the string cannot be converted to a double.
	 * @see {@link #get(String)}
	 */
	public double getDouble(String key) throws NumberFormatException {
		return Double.parseDouble(get(key));
	}
	
	/**
	 * Gets a float based on the key.
	 * @param key The key in key-value pair.
	 * @return A float representation of the value.
	 * @throws NumberFormatException Thrown if the string cannot be converted to a float.
	 * @see {@link #get(String)}
	 */
	public float getFloat(String key) throws NumberFormatException {
		return Float.parseFloat(get(key));
	}
	
	/**
	 * Gets a byte based on the key.
	 * @param key The key in key-value pair.
	 * @return A byte representation of the value.
	 * @throws NumberFormatException Thrown if the string cannot be converted to a byte.
	 * @see {@link #get(String)}
	 */
	public byte getByte(String key) throws NumberFormatException {
		return Byte.parseByte(get(key));
	}
	
	/*
	 * Simple getX() functions
	 */
	
	public KeyValueFile getRawConfigFile() {
		return configFile;
	}
	
	public TreeMap<String, String> getDefaultPairings() {
		return defaultPairings;
	}
	
}
