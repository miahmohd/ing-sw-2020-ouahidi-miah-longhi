package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

public abstract class LobbyView extends View {

    /**
     * Callback to what to do when a newJoin message is received
     *
     * @param joinOrNew message
     */
    public abstract void newJoin(Message joinOrNew);

    /**
     * Callback to what to do when a gameCreated message is received
     *
     * @param gameCreated message
     */
    public abstract void gameCreated(Message gameCreated);

    /**
     * Callback to what to do when a gameJoined message is received
     *
     * @param gameJoined message
     */
    public abstract void gameJoined(Message gameJoined);

    @Override
    public void setServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;

        this.virtualServer.addMessageHandler(Message.Code.NEW_OR_JOIN, this::newJoin);
        this.virtualServer.addMessageHandler(Message.Code.GAME_CREATED, this::gameCreated);
        this.virtualServer.addMessageHandler(Message.Code.GAME_JOINED, this::gameJoined);
    }
}
