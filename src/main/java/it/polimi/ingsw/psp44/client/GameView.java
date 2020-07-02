package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

public abstract class GameView extends View {
    /**
     * Callback to what to do when a chooseWorkersInitialPosition message is received
     *
     * @param workers message
     */
    public abstract void chooseWorkersInitialPositionFrom(Message workers);

    /**
     * Callback to what to do when a chooseWorker message is received
     *
     * @param workers message
     */
    public abstract void chooseWorkerFrom(Message workers);

    /**
     * Callback to what to do when a chooseAction message is received
     *
     * @param actions message
     */
    public abstract void chooseActionFrom(Message actions);

    /**
     * Callback to what to do when a start message is received
     *
     * @param start message
     */
    public abstract void start(Message start);

    /**
     * Callback to what to do when a gameCreated message is received
     *
     * @param end message
     */
    public abstract void end(Message end);

    /**
     * Callback to what to do when a lost message is received
     *
     * @param lost message
     */
    public abstract void lost(Message lost);

    /**
     * Callback to what to do when a won message is received
     *
     * @param won message
     */
    public abstract void won(Message won);

    /**
     * Callback to what to do when a update message is received
     *
     * @param update message
     */
    public abstract void update(Message update);

    /**
     * Callback to what to do when a activePlayer message is received
     *
     * @param activePlayer message
     */
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
