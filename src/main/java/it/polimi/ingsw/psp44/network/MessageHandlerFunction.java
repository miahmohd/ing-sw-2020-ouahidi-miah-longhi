package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.util.network.Message;

import java.util.function.BiFunction;

/**
 * Represents a functions that process a Message sent by the VirtualView.
 */
public interface MessageHandlerFunction extends BiFunction<VirtualView, Message, Boolean> {
}
