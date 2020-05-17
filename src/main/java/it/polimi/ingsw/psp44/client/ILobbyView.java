package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

public interface ILobbyView extends IView{

    void newJoin(Message joinOrNew);

    void gameCreated(Message gameCreated);

    void gameJoined(Message gameJoined);
}
