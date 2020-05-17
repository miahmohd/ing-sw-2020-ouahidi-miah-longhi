package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

public interface IGameView extends IView{

    void chooseWorkersInitialPositionFrom(Message workers);

    void chooseWorkerFrom(Message workers);

    void chooseActionFrom(Message actions);

    void start(Message start);

    void end(Message end);

    void lost(Message lost);

    void won(Message won);

    void update(Message update);

    void activeTurn(Message activePlayer);
}
