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


    private int lobbyID;
    private Message lastSend;
    private boolean errorFlag = true;

    public VirtualView(IConnection<String> connection) {
        super(connection);
        this.lobbyID = -1;
        this.handlers = Collections.synchronizedMap(new EnumMap<>(Message.Code.class));
    }

    public int getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(int lobbyID) {
        this.lobbyID = lobbyID;
    }

    public void setClosable(){
        this.errorFlag = false;
    }


    @Override
    public void run() {
        String rawJson;
        Message message;

        try {

            /* On Linux:
             * If the client disconnect (ie ctrl+c), .readLine() returns null.
             * If there is a problem on the network (ie package loss) .readLine() throws SocketTimeoutException.
             * If another thread closes the socket, .readLine() throws SocketException
             * see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/Socket.html#close()
             *
             * On Windows:
             * If the client disconnect (ie ctrl+c), .readLine() throws SocketException
             * see https://stackoverflow.com/questions/22931811/differences-on-java-sockets-between-windows-and-linux-how-to-handle-them
             */
            while ((rawJson = this.connection.readLine()) != null) {
                message = JsonConvert.getInstance().fromJson(rawJson, Message.class);
                this.routeMessage(this, message);
            }

            if (this.errorFlag) {
                this.routeMessage(this, new Message(Message.Code.CLIENT_DISCONNECTED));
                this.routeMessage(this, new Message(Message.Code.LOBBY_DISCONNECTED));
            }


        } catch (SocketException | SocketTimeoutException ex) {

            if (this.errorFlag) {
                System.out.println(ex.getMessage() + "for " + this);
                this.routeMessage(this, new Message(Message.Code.CLIENT_DISCONNECTED));
                this.routeMessage(this, new Message(Message.Code.LOBBY_DISCONNECTED));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.handlers.clear();

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
    public void close() {
        this.errorFlag = false;
        super.close();
    }

    @Override
    public void update(IObservable<Message> observable, Message arg) {
        super.sendMessage(arg);
    }


    private void routeMessage(VirtualView view, Message message) {
        if (message.getCode() == Message.Code.PING)
            return;

        System.out.println(view+":\t\t" + message.getCode() );

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






























