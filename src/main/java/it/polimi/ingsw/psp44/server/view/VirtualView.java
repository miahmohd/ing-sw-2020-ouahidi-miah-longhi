package it.polimi.ingsw.psp44.server.view;

import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.Virtual;
import it.polimi.ingsw.psp44.network.message.IMessageHandlerFunction;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.IObservable;
import it.polimi.ingsw.psp44.util.IObserver;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.R;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Class representing the view of a single player.
 */
public class VirtualView extends Virtual implements Runnable, IObserver<Message> {


    private final Map<Message.Code, IMessageHandlerFunction> handlers;
    private Message lastSend;

    public VirtualView(IConnection<String> connection) {
        super(connection);
        this.handlers = Collections.synchronizedMap(new EnumMap<>(Message.Code.class));
    }

    @Override
    public void run() {
        String rawJson;
        Message message;
        this.addMessageHandler(Message.Code.PING, (v, m) -> System.out.println("Ping received from " + v));

        try {

            /* On Linux:
             * If the client disconnect (ie ctrl+c), .readLine() returns null.
             * If there is a problem on the network (ie package loss) .readLine() throws SocketTimeoutException.
             * If the server closes the socket .readLine(), throws SocketException
             * see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/Socket.html#close()
             */
            while ((rawJson = this.connection.readLine()) != null) {
                message = JsonConvert.getInstance().fromJson(rawJson, Message.class);
                this.routeMessage(this, message);
            }
            System.out.println("readline = null");
            this.routeMessage(this, new Message(Message.Code.CLIENT_DISCONNECTED));

        } catch (SocketTimeoutException exception) {
            System.out.println("SocketTimeoutException for " + this);
            this.routeMessage(this, new Message(Message.Code.CLIENT_DISCONNECTED));
        } catch (SocketException ignored) {
            System.out.println("SocketException " + ignored.getMessage() + this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add an handler function for message with the specified code.
     *
     * @param code    the code that identify the messages that the handler can handle.
     * @param handler the function that handles the message.
     */
    public void addMessageHandler(Message.Code code, IMessageHandlerFunction handler) {
        handlers.put(code, handler);
    }


    @Override
    public void sendMessage(Message message) {
        super.sendMessage(message);
        lastSend = message;
    }


    @Override
    public void update(IObservable<Message> observable, Message arg) {
        super.sendMessage(arg);
    }


    private void routeMessage(VirtualView view, Message message) {
//        if (message.getCode() == Message.Code.PING)
//            return;

        IMessageHandlerFunction handlerFunction = this.handlers.get(message.getCode());
        if (handlerFunction == null)
            throw new IllegalArgumentException(String.format(R.getAppProperties().get(ErrorCodes.MESSAGE_HANDLER_NOT_FOUND), message.getCode()));
        try {
            handlerFunction.accept(view, message);
        } catch (Exception ex) {
            this.lastSend.addHeader(MessageHeader.ERROR, ex.getClass().toString());
            this.lastSend.addHeader(MessageHeader.ERROR_DESCRIPTION, ex.getMessage());
            this.sendMessage(this.lastSend);
        }
    }

}






























