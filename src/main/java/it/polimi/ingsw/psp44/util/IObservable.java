package it.polimi.ingsw.psp44.util;

import java.util.ArrayList;
import java.util.List;

public class IObservable<T> {

    private final List<IObserver<T>> observers = new ArrayList<>();

    public IObservable() {
    }

    public void addObserver(IObserver<T> observer) {
        if (!this.observers.contains(observer) && observer != null)
            this.observers.add(observer);
    }

    public void deleteObserver(IObserver<T> observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers(T arg) {
        for (IObserver<T> o : this.observers) {
            o.update(this, arg);
        }
    }
}
