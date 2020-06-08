package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;

public abstract class DynamicFilter extends Filter {

    public DynamicFilter() {
        super();
        this.active = false;
    }

    /**
     * Update the sate of the filter
     *
     * @param lastAction
     * @param board
     */

    @Override
    public void update(Action lastAction, Board board) {
        super.update(lastAction, board);
        this.active = true;
    }
}
