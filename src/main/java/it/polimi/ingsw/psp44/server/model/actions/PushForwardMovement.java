package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PushForwardMovement extends Movement {

    private final Position forwardPosition;

    /**
     * Create e movement where the worker in target position is pushed one position straight backwards.
     *
     * @param sourcePosition initial position of the worker to move
     * @param targetPosition final position of the worker to move, and initial position of the worker to push
     */
    public PushForwardMovement(Position sourcePosition, Position targetPosition, Position forwardPosition) {
        super(sourcePosition, targetPosition);
        this.forwardPosition = forwardPosition;
    }

    @Override
    public void execute(Board board) {
        board.setWorker(this.forwardPosition, board.getWorker(this.targetPosition));
        board.setWorker(this.targetPosition, board.getWorker(this.sourcePosition));
        board.setWorker(this.sourcePosition, null);
    }

    @Override
    public List<Position> getModifiedPositions() {
        return new ArrayList<>(Arrays.asList(
                this.sourcePosition,
                this.targetPosition,
                this.forwardPosition
        ));
    }
}
