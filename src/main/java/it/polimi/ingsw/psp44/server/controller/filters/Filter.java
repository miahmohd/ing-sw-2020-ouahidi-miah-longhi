package it.polimi.ingsw.psp44.server.controller.filters;

import java.util.List;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

/**
 * Abstract Class that defines the model for a Filter class
 * Defines @see filter
 * Ovverrides equals method so that two objects that have the same class are the same
 */
public abstract class Filter {

    /**
     * Modifies the list based on the method implementation
     * The method implementation is "determined" by its class name
     * The method can use other parameters from the actual class
     * 
     * @param startingPosition position from which the filterage starts
     * @param positionsToFilter List of positions that needs to be filtered
     * @param gameBoard provides information about a certain position using it's getters
     */
    public abstract void filter(Position startingPosition, List<Position> positionsToFilter, Board gameBoard);

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() == obj.getClass())
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}