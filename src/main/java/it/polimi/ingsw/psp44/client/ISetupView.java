package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.message.Message;

public interface ISetupView extends IView{

    void chooseCardsFrom(Message cards);

    void chooseCardFrom(Message cards);

    void allPlayerCards(Message players);

    void start(Message start);

    void end(Message end);
}
