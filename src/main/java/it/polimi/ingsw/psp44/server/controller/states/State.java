package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * The smallest part of player's turn. Each state knows what a worker can do at this specific
 * phase of the game
 */
public abstract class State {
    //TODO Leo se ti piace puoi usare FilterCollection che ha la lista di filtri, altrimenti vedi tu fra
    //Io rimango dell'idea che allo stato devo passare le due collection ma la scelta la lascio a te
    private List<Filter> activeFilters;

    public State(){
        this.activeFilters=new ArrayList<Filter>();
    }

    /**
     * Set the list of filters tu use for decide which action are aviable
     * @param activeFilters the list of filters
     */
    public void setActiveFilters(List<Filter> activeFilters) {
        this.activeFilters = activeFilters;
    }

    /**
     * Compute the available actions that the player can perform
     * @param board representation of the playing field
     * @param selectedWorker worker selected from the player
     * @return list of available actions
     */
    public abstract List<Action> getAviableActions(Board board, Worker selectedWorker);
}
