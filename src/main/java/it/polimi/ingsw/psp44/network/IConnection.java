package it.polimi.ingsw.psp44.network;

/**
 * Interface for the communication between the user (of the class) and a remote host.
 */
public interface IConnection<T> {


    /**
     * Reads form the chosen connection
     *
     * @return the message just read
     */
    T readLine();


    /**
     * Writes the message into the chosen connection
     */
    void writeLine(T message);

}