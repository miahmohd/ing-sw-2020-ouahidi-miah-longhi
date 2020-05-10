package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.VictoryCondition.VictoryCondition;
import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;
import it.polimi.ingsw.psp44.util.property.AppProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * This class menages the turn of a player.
 * Each turn is a deterministic finite state machine, where the states are associated
 * with the actions that the player can perform.
 */
public class CardController {
    private Controller context;
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
    private FilterCollection buildFilter;
    /**
     * filters to apply when computing move actions
     */
    private FilterCollection moveFilter;
    /**
     * The current state of the player turn
     */
    private State currentState;


    public CardController(
            List<Transition> transitionsList,
            List<VictoryCondition> victoryConditionsList,
            FilterCollection buildFilter,
            FilterCollection moveFilter) {

        this.context = null;
        this.currentState = transitionsList.stream().filter((t) -> t.getNextState().isInitialState()).findFirst().get().getNextState();
        this.transitionsList = transitionsList;
        this.victoryConditionsList = victoryConditionsList;
        this.buildFilter = buildFilter;
        this.moveFilter = moveFilter;

    }

    public void setContext(Controller context) {
        this.context = context;
    }

    /**
     * Compute all the action that a worker can do
     *
     * @param board          the playground of the match, used to check the available actions
     * @param selectedWorker the worker that the player wants to move
     * @return a list with the available actions
     */
    public List<Action> getAvailableAction(Board board, Position selectedWorker) {
        List<Action> availableActions = currentState.getAvailableActions(board, selectedWorker, moveFilter, buildFilter);
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
            if (!currentState.getAvailableActions(board, p, moveFilter, buildFilter).isEmpty())
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
    public boolean nextState(Action lastAction, Board board) {

        Transition activeTransition = transitionsList.stream()
                .filter((t) -> currentState.equals(t.getCurrentState())
                        && (t.isUnconditional() || t.checkCondition(lastAction)))
                .findFirst()
                .orElse(null);
        executeTransition(activeTransition, lastAction, board);
        return !currentState.isInitialState();
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
     * @param board
     * @throws IllegalArgumentException if transition is null
     */
    private void executeTransition(Transition transition, Action lastAction, Board board) {
        if (currentState == null)
            throw new IllegalArgumentException(AppProperties.getInstance().get(ErrorCodes.TRANSITION_SCHEMA_ERROR));

        currentState = transition.getNextState();

        transition.getBuildFilter().forEach((filter) -> {
            if (filter.isExternal())
                context.appliesOpponentsBuildFilter(filter, lastAction);
            else
                buildFilter.update(filter, lastAction, board);
        });

        transition.getMoveFilter().forEach((filter) -> {
            if (filter.isExternal())
                context.appliesOpponentsMoveFilter(filter, lastAction);
            else
                moveFilter.update(filter, lastAction, board);
        });
    }

    /**
     * add a filter to activeBuildFilter list
     *
     * @param filter to add
     */
    public void addBuildFilter(Filter filter, Action lastAction, Board board) {
        if (buildFilter.contains(filter)) {
            buildFilter.update(filter, lastAction, board);
        } else {
            filter.update(lastAction, board);
            buildFilter.add(filter);
        }
    }

    /**
     * add a filter to activeMoveFilter list
     *
     * @param filter to add
     */
    public void addMoveFilter(Filter filter, Action lastAction, Board board) {
        if (moveFilter.contains(filter)) {
            moveFilter.update(filter, lastAction, board);
        } else {
            filter.update(lastAction, board);
            moveFilter.add(filter);
        }
    }

    /**
     * check if current state is final
     *
     * @return <code>true</code> if current state is final <code>false</code> otherwise
     */
    public boolean isEndableTurn() {
        return currentState.isFinalState();
    }


    public void setContext(Controller context){
        this.context = context;
    }
}
