package it.polimi.ingsw.psp44.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {
    private static AppProperties instance;
    private Properties prop;

    private AppProperties() {
        this.prop = new Properties();

        InputStream in = AppProperties.class.getResourceAsStream("/app.properties");
        if (in != null) {
            try {
                prop.load(in);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    public static AppProperties getInstance() {
        if (instance == null) {
            instance = new AppProperties();
        }
        return instance;
    }

    public String getMessage(String code) {
        return this.prop.getProperty(code);
    }
}
