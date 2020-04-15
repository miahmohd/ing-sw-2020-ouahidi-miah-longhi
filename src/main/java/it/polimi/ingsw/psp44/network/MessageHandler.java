package it.polimi.ingsw.psp44.network;


import it.polimi.ingsw.psp44.util.network.Message;


/**
 * Class implementing the chain of responsibility pattern in order to handle a Message.
 * see https://refactoring.guru/design-patterns/chain-of-responsibility
 */
public class MessageHandler {

    private MessageHandler next;
    private MessageHandlerFunction handler;

    public MessageHandler(MessageHandlerFunction handler) {
        this.next = null;
        this.handler = handler;
    }

    public void handle(VirtualView view, Message message) {
        if (!handler.apply(view, message) && this.next != null)
            this.next.handle(view, message);
    }

    public void setNext(MessageHandler handler) {
        if (this.next != null)
            this.next.setNext(handler);
        this.next = handler;
    }
}
