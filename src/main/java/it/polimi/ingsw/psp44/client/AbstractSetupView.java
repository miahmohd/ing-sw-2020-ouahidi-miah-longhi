package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

public abstract class AbstractSetupView extends AbstractView {

    public abstract void chooseCardsFrom(Message cards);

    public abstract void chooseCardFrom(Message cards);

    public abstract void allPlayerCards(Message players);

    public abstract void start(Message start);

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
