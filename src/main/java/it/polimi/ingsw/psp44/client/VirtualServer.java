package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.Virtual;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.JsonConvert;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualServer extends Virtual implements Runnable {

    /**
     * This ExecutorService creates daemon threads in order to read from System.in
     * The JVM exits when the only threads running are all daemon threads.
     */
    private final ExecutorService executor;
    private final Map<Message.Code, IMessageHandlerFunction> router;
    private final Object _lock;

    public VirtualServer(IConnection<String> connection) {
        super(connection);
        this.router = new ConcurrentHashMap<>();
        this._lock = new Object();
        this.executor = Executors.newFixedThreadPool(2, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
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

    }

    @Override
    public void run() {
        String rawJson;
        try {

            while ((rawJson = this.connection.readLine()) != null) {
                Message message = JsonConvert.getInstance().fromJson(rawJson, Message.class);
                this.routeMessage(message);
            }

        } catch (SocketTimeoutException e) {
            System.out.println("SocketTimeoutException");
        } catch (SocketException e) {
            System.out.println("SocketException");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        this.executor.shutdownNow();
        this.close();
    }


    private void routeMessage(Message message) throws InterruptedException {
        Message.Code code = message.getCode();
        if (code == Message.Code.PING)
            return;

        synchronized (_lock) {
            while (!this.router.containsKey(code)) {
                _lock.wait();
            }
        }
        this.executor.execute(() -> router.get(code).accept(message));
    }

}