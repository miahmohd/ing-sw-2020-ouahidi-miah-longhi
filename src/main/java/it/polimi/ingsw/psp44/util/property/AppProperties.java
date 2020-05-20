package it.polimi.ingsw.psp44.util.property;

public class AppProperties extends Property {
    private static final String[] propertyPaths = {"/errors.properties", "/model.properties", "/config.properties"};
    private static AppProperties instance;

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
