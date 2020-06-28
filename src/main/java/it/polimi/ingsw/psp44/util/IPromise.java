package it.polimi.ingsw.psp44.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a task that may be explicitly completed, and supports functions that triggers upon completion.
 * Inspired from https://developer.mozilla.org/it/docs/Web/JavaScript/Reference/Global_Objects/Promise
 */
public class IPromise {
    private final List<Runnable> callbacks = new ArrayList<>();

    public IPromise() {
    }

    public IPromise then(Runnable cb) {
        this.callbacks.add(cb);
        return this;
    }

    public void resolve() {
        for (Runnable cb : this.callbacks) {
            cb.run();
        }
    }

}
