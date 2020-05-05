package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.IVirtual;
import it.polimi.ingsw.psp44.network.message.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualServer implements IVirtual<Message>, Runnable {

    private final ExecutorService executor;
    private final IConnection<Message> connection;
    private final Map<Message.Code, IMessageHandlerFunction> router;
    private final IView<Message> view;

    public VirtualServer(IConnection connection, IView<Message> view) {
        this.connection = connection;
        this.view = view;

        router = new HashMap<>();
        view.setServer(this);

        this.executor = Executors.newFixedThreadPool(100);
        initRouter();
    }

    private void initRouter() {
        router.put(Message.Code.START, view::startTurn);
        router.put(Message.Code.CHOOSE_CARDS, view::chooseCardsFrom);
        router.put(Message.Code.CHOOSE_CARD, view::chooseCardFrom);
        router.put(Message.Code.CHOOSE_WORKERS_INITIAL_POSITION, view::chooseWorkersInitialPositionFrom);
        router.put(Message.Code.CHOOSE_WORKER, view::chooseWorkerFrom);
        router.put(Message.Code.MODIFIED_POSITIONS, view::update);
        router.put(Message.Code.ALL_PLAYER_NICKNAMES, view::allPlayerNicknames);
        router.put(Message.Code.CHOOSE_ACTION, view::chooseActionFrom);
    }

    @Override
    public void run() {
        while (true) {
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