package it.polimi.ingsw.psp44.util;

public interface IObserver<T> {
    void update(IObservable<T> observable, T arg);
}
