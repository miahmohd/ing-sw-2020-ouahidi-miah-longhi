package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

public abstract class DynamicFilter extends Filter {

    public DynamicFilter() {
        super();
        this.active=false;
    }

    /**
     * Update the sate of the filter
     * @param lastAction
     */

    @Override
    public void update(Action lastAction){
        super.update(lastAction);
        this.active=true;
    }
}
