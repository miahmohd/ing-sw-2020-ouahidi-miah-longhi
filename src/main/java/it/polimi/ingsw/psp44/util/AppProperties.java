package it.polimi.ingsw.psp44.util;

public class AppProperties extends Property {
    private static AppProperties instance;
    private static final String[] propertyPaths = {"/errors.properties", "/codes.properties"};

    private AppProperties(String[] path) {
        super(path);
    }

    public static AppProperties getInstance() {
        if (instance == null) {
            instance = new AppProperties(propertyPaths);
        }
        return instance;
    }
}
