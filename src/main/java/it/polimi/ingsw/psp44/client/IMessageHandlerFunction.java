package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

import java.util.function.Consumer;

public interface IMessageHandlerFunction extends Consumer<Message> {
}
