package by.bsuir.matys_rozin_zarudny.configuration;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used for reading properties from files in the Java classspath.
 */
public class ClassPathConfigLoaderStrategy implements ConfigLoaderStrategy {

	private static final Logger LOGGER = Logger.getLogger(ClassPathConfigLoaderStrategy.class.getName());

	/**
	 * Return properties from file on the class path
	 */
	@Override
	public Properties getConfig(String file) {

		Properties properties = new Properties();

		try {
			try (Reader reader = new InputStreamReader(
					Thread.currentThread().getContextClassLoader().getResourceAsStream(file))) {
				properties.load(reader);
				return properties;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, null, e);
			return null;
		}
	}
}
