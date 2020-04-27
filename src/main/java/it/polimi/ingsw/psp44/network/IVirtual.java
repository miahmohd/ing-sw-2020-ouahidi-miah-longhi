package it.polimi.ingsw.psp44.network;

/**
 * Interface intended for networking with something (Virtual entity) through a single method
 */
public interface IVirtual<T> {

    /**
     * Method that sends the specified message
     *
     * @param message is the formatted message
     */
    void sendMessage(T message);

}