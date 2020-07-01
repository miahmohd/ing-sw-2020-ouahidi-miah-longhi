package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

public abstract class LobbyView extends View {

    public abstract void newJoin(Message joinOrNew);

    public abstract void gameCreated(Message gameCreated);

    public abstract void gameJoined(Message gameJoined);

    @Override
    public void setServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;

        this.virtualServer.addMessageHandler(Message.Code.NEW_OR_JOIN, this::newJoin);
        this.virtualServer.addMessageHandler(Message.Code.GAME_CREATED, this::gameCreated);
        this.virtualServer.addMessageHandler(Message.Code.GAME_JOINED, this::gameJoined);
    }
}
