package it.polimi.ingsw.psp44.network;

/**
 * This class establishes a connection between the user (of the class) and a remote host.
 * It's an abstract class purely for testing
 */
public abstract class Connection<T> {


    /**
     * Reads form the chosen connection
     * @return the message just read
     */
    public abstract T readLine();
    

    /**
     * Writes the message into the chosen connection
     */
    public abstract void writeLine(T message);

}