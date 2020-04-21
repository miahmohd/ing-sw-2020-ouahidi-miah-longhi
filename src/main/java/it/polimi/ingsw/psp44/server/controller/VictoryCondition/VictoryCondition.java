package it.polimi.ingsw.psp44.server.controller.VictoryCondition;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;

/**
 * Abstract Class that defines the model for a Victory Condition class
 * Defines @see filter
 */
public abstract class VictoryCondition {
    /**
     * Victory condition are usually checked after move actions.
     * if there is an exception and a victory condition must be checked
     */
    protected boolean afterBuild;

    public VictoryCondition() {
        this.afterBuild = false;
    }

    public boolean isAfterBuild() {
        return afterBuild;
    }

    /**
     * Check whether the player has won, with his last action
     *
     * @param lastAction the last action performed by the player
     * @param gameBoard  the game field
     */
    public abstract boolean check(Action lastAction, Board gameBoard);

}





