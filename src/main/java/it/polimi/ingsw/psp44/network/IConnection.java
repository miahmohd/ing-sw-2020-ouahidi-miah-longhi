package it.polimi.ingsw.psp44.network;

import java.io.IOException;

/**
 * Interface for the communication between the user (of the class) and a remote host.
 */
public interface IConnection {


    /**
     * Reads form the chosen connection
     *
     * @return the message just read
     */
    String readLine() throws IOException;


    /**
     * Writes the message into the chosen connection
     */
    void writeLine(String message);

    /**
     * Closes the connection
     */
    void close();
}