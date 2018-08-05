package lib.finlay.core.io;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * Stores the details about a configuration file.<br>
 * They must be constructed using the {@link ConfigurationDetails.Builder} first.
 * The details can be used to create a {@link ConfigurationFile}.
 * @author Finlay
 * @version 1.0
 * @since 1.0
 */
public final class ConfigurationDetails {

	private final File file;
	private final char separator;
	private final TreeMap<String, String> defaultPairings;
	
	private ConfigurationDetails(Builder builder) {
		this.file = builder.file;
		this.separator = builder.separator;
		this.defaultPairings = builder.defaultPairings;
	}

	public TreeMap<String, String> getDefaultPairings() {
		return defaultPairings;
	}
	
	public File getFile() {
		return file;
	}
	
	public char getSeparator() {
		return separator;
	}
	
	/**
	 * Used to construct the details for a {@link ConfigurationFile}
	 * @author Finlay
	 * @version 1.0
	 * @since 1.0
	 */
	public static class Builder {
		
		private File file;
		private char separator;
		private TreeMap<String, String> defaultPairings;
		
		{
			file = null;
			separator = 0;
			defaultPairings = new TreeMap<>();
		}
		
		/**
		 * Sets the file that will be used on disk to store the configuration.
		 * @param file The file to use.
		 * @return An updated builder object.
		 */
		public Builder setFile(File file) {
			this.file = file;
			return this;
		}
		
		/**
		 * Sets the separator that will split the key from the value.
		 * @param separator The separator to use.
		 * @return An updated builder object.
		 */
		public Builder setSeparator(char separator) {
			this.separator = separator;
			return this;
		}
		
		/**
		 * Sets the default pairings.<br>
		 * <strong>Note:</strong> Overwrites existing pairings.
		 * @param defaultPairings The new default pairings.
		 * @return An updated builder object.
		 */
		public Builder setDefaultPairings(TreeMap<String, String> defaultPairings) {
			this.defaultPairings = defaultPairings;
			return this;
		}
		
		/**
		 * Appends a pairing to the default pairings.
		 * @param key The key of the new pairing.
		 * @param value The value fo the new pairing.
		 * @return An updated builder object.
		 */
		public Builder appendPairing(String key, String value) {
			this.defaultPairings.put(key, value);
			return this;
		}
		
		/**
		 * Appends multiple pairings to the default pairings.
		 * @param pairings The pairings to append.
		 * @return An updated builder object.
		 */
		public Builder appendPairings(Map<String, String> pairings) {
			pairings.forEach((key, value) -> {
				defaultPairings.put(key, value);
			});
			return this;
		}
		
		/**
		 * Builds the {@link ConfigurationDetails} with the specified settings.
		 * @return A final, immutable detail object.
		 * @throws NullPointerException Thrown if the file or the separator are not set.
		 */
		public ConfigurationDetails build() {
			if(file == null) {
				throw new NullPointerException("File has not been set.");
			}
			
			if(separator == 0) {
				throw new NullPointerException("Separator has not been set.");
			}
			
			return new ConfigurationDetails(this);
		}
		
	}
	
}
