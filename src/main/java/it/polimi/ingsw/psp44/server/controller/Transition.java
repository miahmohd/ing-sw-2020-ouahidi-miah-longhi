package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.filters.DinamicFilter;
import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.server.model.actions.Action;

import java.util.List;


public class Transition {
    private final State currentState;
    private final State nextState;
    private final Action condition;
    private final List<DinamicFilter> buildFilters;
    private final List<DinamicFilter> moveFilters;

    public Transition(State currentState, State nextState, Action condition, List<DinamicFilter> buildFilters, List<DinamicFilter> moveFilters) {
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

    public List<DinamicFilter> getBuildFilter() {
        return buildFilters;
    }

    public List<DinamicFilter> getMoveFilter() {
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
