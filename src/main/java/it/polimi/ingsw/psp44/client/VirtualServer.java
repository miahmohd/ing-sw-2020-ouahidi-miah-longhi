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
    private final Map<Message.Code, Runnable> errorRouter;
    private final Object _lock;
    private boolean errorFlag = true;

    public VirtualServer(IConnection<String> connection) {
        super(connection);
        this.router = new ConcurrentHashMap<>();
        errorRouter = new ConcurrentHashMap<>();

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

    public void addErrorHandler(Message.Code code, Runnable route) {
        this.errorRouter.put(code, route);
    }

    public void cleanMessageHandlers() {
        this.router.clear();
    }

    @Override
    public void run() {
        String rawJson;
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
                Message message = JsonConvert.getInstance().fromJson(rawJson, Message.class);
                this.routeMessage(message);
            }

            if (this.errorFlag) {
                this.routeErrorMessage(new Message(Message.Code.DISCONNECTED));
            }

        } catch (SocketException e) {
            if (this.errorFlag) {
                this.routeErrorMessage(new Message(Message.Code.DISCONNECTED));
            }
        } catch (SocketTimeoutException e) {
            if (this.errorFlag) {
                this.routeErrorMessage(new Message(Message.Code.NETWORK_ERROR));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.executor.shutdownNow();
        this.close();
    }


    private void routeMessage(Message message) {
        Message.Code code = message.getCode();
        if (code == Message.Code.PING)
            return;

//        Can close without errors if won or lost.
        this.errorFlag = code != Message.Code.WON && code != Message.Code.LOST;

        synchronized (_lock) {
            while (!this.router.containsKey(code)) {
                try {
                    _lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        this.executor.execute(() -> router.get(code).accept(message));
    }

    private void routeErrorMessage(Message message){
        Message.Code code = message.getCode();
        if (this.errorRouter.containsKey(code))
            this.errorRouter.get(code).run();
    }

}