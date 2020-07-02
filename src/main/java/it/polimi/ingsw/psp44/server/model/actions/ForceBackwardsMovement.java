package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.ModelCodes;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForceBackwardsMovement extends Movement {
    private final Position forcedPosition;

    /**
     * Create a movement that force a
     * neighboring opponent Worker
     * to the space directly on the other side of your
     * Worker
     *
     * @param sourcePosition position of first worker
     * @param targetPosition position of the opponents worker
     */
    public ForceBackwardsMovement(Position sourcePosition, Position targetPosition, Position forcedPosition) {
        super(sourcePosition, targetPosition, R.getAppProperties().get(ModelCodes.FORCE_BACKWARDS_MOVEMENT_DESCRIPTION));
        this.forcedPosition = forcedPosition;
    }

    @Override
    public void execute(Board board) {

        Worker targetWorker = board.getWorker(this.targetPosition);
        board.setWorker(this.targetPosition, null);
        board.setWorker(this.forcedPosition, targetWorker);
    }

    @Override
    public List<Position> getModifiedPositions() {
        return new ArrayList<>(Arrays.asList(
                this.forcedPosition,
                this.targetPosition
        ));
    }
}
