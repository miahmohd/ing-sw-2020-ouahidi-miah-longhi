package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.network.message.Message;

/**
 * Interface intended for networking with something (Virtual entity) through a single method
 */
public interface IVirtual {

    /**
     * Method that sends the specified message
     *
     * @param message is the formatted message
     */
    void sendMessage(Message message);

}