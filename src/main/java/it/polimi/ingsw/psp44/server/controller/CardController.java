package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.VictoryCondition.VictoryCondition;
import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * This class menages the turn of a player.
 * Each turn is a deterministic finite state machine, where the states are associated
 * with the actions that the player can perform.
 */
public class CardController {

    /**
     * A list of the possible states transitions for the card
     */
    private List<Transition> transitionsList;
    /**
     * filters to apply when computing build actions
     */
    private FilterCollection activeBuildFilter;
    /**
     * filters to apply when computing move actions
     */
    private FilterCollection activeMoveFilter;
    /**
     * A list of victory condition for the card
     */
    private List<VictoryCondition> victoryConditionsList;
    /**
     * The current state of the player turn
     */
    private State currentState;
    /**
     * The initial state of the turn
     */
    private State initialState;

   
    /**
     * Compute all the action that a worker can do
     * @param board the playground of the match, used to check the available actions
     * @param selectedWorker the worker that the player wants to move
     * @return a list with the available actions
     */
    public List<Action> getAction(Board board, Position selectedWorker){
        List<Action> availableActions=currentState.getAvailableActions(board,selectedWorker,activeMoveFilter,activeBuildFilter);
        return availableActions;
    }

    /**
     * Find the positions of the
     * @param board the playground of the match, used to find the worker
     * @param playerNickname the player that is playing the turn
     * @return a list with the positions of the player's worker
     */
    public List<Position> getWorkers(Board board, String playerNickname){
        List<Position> availableWorkers=new ArrayList<>();
        for(Position p : board.getPlayerWorkersPositions(playerNickname))
            if(!currentState.getAvailableActions(board,p,activeMoveFilter,activeBuildFilter).isEmpty())
                availableWorkers.add(p);
        return availableWorkers;
    }


    /**
     * After that an action is performed menage the transition to the next status
     * if there are no transition active means that the turn is ended
     * @param lastAction the last action performed
     * @return true if there is a new state, false if the turn is ended
     */
    public boolean nextState(Action lastAction){

        Transition activeTransition= transitionsList.stream()
                .filter((t)-> currentState.equals(t.getCurrentState())
                        && ( t.isUnconditional() || t.getCondition().isAssignableFrom(lastAction.getClass())))
                .findFirst()
                .orElse(null);

        if(activeTransition==null){
            return false;
        }
            currentState = activeTransition.getNextState();
            activeBuildFilter.empty();
            activeMoveFilter.empty();
            activeTransition.getBuildFilter().forEach(filter -> activeBuildFilter.add(filter) );
            activeTransition.getMoveFilter().forEach(filter -> activeMoveFilter.add(filter));
            return true;

    }

    /**
     * initialize the player's turn. set the first state
     */
    public void startTurn() {
        currentState=initialState;
    }

    /**
     * check victory conditions, set status to WON is a condition is proved
     * @param lastAction last action performed
     * @param board the playing field
     */
    public boolean checkVictory(Action lastAction, Board board) {
        return victoryConditionsList.stream()
                .filter((condition)->lastAction.isCostruction()?condition.isAfterBuildCheck():
                        !condition.isAfterBuildCheck())
                .anyMatch((condition)-> condition.check(lastAction,board));

    }
}
