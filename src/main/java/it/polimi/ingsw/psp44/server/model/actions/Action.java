package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

/**
 * A class that represents a generic operation that changes the game board.
 */
public abstract class Action {

    protected Position targetPosition;
    protected Position sourcePosition;

    public Action(Position sourcePosition, Position targetPosition) {
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
    }

    public Action(Position target) {
        this.sourcePosition=null;
        this.targetPosition = target;
    }

    public Position getSourcePosition() {
        return sourcePosition;
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

    /**
     * Indicate the type of action
     * @return true if it is a movement action false otherwise
     */
    public abstract boolean isMovement();

    /**
     * Indicate the type of action
     * @return true if it is a movement action false otherwise
     */
    public abstract boolean isCostruction();
}

