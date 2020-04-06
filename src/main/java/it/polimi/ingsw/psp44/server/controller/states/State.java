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
     * Compute the available actions that the player can perform
     * @param board representation of the playing field
     * @param selectedWorker worker selected from the player
     * @return list of available actions
     */
    public abstract List<Action> getAvailableActions(Board board, Position selectedWorker);
}
