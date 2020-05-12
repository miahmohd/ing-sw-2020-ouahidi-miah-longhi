package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.ModelCodes;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitialPlacement extends Movement {

    private Worker worker;

    /**
     * Create the initial placement for the worker.
     *
     * @param targetPosition the position where to place the worker.
     * @param worker         the worker to place.
     */
    public InitialPlacement(Position targetPosition, Worker worker) {
        super(null, targetPosition, R.getAppProperties().get(ModelCodes.INITIAL_PLACEMENT_DESCRIPTION));
        this.worker = worker;
    }

    @Override
    public void execute(Board board) {
        board.setWorker(this.targetPosition, worker);
    }

    @Override
    public List<Position> getModifiedPositions() {
        return new ArrayList<>(Arrays.asList(
                this.targetPosition
        ));
    }
}
