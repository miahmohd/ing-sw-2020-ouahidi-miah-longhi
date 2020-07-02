package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

public abstract class SetupView extends View {

    /**
     * Callback to what to do when a chooseCards message is received
     * @param cards message
     */
    public abstract void chooseCardsFrom(Message cards);

    /**
     * Callback to what to do when a chooseCard message is received
     * @param cards message
     */
    public abstract void chooseCardFrom(Message cards);

    /**
     * Callback to what to do when a allPlayerCards message is received
     * @param players message
     */
    public abstract void allPlayerCards(Message players);

    /**
     * Callback to what to do when a start message is received
     * @param start message
     */
    public abstract void start(Message start);

    /**
     * Callback to what to do when an end message is received
     * @param end message
     */
    public abstract void end(Message end);

    @Override
    public void setServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;

        virtualServer.cleanMessageHandlers();

        virtualServer.addMessageHandler(Message.Code.ALL_PLAYER_CARDS, this::allPlayerCards);
        virtualServer.addMessageHandler(Message.Code.START_TURN, this::start);
        virtualServer.addMessageHandler(Message.Code.CHOOSE_CARD, this::chooseCardFrom);
        virtualServer.addMessageHandler(Message.Code.CHOOSE_CARDS, this::chooseCardsFrom);
        virtualServer.addMessageHandler(Message.Code.END_TURN, this::end);
    }
}
