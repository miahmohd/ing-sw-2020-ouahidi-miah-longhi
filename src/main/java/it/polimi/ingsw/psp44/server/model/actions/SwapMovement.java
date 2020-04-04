package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwapMovement extends Movement {

    /**
     * Create a movement that swap worker in target with th worker in source
     *
     * @param sourcePosition position of first worker
     * @param targetPosition position of the second worker
     */
    public SwapMovement(Position sourcePosition, Position targetPosition) {
        super(sourcePosition, targetPosition);
    }

    @Override
    public void execute(Board board) {
        Worker sourceWorker = board.getWorker(this.sourcePosition);
        board.setWorker(this.sourcePosition, board.getWorker(this.targetPosition));
        board.setWorker(this.targetPosition, sourceWorker);
    }

    @Override
    public List<Position> getModifiedPositions() {
        return new ArrayList<>(Arrays.asList(
                this.sourcePosition,
                this.targetPosition
        ));
    }
}
