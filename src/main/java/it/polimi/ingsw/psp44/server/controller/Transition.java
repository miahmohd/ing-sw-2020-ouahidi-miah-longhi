package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.filters.DynamicFilter;
import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.server.model.actions.Action;

import java.util.List;


public class Transition {
    private final State currentState;
    private final State nextState;
    private final Action condition;
    private final List<DynamicFilter> buildFilters;
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

    public boolean isUnconditional() {
        return condition == null;
    }

    public boolean checkCondition(Action lastAction) {
        if (condition.isMovement())
            return lastAction.isMovement();
        else
            return lastAction.isBuild();
    }
}
