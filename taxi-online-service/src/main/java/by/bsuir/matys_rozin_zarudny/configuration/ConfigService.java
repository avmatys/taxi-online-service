package by.bsuir.matys_rozin_zarudny.configuration;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a configuration service.
 *
 */
public class ConfigService {

    private static final Logger LOGGER = Logger.getLogger(
            ConfigService.class.getName());

    /**
     * Represents configuration loaders
     */
    private static final ConfigLoaderStrategy[] LOADERS = new ConfigLoaderStrategy[]{
        new ClassPathConfigLoaderStrategy()
    };

    private ConfigService() {
    }

    /**
     * Load specified configuration.
     *
     * @param conf file to load configuration from.
     * @return properties loaded from the specified configuration file else
     * null.
     */
    public static Properties getConfig(String conf) {
        try {
            for (ConfigLoaderStrategy configLoader : LOADERS) {
                Properties properties = configLoader.getConfig(conf);

                if (properties != null) {
                    return properties;
                }
            }
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }

        return null;
    }

    /**
     * Tokenise a property token with a provided value. 
     * api.endpoint=http://127.0.0.1/api/{id} becomes http://127.0.0.1/api/10
     *
     * @param property property value to parse.
     * @param tokens tokens to value map.
     * @return property with values replaced by tokens.
     */
    public static String parseProperty(String property, Map<String, String> tokens) {
        
            if (property == null || tokens == null || tokens.isEmpty()) {
            throw new IllegalArgumentException();
        }

   
        return new URLTokenParser(tokens).tokenize(property);
    }
}
