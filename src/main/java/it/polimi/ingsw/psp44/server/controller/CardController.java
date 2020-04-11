package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

/**
 * This class menages the turn of a player.
 * Each turn is a deterministic finite state machine, where the states are associated
 * with the actions that the player can perform.
 */
public class CardController {
    /**
     * The turn status is a signal that report particular condition of the turn:
     * - RUN: the turn is running
     * - WON: current player has won the match
     * - LOST: current player has lost the match
     * - END: the turn is ended
     */
    private enum Status{
        RUN,
        WON,
        LOST,
        END
    }

    /**
     *  The status of the turn
     */
    private Status turnStatus;
    /**
     * A list of the possible states transitions for the card
     */
    private List<Transition> transitionsList;

    /**
     * A list of victory condition for the card
     */
    //TODO
   // private List<VictoryCondition> victoryConditionsList;

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
        //TODO gestire la fine del turno --> se lo stato è finale aggiungi la mossa end turn
        //TODO gestione della assenza di mosse disponibili
        return currentState.getAvailableActions(board,selectedWorker);
    }

    /**
     * Find the positions of the
     * @param board the playground of the match, used to find the worker
     * @param playerNickname the player that is playing the turn
     * @return a list with the positions of the player's worker
     */
    public List<Position> getWorkers(Board board, String playerNickname){
        //TODO gestione worker che non si possono muovere
        return board.getPlayerWorkersPositions(playerNickname);
    }

    /**
     * After that an action is performed menage the transition to the next status
     * @param lastAction the last action performed
     */
    public void nextState(Action lastAction){
        //TODO gestione della vittoria

        if(this.turnStatus==Status.WON)
            return;

        Transition activeTransition= transitionsList.stream()
                .filter((t)-> currentState.equals(t.getCurrentState())
                        && ( t.isUnconditional() || t.getCondition().isAssignableFrom(lastAction.getClass())))
                .findFirst()
                .orElse(null);

        if(activeTransition==null){
            this.turnStatus=Status.END;
            return;
        }
            currentState = activeTransition.getNextState();
            currentState.setActiveBuildFilters(activeTransition.getBuildFilter());
            currentState.setActiveMoveFilters(activeTransition.getMoveFilter());

    }

    /**
     * reports that the player has won during the current turn
     * @return true if the player has won, false otherwise
     */
    public boolean hasWon() {
        return turnStatus==Status.WON;
    }

    /**
     * check whether the turn i ended
     * @return true if the turn is ended, false otherwise
     */
    public boolean isTurnEnded() {
        return this.turnStatus==Status.END;
    }

    /**
     * initialize the player's turn. set the first state and set turn status to running
     */
    public void startTrun() {
        turnStatus=Status.RUN;
        currentState=initialState;
    }
}
