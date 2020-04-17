package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.server.model.actions.Action;

import java.util.List;


public class Transition {
    private State currentState;
    private State nextState;
    private Action condition;
    private List<Filter> buildFilters;
    private List<Filter> moveFilters;

    public Transition(State currentState, State nextState, Action condition, List<Filter> buildFilters, List<Filter> moveFilters) {
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

    public List<Filter> getBuildFilter(Action lastAction) {
        buildFilters.stream().forEach((filter)->filter.setLastAction(lastAction));
        return buildFilters;
    }

    public List<Filter> getMoveFilter(Action lastAction) {
        buildFilters.stream().forEach((filter)->filter.setLastAction(lastAction));
        return moveFilters;
    }

    public boolean isUnconditional() {
        return condition==null;
    }

    public boolean checkCondition(Action lastAction) {
        if (condition.isMovement())
            return lastAction.isMovement();
        else
            return lastAction.isConstruction();
    }
}
