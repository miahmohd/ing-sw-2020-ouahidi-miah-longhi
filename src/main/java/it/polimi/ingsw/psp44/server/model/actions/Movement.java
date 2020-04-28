package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

public abstract class Movement extends Action {

    public Movement(Position sourcePosition, Position targetPosition) {

        super(sourcePosition, targetPosition);
    }

    /**
     * Execute the action and apply the changes to the board
     *
     * @param board the board to change
     */
    @Override
    public void execute(Board board) {
        board.setSelectedWorker(this.targetPosition);
    }

    /**
     * Indicate the type of action
     *
     * @return true if it is a movement action false otherwise
     */
    @Override
    public boolean isMovement() {
        return true;
    }

    /**
     * Indicate the type of action
     *
     * @return true if it is a movement action false otherwise
     */
    @Override
    public boolean isBuild() {
        return false;
    }
}
