package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

public class DomeBuild extends Build {

    /**
     * Build a dome at the target position.
     *
     * @param targetPosition position where the dome is built.
     */
    public DomeBuild(Position targetPosition) {
        super(targetPosition);
    }

    /**
     * Build a dome at the target position.
     *
     * @param targetPosition position where the dome is built.
     * @param isUnbuild      if true the operation is an unbuild operation.
     */
    public DomeBuild(Position targetPosition, boolean isUnbuild) {
        super(targetPosition, isUnbuild);
    }

    @Override
    public void execute(Board board) {
        if (this.isUnbuild)
            board.removeDome(this.targetPosition);
        else
            board.buildDome(this.targetPosition);
    }


}
