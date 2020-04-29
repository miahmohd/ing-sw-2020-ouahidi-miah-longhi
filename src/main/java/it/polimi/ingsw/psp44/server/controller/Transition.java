package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.filters.DynamicFilter;
import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.server.model.actions.Action;

import java.util.List;

/**
 * The transition contains all the information used by the card controller to change his state inside a turn
 */
public class Transition {

    /**
     * The state where the transition start.
     * To activate a transition the <code>CardController</code> must be in this state
     */
    private final State currentState;

    /**
     * The next state where the <code>Card Controller wil be</code>  activating this transition
     */
    private final State nextState;

    /**
     * From a specific state could start different transitions.
     * <code>Condition</code> is used to select the transition to follow.
     */
    private final Action condition;

    /**
     * the dynamic build filter that will be used in the next state
     */
    private final List<DynamicFilter> buildFilters;

    /**
     * The dynamic move filter that will be used in the next state
     */
    private final List<DynamicFilter> moveFilters;

    public Transition(State currentState, State nextState, Action condition, List<DynamicFilter> buildFilters, List<DynamicFilter> moveFilters) {
        this.currentState = currentState;
        this.nextState = nextState;
        this.condition = condition;
        this.buildFilters = buildFilters;
        this.moveFilters = moveFilters;
    }

    public State getCurrentState() {
        return currentState;
    }

    public State getNextState() {
        return nextState;
    }

    public List<DynamicFilter> getBuildFilter() {
        return buildFilters;
    }

    public List<DynamicFilter> getMoveFilter() {
        return moveFilters;
    }

    /**
     * Check if the transition can be activated without any condition
     * @return <code>true</code> if no transition needed, <false>Otherwise</false>
     */
    public boolean isUnconditional() {
        return condition == null;
    }

    /**
     * Check if there have been the required condition to activate this transition
     * Conditions depend on the type of the last action performed
     * @param lastAction
     * @return
     */
    public boolean checkCondition(Action lastAction) {
        if (condition.isMovement())
            return lastAction.isMovement();
        else
            return lastAction.isBuild();
    }
}
