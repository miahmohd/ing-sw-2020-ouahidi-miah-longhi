package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.IVirtual;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.JsonConvert;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualServer implements IVirtual, Runnable {

    private final ExecutorService executor;
    private final IConnection<String> connection;
    private final Map<Message.Code, IMessageHandlerFunction> router;

    private final Object _lock;

    public VirtualServer(IConnection<String> connection) {
        this(connection, new ConcurrentHashMap<>());
    }


    public VirtualServer(IConnection<String> connection, Map<Message.Code, IMessageHandlerFunction> router) {
        this.connection = connection;
        this.router = router;
        this.executor = Executors.newFixedThreadPool(2);
        this._lock = new Object();
    }

    public void addMessageHandler(Message.Code code, IMessageHandlerFunction route) {
        synchronized (_lock) {
            this.router.put(code, route);
            _lock.notifyAll();
        }
    }

    public void cleanMessageHandlers() {
        // FIXME la clear toglie anche gli handler per ping e chiusura
        this.router.clear();
        setPingResponse();
    }

    @Override
    public void run() {
        String rawJson;
        setPingResponse();
        try {
            while ((rawJson = this.connection.readLine()) != null) {

                Message message = JsonConvert.getInstance().fromJson(rawJson, Message.class);
                Message.Code code = message.getCode();

                synchronized (_lock) {
                    while (!this.router.containsKey(code)) {
                        _lock.wait();
                    }
                }

                this.executor.execute(() -> router.get(code).accept(message));

            }
            System.out.println("readline = null");


        } catch (SocketTimeoutException e) {
            System.out.println("SocketTimeoutException");
        } catch (SocketException e) {
            System.out.println("SocketException");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessage(Message message) {
        String messageString = JsonConvert.getInstance().toJson(message, Message.class);
        connection.writeLine(messageString);
    }

    /**
     * After the clients receives the PING message, it replays back with a PING message
     */
    private void setPingResponse() {
        this.addMessageHandler(Message.Code.PING, m -> this.sendMessage(new Message(Message.Code.PING)));
    }

}