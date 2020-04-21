package it.polimi.ingsw.psp44.network.message;

import it.polimi.ingsw.psp44.network.VirtualView;

import java.util.function.BiConsumer;

/**
 * Represents a functions that process a Message sent by the VirtualView.
 */
public interface MessageHandlerFunction extends BiConsumer<VirtualView, Message> {
}
