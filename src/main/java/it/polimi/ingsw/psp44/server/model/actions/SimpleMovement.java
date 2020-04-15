package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SimpleMovement extends Action {


    /**
     * Create a simple movement for a worker
     *
     * @param sourcePosition initial position of the worker
     * @param targetPosition final position of the worker
     */
    public SimpleMovement(Position sourcePosition, Position targetPosition) {
        super(sourcePosition, targetPosition);
    }

    @Override
    public void execute(Board board) {
        board.setWorker(this.targetPosition, board.getWorker(this.sourcePosition));
        board.setWorker(this.sourcePosition, null);
    }

    @Override
    public List<Position> getModifiedPositions() {
        return new ArrayList<>(Arrays.asList(
                this.sourcePosition,
                this.targetPosition
        ));
    }
}
