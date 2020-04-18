package it.polimi.ingsw.psp44.util;

public class CLIProperties extends Property{
    private static CLIProperties instance;
    private static String propertyPath = "/cli.properties";

    private CLIProperties(String path) {
        super(path);
    }


    public static CLIProperties getInstance() {
        if (instance == null) {
            instance = new CLIProperties(propertyPath);
        }
        return instance;
    }
    

}