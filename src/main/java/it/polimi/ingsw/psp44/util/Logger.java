package it.polimi.ingsw.psp44.util;

public class Logger {

    private static Logger instance;
    private boolean debug;

    private Logger(){
        this.debug = true;
    }

    public Logger getInstance(){
        if (instance == null)
            instance = new Logger();
        return instance;
    }

    public void println(String logEntry){
        System.out.println("Log: " + logEntry);
    }
}
