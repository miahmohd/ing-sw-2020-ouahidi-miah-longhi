package it.polimi.ingsw.psp44.network.message;

import it.polimi.ingsw.psp44.network.VirtualView;
import it.polimi.ingsw.psp44.network.message.Message;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Represents a functions that process a Message sent by the VirtualView.
 */
public interface MessageHandlerFunction extends BiConsumer<VirtualView, Message> {
}
