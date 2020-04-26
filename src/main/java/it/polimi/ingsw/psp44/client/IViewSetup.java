package it.polimi.ingsw.psp44.client;

public interface IViewSetup<T> {

    //TODO: Maybe futile
    void chooseFromOptions(T options);

    void chooseCardsFrom(T cards);

    void players(T players);

    void chooseNickname(T chooseNickname);
}
