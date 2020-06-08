package it.polimi.ingsw.psp44.server.controller.VictoryCondition;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;

/**
 * the player with this victory condition win the match
 * if in the last movement move from the second to the third level
 */
public class DownByTwoVictoryCondition extends VictoryCondition {
    /**
     * Check whether the player has won, with his last action
     *
     * @param lastAction the last action performed by the player
     * @param gameBoard  the game field
     */
    @Override
    public boolean check(Action lastAction, Board gameBoard) {
        int source = gameBoard.getLevel(lastAction.getSourcePosition());
        int target = gameBoard.getLevel(lastAction.getTargetPosition());
        return (source - target) >= 2;
    }
}
