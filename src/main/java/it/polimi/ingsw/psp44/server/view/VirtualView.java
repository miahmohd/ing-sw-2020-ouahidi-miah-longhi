package it.polimi.ingsw.psp44.server.view;

import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.IVirtual;
import it.polimi.ingsw.psp44.network.message.IMessageHandlerFunction;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.*;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Class representing the view of a single player.
 */
public class VirtualView implements Runnable, IVirtual, IObserver<Message> {

    private static final int TIMEOUT = Integer.parseInt(R.getAppProperties().get(ConfigCodes.TIMEOUT));
    private static final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    private final IConnection<String> connection;
    private final Map<Message.Code, IMessageHandlerFunction> handlers;

    private ScheduledFuture<?> scheduledFuture;
    private Message lastSend;

    public VirtualView(IConnection<String> connection) {
        this.connection = connection;
        this.handlers = new EnumMap<>(Message.Code.class);
    }

    @Override
    public void run() {
        String rawJson;
        Message message;

        startPingDaemon();

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
            System.out.println("SocketTimeoutException");
            this.routeMessage(this, new Message(Message.Code.CLIENT_DISCONNECTED));
        } catch (SocketException ignored) {
            System.out.println("SocketException" + ignored.getMessage());
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
        String messageString = JsonConvert.getInstance().toJson(message, Message.class);
        connection.writeLine(messageString);
        lastSend = message;
    }


    @Override
    public void update(IObservable<Message> observable, Message arg) {
        String messageString = JsonConvert.getInstance().toJson(arg, Message.class);
        connection.writeLine(messageString);
    }


    /**
     * Closes the connection with the view and the scheduled task tha sends PING messages.
     */
    public void close() {
        this.scheduledFuture.cancel(true);
        this.connection.close();
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

    /**
     * Starts a daemon that sends a PING message to the client repeatedly after TIMEOUT / 2 seconds.
     * The client is reachable if the server receives a PING message response.
     * If after TIMEOUT second the server did not receive a PING response, the socket throws a SocketTimeoutException.
     */
    private void startPingDaemon() {
        int delay = TIMEOUT / 2;
        this.scheduledFuture = scheduledExecutor.scheduleAtFixedRate(this::sendPing, delay, delay, TimeUnit.MILLISECONDS);
        this.addMessageHandler(Message.Code.PING, (v, m) -> System.out.println("Ping received from " + v));
    }

    private void sendPing() {
        Message msg = new Message(Message.Code.PING);
        this.connection.writeLine(JsonConvert.getInstance().toJson(msg, Message.class));
    }

}






























