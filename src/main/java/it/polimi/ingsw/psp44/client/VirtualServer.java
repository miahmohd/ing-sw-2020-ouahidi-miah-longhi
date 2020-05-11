package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.IVirtual;
import it.polimi.ingsw.psp44.network.message.Message;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualServer implements IVirtual<Message>, Runnable {

    private final ExecutorService executor;
    private final IConnection<Message> connection;
    private final Map<Message.Code, IMessageHandlerFunction> router;
    private final Queue<Message> buffer;

    public VirtualServer(IConnection connection) {
        this(connection, new ConcurrentHashMap<>());
    }


    public VirtualServer(IConnection connection, Map<Message.Code, IMessageHandlerFunction> router){
        this.connection = connection;
        this.router = router;

        this.executor = Executors.newFixedThreadPool(2);
        this.buffer = new PriorityQueue<>();
    }

    public synchronized void addRoute(Message.Code code, IMessageHandlerFunction route) {
        //TODO: allinearmi con miah per i nomi
        //System.out.println("i'm adding to routes");
        this.router.put(code, route);

        notify();
    }

    public void cleanRoutes(){
        //System.out.println("i'm trying to clean");
        this.router.clear();
    }

    @Override
    public synchronized void run() {
        while (true) {

            Message message = connection.readLine();
            Message.Code code = message.getCode();
            //System.out.println(code);

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