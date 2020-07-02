package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.filters.DynamicFilter;
import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.ForceBackwardsMovement;

import java.util.ArrayList;
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
    private final Condition condition;

    /**
     * the dynamic build filter that will be used in the next state
     */
    private final List<DynamicFilter> buildFilters;

    /**
     * The dynamic move filter that will be used in the next state
     */
    private final List<DynamicFilter> moveFilters;

    public Transition(State currentState, State nextState, Condition condition, List<DynamicFilter> buildFilters, List<DynamicFilter> moveFilters) {
        this.currentState = currentState;
        this.nextState = nextState;
        this.condition = condition;
        if (buildFilters != null)
            this.buildFilters = buildFilters;
        else
            this.buildFilters = new ArrayList<>();

        if (moveFilters != null)
            this.moveFilters = moveFilters;
        else
            this.moveFilters = new ArrayList<>();
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
     *
     * @return <code>true</code> if no transition needed, <false>Otherwise</false>
     */
    public boolean isUnconditional() {
        return condition == null;
    }

    /**
     * Check if there have been the required condition to activate this transition
     * Conditions depend on the type of the last action performed
     *
     * @param lastAction
     * @return
     */
    public boolean checkCondition(Action lastAction) {

        if (condition == Condition.MOVE) {
            return lastAction.isMovement() && (!(lastAction instanceof ForceBackwardsMovement));
        }
        if (condition == Condition.BUILD)
            return lastAction.isBuild();
        if (condition == Condition.FORCE_OPONENTS_WORKER)
            return lastAction instanceof ForceBackwardsMovement;
        return false;
    }

    public enum Condition {
        MOVE,
        BUILD,
        FORCE_OPONENTS_WORKER
    }
}
