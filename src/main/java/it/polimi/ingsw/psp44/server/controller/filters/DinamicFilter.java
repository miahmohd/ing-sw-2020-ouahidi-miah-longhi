package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

public abstract class DinamicFilter extends Filter {
    private boolean active;
    private Action lastAction;

    /**
     * Active the filter and set the last action performed
     * @param lastAction
     */
    public void update(Action lastAction){
        this.lastAction=lastAction;
        this.active=true;
    }
}
