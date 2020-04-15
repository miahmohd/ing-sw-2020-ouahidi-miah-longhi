package it.polimi.ingsw.psp44.server.controller.states;
import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.AppProperties;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;
import it.polimi.ingsw.psp44.util.exception.FilterException;
import it.polimi.ingsw.psp44.util.exception.StateException;

import java.util.ArrayList;
import java.util.List;

/**
 * CompoundState extends State.
 * Has simple collection methods (add and remove)
 * Architecture based on the composite pattern (https://en.wikipedia.org/wiki/Composite_pattern)
 */
public class CompoundState extends State {
    private List<State> simpleStates;

    public CompoundState(){
        this.simpleStates=new ArrayList<>();
    }

    /**
     * Compute the available actions that the player can perform
     *
     * @param board          representation of the playing field
     * @param selectedWorker worker selected from the player
     * @param moveFilter     filter to apply to move actions
     * @param buildFilter    filter to apply to build actions
     * @return list of available actions
     */
    @Override
    public List<Action> getAvailableActions(Board board, Position selectedWorker, FilterCollection moveFilter, FilterCollection buildFilter) {
        ArrayList<Action> availableActions=new ArrayList<>();
        if(simpleStates.isEmpty())
            throw new StateException(AppProperties.getInstance().getMessage(ErrorCodes.NO_STATE_IN_COLLECTION));
        for (State s:simpleStates)
            availableActions.addAll(s.getAvailableActions(board,selectedWorker, moveFilter, buildFilter));
        return availableActions;
    }

    public CompoundState(State... states){
        this.simpleStates=new ArrayList<>();
        for(State s: states){
            this.simpleStates.add(s);
        }
    }

    /**
     * Adds a state to the list of states in the list
     * @param newState the state to add
     * @throws IllegalArgumentException if newState is null
     */
     public void addState(State newState){
         if(newState == null)
             throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_STATE));
        this.simpleStates.add(newState);
     }

    /**
     * Removes a state form the list of states
     * @param targetState, the state to remove
     * @throws IllegalArgumentException if Filter is null
     * @throws FilterException if filter is not in the list
     */
     public void removeState(State targetState){
         if(targetState == null)
             throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_STATE));
         if(!simpleStates.contains(targetState))
             throw new StateException(AppProperties.getInstance().getMessage(ErrorCodes.STATE_NOT_IN_COLLECTION));
        this.simpleStates.remove(targetState);
     }

    /**
     * Removes all the states from the list of states
     */
     public void empty(){
        simpleStates.clear();
     }

    /**
     * Get the list of simple state in this compound state
     * @return a list of State
     */
    public List<State> getSimpleStates() {
        return simpleStates;
    }






}
