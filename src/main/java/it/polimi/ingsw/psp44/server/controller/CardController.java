package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.VictoryCondition.VictoryCondition;
import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.AppProperties;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * This class menages the turn of a player.
 * Each turn is a deterministic finite state machine, where the states are associated
 * with the actions that the player can perform.
 */
public class CardController {
    private final Controller context;
    /**
     * The initial state of the turn
     */
    private final State initialState;
    /**
     * A list of the possible states transitions for the card
     */
    private final List<Transition> transitionsList;
    /**
     * A list of victory condition for the card
     */
    private final List<VictoryCondition> victoryConditionsList;
    /**
     * filters to apply when computing build actions
     */
    private FilterCollection activeBuildFilter;
    /**
     * filters to apply when computing move actions
     */
    private FilterCollection activeMoveFilter;
    /**
     * The current state of the player turn
     */
    private State currentState;


    public CardController(State initialState, List<Transition> transitionsList, List<VictoryCondition> victoryConditionsList, Controller context) {
        this.context = context;
        this.initialState = initialState;
        this.transitionsList = transitionsList;
        this.victoryConditionsList = victoryConditionsList;
        Transition loopBackTransition = transitionsList.stream()
                .filter((t) -> initialState.equals(t.getNextState()))
                .findFirst()
                .orElse(null);
        executeTransition(loopBackTransition, null);
    }

    /**
     * Compute all the action that a worker can do
     *
     * @param board          the playground of the match, used to check the available actions
     * @param selectedWorker the worker that the player wants to move
     * @return a list with the available actions
     */
    public List<Action> getAvailableAction(Board board, Position selectedWorker) {
        List<Action> availableActions = currentState.getAvailableActions(board, selectedWorker, activeMoveFilter, activeBuildFilter);
        activeBuildFilter.empty();
        activeMoveFilter.empty();
        return availableActions;
    }

    /**
     * Find the positions of the
     *
     * @param board          the playground of the match, used to find the worker
     * @param playerNickname the player that is playing the turn
     * @return a list with the positions of the player's worker
     */
    public List<Position> getWorkers(Board board, String playerNickname) {
        List<Position> availableWorkers = new ArrayList<>();
        for (Position p : board.getPlayerWorkersPositions(playerNickname))
            if (!currentState.getAvailableActions(board, p, activeMoveFilter, activeBuildFilter).isEmpty())
                availableWorkers.add(p);
        return availableWorkers;
    }


    /**
     * After that an action is performed menage the transition to the next status
     * if there are no transition active means that the turn is ended
     *
     * @param lastAction the last action performed
     * @return true if there is a new state, false if the turn is ended
     * @throws IllegalArgumentException there aren't active transitions
     */
    public boolean nextState(Action lastAction) {

        Transition activeTransition = transitionsList.stream()
                .filter((t) -> currentState.equals(t.getCurrentState())
                        && (t.isUnconditional() || t.checkCondition(lastAction)))
                .findFirst()
                .orElse(null);
        executeTransition(activeTransition, lastAction);
        return !currentState.equals(initialState);
    }

    /**
     * check victory conditions, set status to WON is a condition is proved
     *
     * @param lastAction last action performed
     * @param board      the playing field
     */
    public boolean checkVictory(Action lastAction, Board board) {
        return victoryConditionsList.stream()
                .filter((condition) -> lastAction.isBuild() == condition.isAfterBuild())
                .anyMatch((condition) -> condition.check(lastAction, board));
    }

    /**
     * set next status and activate filters
     *
     * @param transition transition that must be performed
     * @param lastAction last action performed
     * @throws IllegalArgumentException if transition is null
     */
    private void executeTransition(Transition transition, Action lastAction) {
        if (currentState == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getProperty(ErrorCodes.TRANSITION_SCHEMA_ERROR));

        currentState = transition.getNextState();

        transition.getBuildFilter(lastAction).forEach((filter) -> {
            if (filter.isExternal())
                context.appliesOpponentsBuildFilter(filter);
            else
                activeBuildFilter.add(filter);
        });

        transition.getMoveFilter(lastAction).forEach((filter) -> {
            if (filter.isExternal())
                context.appliesOpponentsMoveFilter(filter);
            else
                activeMoveFilter.add(filter);
        });
    }

    /**
     * add a filter to activeBuildFilter list
     *
     * @param filter to add
     */
    public void addBuildFilter(Filter filter) {
        activeBuildFilter.add(filter);
    }

    /**
     * add a filter to activeMoveFilter list
     *
     * @param filter to add
     */
    public void addMoveFilter(Filter filter) {
        activeMoveFilter.add(filter);
    }

    /**
     * check if current state is final
     * @return <code>true</code> if current state is final <code>false</code> otherwise
     */
    public boolean isEndableTurn() {
        return currentState.isFinalState();
    }
}
