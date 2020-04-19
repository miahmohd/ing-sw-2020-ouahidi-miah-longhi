package it.polimi.ingsw.psp44.network.message;

import it.polimi.ingsw.psp44.network.VirtualView;
import it.polimi.ingsw.psp44.util.R;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible to route the message to the appropriate MessageHandlerFunction
 */
public class MessageRouter {

    private final Map<Message.Code, MessageHandlerFunction> handlers;

    public MessageRouter() {
        this.handlers = new HashMap<>();
    }

    /**
     * Route the message to the appropriate handler capable of processing the message.
     *
     * @param view    the player virtual-view that sent the message.
     * @param message the sent message.
     */
    public void route(VirtualView view, Message message) {
        MessageHandlerFunction handlerFunction = this.handlers.get(message.getCode());
        if (handlerFunction == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.MESSAGE_HANDLER_NOT_FOUND));
        handlerFunction.accept(view, message);
    }

    /**
     * Add a new message handler.
     *
     * @param code    indicates which messages the handler can process.
     * @param handler the function processing the message.
     */
    public void addRoute(Message.Code code, MessageHandlerFunction handler) {
        if (!this.handlers.containsKey(code)) {
            this.handlers.put(code, handler);
        }
    }
}
