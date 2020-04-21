package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

/**
 * The smallest part of player's turn. Each state knows what a worker can do at this specific
 * phase of the game
 */
public abstract class State {
    /**
     * if set to true the player can decide to end the turn without performing actions
     */
    protected boolean finalState;


    public State(){
        this.finalState=false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        return getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean isFinalState() {
        return finalState;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }


    /**
     * Compute the available actions that the player can perform
     * @param board representation of the playing field
     * @param selectedWorker worker selected from the player
     * @param moveFilter filter to apply to move actions
     * @param buildFilter filter to apply to build actions
     * @return list of available actions
     */
    public abstract List<Action> getAvailableActions(Board board, Position selectedWorker, FilterCollection moveFilter, FilterCollection buildFilter);
}
