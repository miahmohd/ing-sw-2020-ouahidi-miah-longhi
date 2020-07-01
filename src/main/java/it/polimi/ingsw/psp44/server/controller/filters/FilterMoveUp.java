package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

public class FilterMoveUp extends DynamicFilter {
    /**
     * Modifies the list based on the method implementation
     * The method implementation is "determined" by its class name
     * The method can use other parameters from the actual class
     *
     * @param startingPosition  position from which the filterage starts
     * @param positionsToFilter List of positions that needs to be filtered
     * @param gameBoard         provides information about a certain position using it's getters
     */
    @Override
    public void filter(Position startingPosition, List<Position> positionsToFilter, Board gameBoard) {
        if (active)
            positionsToFilter.removeIf((p) -> gameBoard.getLevel(p) - gameBoard.getLevel(startingPosition) > 0);
        active = false||this.external;
    }
}
