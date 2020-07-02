package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

import java.util.function.Consumer;

/**
 * Represents a functions that process a Message sent by the VirtualServer.
 */
public interface IMessageHandlerFunction extends Consumer<Message> {
}
