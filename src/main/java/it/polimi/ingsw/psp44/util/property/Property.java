package it.polimi.ingsw.psp44.util.property;

import it.polimi.ingsw.psp44.util.property.AppProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class intended to be extended.
 */
public class Property {
    private final Properties prop;

    /**
     * Creates a property from a specified path
     *
     * @param path to property
     * @throws IOException if the path point to an inexistent property file
     */
    public Property(String path) {
        this.prop = new Properties();

        InputStream in = AppProperties.class.getResourceAsStream(path);
        if (in != null) {
            try {
                prop.load(in);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public Property(String[] paths) {
        this.prop = new Properties();

        for (String path : paths) {
            InputStream in = AppProperties.class.getResourceAsStream(path);
            if (in != null) {
                try {
                    prop.load(in);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

    }

    /**
     * Retrieves a property
     *
     * @param code
     * @return the specified property if the code refers to an existing property
     * otherwise returns null;
     */
    public String getProperty(String code) {
        return this.prop.getProperty(code);
    }

}