package it.polimi.ingsw.psp44.util;

public class AppProperties extends Property {
    private static AppProperties instance;
    private static String propertyPath = "/app.properties";

    private AppProperties(String path) {
        super(path);
    }

    public static AppProperties getInstance() {
        if (instance == null) {
            instance = new AppProperties(propertyPath);
        }
        return instance;
    }
}
