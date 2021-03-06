package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.ModelCodes;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SimpleMovement extends Movement {


    /**
     * Create a simple movement for a worker
     *
     * @param sourcePosition initial position of the worker
     * @param targetPosition final position of the worker
     */
    public SimpleMovement(Position sourcePosition, Position targetPosition) {
        super(sourcePosition, targetPosition, R.getAppProperties().get(ModelCodes.SIMPLE_MOVEMENT_DESCRIPTION));
    }

    @Override
    public void execute(Board board) {
        super.execute(board);
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
