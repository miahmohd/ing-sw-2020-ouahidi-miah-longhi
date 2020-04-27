package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.Connection;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.IVirtual;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualServer implements IVirtual<Message>, Runnable {

    private final ExecutorService executor;
    private final Connection<Message> connection;
    private final Map<Message.Code, IMessageHandlerFunction> router;
    private final IView<Message> view;

    public VirtualServer(Connection connection, IView<Message> view) {
        this.connection = connection;
        this.view = view;

        router = new HashMap<>();
        view.setServer(this);

        this.executor = Executors.newFixedThreadPool(100);
        initRouter();
    }

    private void initRouter(){
        router.put(Message.Code.START, view::startTurn);
        router.put(Message.Code.CHOOSE_CARDS, view::chooseCardsFrom);
        router.put(Message.Code.CHOOSE_CARD, view::chooseCardFrom);
    }

    @Override
    public void run() {
        while(true){
            Message message = connection.readLine();
            Message.Code code = message.getCode();
            //TODO: make it better

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    router.get(code).accept(message);
                }
            });

        }
    }

    @Override
    public void sendMessage(Message message) {
        connection.writeLine(message);
    }
}