package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

/**
 * Abstract Class that defines the model for a Filter class
 * Defines @see filter
 * Ovverrides equals method so that two objects that have the same class are the same
 */
public abstract class Filter {
    protected boolean external;
    protected boolean active;
    protected Action lastAction;

    public Filter() {
        this.active = true;
        this.external = false;
    }

    /**
     * Modifies the list based on the method implementation
     * The method implementation is "determined" by its class name
     * The method can use other parameters from the actual class
     *
     * @param startingPosition  position from which the filterage starts
     * @param positionsToFilter List of positions that needs to be filtered
     * @param gameBoard         provides information about a certain position using it's getters
     */
    public abstract void filter(Position startingPosition, List<Position> positionsToFilter, Board gameBoard);


    /**
     * Update the sate of the filter
     *
     * @param lastAction
     * @param board
     */
    public void update(Action lastAction, Board board) {
        this.lastAction = lastAction;
    }

    public boolean isExternal() {
        return external;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        return getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}