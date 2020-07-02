package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

public abstract class AbstractGameView extends AbstractView {

    public abstract void chooseWorkersInitialPositionFrom(Message workers);

    public abstract void chooseWorkerFrom(Message workers);

    public abstract void chooseActionFrom(Message actions);

    public abstract void start(Message start);

    public abstract void end(Message end);

    public abstract void lost(Message lost);

    public abstract void won(Message won);

    public abstract void update(Message update);

    public abstract void activeTurn(Message activePlayer);

    @Override
    public void setServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;

        virtualServer.cleanMessageHandlers();

        virtualServer.addMessageHandler(Message.Code.START_TURN, this::start);
        virtualServer.addMessageHandler(Message.Code.END_TURN, this::end);
        virtualServer.addMessageHandler(Message.Code.CHOOSE_WORKER, this::chooseWorkerFrom);
        virtualServer.addMessageHandler(Message.Code.UPDATE, this::update);
        virtualServer.addMessageHandler(Message.Code.CHOOSE_ACTION, this::chooseActionFrom);
        virtualServer.addMessageHandler(Message.Code.WON, this::won);
        virtualServer.addMessageHandler(Message.Code.LOST, this::lost);
        virtualServer.addMessageHandler(Message.Code.CHOOSE_WORKERS_INITIAL_POSITION, this::chooseWorkersInitialPositionFrom);
        virtualServer.addMessageHandler(Message.Code.UPDATE, this::update);
        virtualServer.addMessageHandler(Message.Code.ACTIVE_TURN, this::activeTurn);
    }
}
