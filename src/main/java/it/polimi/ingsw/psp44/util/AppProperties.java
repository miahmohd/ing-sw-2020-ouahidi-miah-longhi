package it.polimi.ingsw.psp44.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class AppProperties {
    private Properties prop;
    private static AppProperties instance;

    private AppProperties()  {
        this.prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AppProperties getInstance(){
        if(instance == null){
            instance = new AppProperties();
        }
        return instance;
    }

    public String getErrorMessage(String code){
        return this.prop.getProperty(code);
    }
}
