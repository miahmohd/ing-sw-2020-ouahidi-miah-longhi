package it.polimi.ingsw.psp44.network;

import java.io.IOException;

/**
 * Interface for the communication between the user (of the class) and a remote host.
 */
public interface IConnection<T> {


    /**
     * Reads form the chosen connection
     *
     * @return the message just read
     */
    T readLine() throws IOException;


    /**
     * Writes the message into the chosen connection
     */
    void writeLine(T message);

    /**
     * Closes the connection
     */
    void close();
}