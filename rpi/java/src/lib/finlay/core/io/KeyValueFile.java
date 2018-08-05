package lib.finlay.core.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Basis for a key-value pairing file.
 * @author Finlay
 * @version 1.0
 * @since 1.0
 */
public class KeyValueFile {

	private final SerialFileHandler fileHandler;
	private final char separator;
	private final TreeMap<String, String> cachedPairings;
	
	/**
	 * Creates a new file handling instance.
	 * @param configFile The file to store the data in.
	 * @param separator This is used to separated the key from the value.
	 * @throws IOException Thrown if the file cannot be created.
	 * @see {@link KeyValueFile}
	 */
	public KeyValueFile(File configFile, char separator) throws IOException {
		this.fileHandler = new SerialFileHandler(configFile);
		this.cachedPairings = new TreeMap<>();
		this.separator = separator;
	}
	
	/**
	 * Creates a new file handling instance.
	 * @param directory The directory that will contain the file.
	 * @param fileName The name of the file.
	 * @param separator This is used to separated the key from the value.
	 * @throws IOException Thrown if the file cannot be created.
	 * @see {@link KeyValueFile}
	 */
	public KeyValueFile(String directory, String fileName, char separator) throws IOException {
		this(new File(directory, fileName), separator);
	}
	
	/**
	 * Retrieves the pairings from the file (dependent on the parameter value) and loads them into a {@link TreeMap}
	 * @param updateData This decides if the data should first be updated from the disk.
	 * @return A {@link TreeMap} of key-value pairings.
	 * @throws IOException Thrown if the file cannot be read.
	 * @see {@link TreeMap}, {@link #getPairings()}
	 */
	public TreeMap<String, String> getPairings(boolean updateData) throws IOException {
		if(!updateData) {
			if(cachedPairings != null && !cachedPairings.isEmpty()) {
				return cachedPairings;
			}
		}
		
		cachedPairings.clear();
		ArrayList<String> latestData = fileHandler.getContents();
		
		for(String line : latestData) {
			String[] splitLine = line.split(String.valueOf(separator));
			
			if(splitLine.length < 2) {
				continue;
			}
			
			String key = splitLine[0];
			String value = "";
			
			for(int i = 1; i < splitLine.length; i++) {
				value += splitLine[i];
			}
			
			cachedPairings.put(key, value);
		}
		
		return cachedPairings;
	}
	
	/**
	 * Retrieves the pairings from the file and loads them into a {@link TreeMap}
	 * @return A {@link TreeMap} of key-value pairings.
	 * @throws IOException Thrown if the file cannot be read.
	 */
	public TreeMap<String, String> getPairings() throws IOException {
		return getPairings(true);
	}
	
	/**
	 * Writes the pairings to the file in a suitable format.<br>
	 * <strong>Note:</strong> All pairings are written, not just the changed ones.
	 * @param configurationValues The values to write.
	 * @throws IOException Thrown if the file cannot be written to.
	 * @see {@link #writePairings()}
	 */
	public void writePairings(Map<String, String> configurationValues) throws IOException {
		ArrayList<String> contents = new ArrayList<>();
		
		configurationValues.forEach((key, value) -> {
			contents.add(key + separator + value);
		});
		
		fileHandler.writeContents(contents, false);
	}
	
	/**
	 * Writes the cached pairings to the file.
	 * @throws IOException Thrown if the file cannot be written to.
	 * @see {@link #writePairings(Map)}
	 */
	public void writePairings() throws IOException {
		writePairings(getCachedPairings());
	}
	
	/**
	 * Adds a pairing to the current pairings.
	 * @param key The key of the pairing.
	 * @param value The value associated with the key.
	 * @param write Decides whether the changes should be written to file.
	 * @throws IOException Thrown if the file cannot be written to.
	 * @see {@link #addPairing(String, String)}
	 */
	public void addPairing(String key, String value, boolean write) throws IOException {
		cachedPairings.put(key, value);
		
		if(write) {
			writePairings();
		}
	}
	
	/**
	 * Adds a pairing to the current pairings and writes it to file.
	 * @param key The key of the pairing.
	 * @param value The value associated with the key.
	 * @throws IOException Thrown if the file cannot be written to.
	 * @see {@link #addPairing(String, String, boolean)}
	 */
	public void addPairing(String key, String value) throws IOException {
		addPairing(key, value, true);
	}
	
	/**
	 * Checks if a key exists.
	 * @param key The key to check the existence of.
	 * @return True if the key exists, false if it does not.
	 * @throws IOException This may be thrown if the pairings could not be retrieved.
	 */
	public boolean doesKeyExist(String key) throws IOException {
		TreeMap<String, String> pairings = getPairings(false);
		return pairings.containsKey(key);
	}
	
	/**
	 * Checks if multiple keys exist.
	 * @param keys The keys to check the existence of.
	 * @return True if all keys exist, false if at least one of the does not.
	 * @throws IOException This may be thrown if the pairings could not be retrieved.
	 */
	public boolean doKeysExist(Collection<String> keys) throws IOException {
		for(String key : keys) {
			if(!doesKeyExist(key)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Retrieves a value based on a key from the cached pairings.
	 * @param key The key that hosts the value.
	 * @return The value based on the key.
	 */
	public String getCachedValue(String key) {
		return cachedPairings.get(key);
	}
	
	/**
	 * Retrieves the full cached pairings.
	 * @return A {@link TreeMap} of pairings.
	 */
	public TreeMap<String, String> getCachedPairings() {
		return cachedPairings;
	}
	
	/**
	 * The value that separates the key from the value.
	 * @return The separator character.
	 */
	public char getSeparator() {
		return separator;
	}
	
	/**
	 * The underlying class that performs the file writing and reading.
	 * @return A {@link SerialFileHandler} object.
	 * @see {@link SerialFileHandler}
	 */
	public SerialFileHandler getFileHandler() {
		return fileHandler;
	}
	
}
