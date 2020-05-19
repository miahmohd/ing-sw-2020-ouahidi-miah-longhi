package it.polimi.ingsw.psp44.server.view;

import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.IVirtual;
import it.polimi.ingsw.psp44.network.message.IMessageHandlerFunction;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.IObservable;
import it.polimi.ingsw.psp44.util.IObserver;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.R;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;

import java.util.EnumMap;
import java.util.Map;

/**
 * Class representing the view of a single player.
 */
public class VirtualView implements Runnable, IVirtual<Message>, IObserver<Message> {

    private final IConnection<String> connection;
    private final Map<Message.Code, IMessageHandlerFunction> handlers;
    private Message lastSend;

    public VirtualView(IConnection<String> connection) {
        this.connection = connection;
        this.handlers = new EnumMap<>(Message.Code.class);

    }

    @Override
    public void run() {
        //TODO da aggiungere:
        // - sistemare in loop
        String rawJson;
        while ((rawJson = this.connection.readLine()) != null) {
            Message message = JsonConvert.getInstance().fromJson(rawJson, Message.class);
            this.routeMessage(this, message);
        }
    }

    /**
     * Add an handler function for message with the specified code.
     *
     * @param code    the code that identify the messages that the handler can handle.
     * @param handler the function that handles the message.
     */
    public void addMessageHandler(Message.Code code, IMessageHandlerFunction handler) {
        if (!this.handlers.containsKey(code))
            handlers.put(code, handler);
    }


    @Override
    public void sendMessage(Message message) {
        String messageString = JsonConvert.getInstance().toJson(message, Message.class);
        connection.writeLine(messageString);
        lastSend=message;

    }


    private void routeMessage(VirtualView view, Message message) {
        IMessageHandlerFunction handlerFunction = this.handlers.get(message.getCode());
        if (handlerFunction == null)
            throw new IllegalArgumentException(R.getAppProperties().get(ErrorCodes.MESSAGE_HANDLER_NOT_FOUND));
        try {
            handlerFunction.accept(view, message);
        }catch (Exception ex){
            lastSend.addHeader(MessageHeader.ERROR,ex.getClass().toString());
            lastSend.addHeader(MessageHeader.ERROR_DESCRIPTION,ex.getMessage());
            sendMessage(lastSend);
        }
    }

    @Override
    public void update(IObservable<Message> observable, Message arg) {
        this.sendMessage(arg);
    }
}






























