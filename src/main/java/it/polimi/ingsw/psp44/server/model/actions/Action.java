package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

/**
 * A class that represents a generic operation that changes the game board.
 */
public abstract class Action {

    protected Position targetPosition;


    public Action(Position target) {
        this.targetPosition = target;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    /**
     * Execute the action and apply the changes to the board
     *
     * @param board the board to change
     */
    public abstract void execute(Board board);

    /**
     * @return the list of positions, on the board, involved in the action
     */
    public abstract List<Position> getModifiedPositions();

}

