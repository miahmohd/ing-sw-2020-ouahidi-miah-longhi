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

    public List<Filter> getBuildFilter() {
        return buildFilters;
    }

    public List<Filter> getMoveFilter() {
        return moveFilters;
    }

    public boolean isUnconditional() {
        return condition==null;
    }
    public Class<? extends Action> getCondition() {
        return condition.getClass();
    }

}
