package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

public class SimpleBuild extends Build {


    /**
     * Build at the target position.
     *
     * @param targetPosition position where the build happens.
     */
    public SimpleBuild(Position targetPosition) {
        super(targetPosition);
    }

    /**
     * Build at the target position.
     *
     * @param targetPosition position where the build happens.
     * @param isUnbuild      if true the operation is an unbuild operation.
     */
    public SimpleBuild(Position targetPosition, boolean isUnbuild) {
        super(targetPosition, isUnbuild);
    }

    @Override
    public void execute(Board board) {
        if (this.isUnbuild)
            board.buildDown(this.targetPosition);
        else
            board.buildUp(this.targetPosition);
    }
}
