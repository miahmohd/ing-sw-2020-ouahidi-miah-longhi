package it.polimi.ingsw.psp44.server.controller.states;
import it.polimi.ingsw.psp44.server.controller.filters.Filter;
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

    /**
     * filters that will be applied to move actions
     */
    protected FilterCollection activeMoveFilters;

    /**
     * filters that will be applied to build actions
     */
    protected FilterCollection activeBuildFilters;

    public State(){
        this.activeMoveFilters=new FilterCollection();
        this.activeBuildFilters=new FilterCollection();
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
     * Populates the list of filters to decide which move action are available
     * @param newFilter the list of filters
     */
    public void setActiveMoveFilters(List<Filter> newFilter) {
        //activeMoveFilters.empty();
        for (Filter f: newFilter){
            activeMoveFilters.add(f);
        }
    }

    /** Populates the list of filters to decide which build action are available
     * @param newFilter the list of filters
     */
    public void setActiveBuildFilters(List<Filter> newFilter) {
        //activeBuildFilters.empty();
        for (Filter f: newFilter){
            activeBuildFilters.add(f);
        }
    }

    /**
     * Get the list of active move filters
     * @return list of filters
     */
    public FilterCollection getActiveMoveFilters() {
        return activeMoveFilters;
    }

    /**
     * Get the list of active build filters
     * @return list of filters
     */
    public FilterCollection getActiveBuildFilters() {
        return activeBuildFilters;
    }

    /**
     * Compute the available actions that the player can perform
     * @param board representation of the playing field
     * @param selectedWorker worker selected from the player
     * @return list of available actions
     */
    public abstract List<Action> getAvailableActions(Board board, Position selectedWorker);
}
