package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.IVirtual;
import it.polimi.ingsw.psp44.network.message.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualServer implements IVirtual<Message>, Runnable {

    private final ExecutorService executor;
    private final IConnection<Message> connection;
    private final Map<Message.Code, IMessageHandlerFunction> router;

    public VirtualServer(IConnection connection) {
        this(connection, new ConcurrentHashMap<>());
    }


    public VirtualServer(IConnection connection, Map<Message.Code, IMessageHandlerFunction> router){
        this.connection = connection;
        this.router = router;

        this.executor = Executors.newFixedThreadPool(2);
    }

    public synchronized void addMessageHandler(Message.Code code, IMessageHandlerFunction route) {
        this.router.put(code, route);
        notify();
    }

    public void cleanMessageHandlers(){
        this.router.clear();
    }

    @Override
    public synchronized void run() {
        while (true) {

            Message message = connection.readLine();
            Message.Code code = message.getCode();

            while(!this.router.containsKey(code)){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            executor.execute(() -> router.get(code).accept(message));
        }
    }

    @Override
    public void sendMessage(Message message) {
        connection.writeLine(message);
    }
}