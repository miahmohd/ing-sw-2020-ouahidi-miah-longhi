package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.ModelCodes;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.R;

public class DomeBuild extends Build {

    /**
     * Build a dome at the target position.
     *
     * @param targetPosition position where the dome is built.
     */
    public DomeBuild(Position targetPosition) {
        super(targetPosition, R.getAppProperties().get(ModelCodes.DOME_BUILD_DESCRIPTION));
    }

    /**
     * Build a dome at the target position.
     *
     * @param targetPosition position where the dome is built.
     * @param isUnbuild      if true the operation is an unbuild operation.
     */
    public DomeBuild(Position targetPosition, boolean isUnbuild) {
        super(targetPosition, isUnbuild, R.getAppProperties().get(ModelCodes.DOME_BUILD_DESCRIPTION));
    }

    @Override
    public void execute(Board board) {
        if (this.isUnbuild)
            board.removeDome(this.targetPosition);
        else
            board.buildDome(this.targetPosition);

    }


}
