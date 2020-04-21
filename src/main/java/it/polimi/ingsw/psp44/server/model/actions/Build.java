package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Build extends Action {

    protected boolean isUnbuild;

    public Build(Position target) {
        super(target);
        this.isUnbuild = false;
    }

    public Build(Position targetPosition, boolean isUnbuild) {
        super(targetPosition);
        this.isUnbuild = isUnbuild;
    }

    /**
     * Indicate the type of action
     *
     * @return true if it is a movement action false otherwise
     */
    @Override
    public boolean isMovement() {
        return false;
    }

    /**
     * Indicate the type of action
     *
     * @return true if it is a movement action false otherwise
     */
    @Override
    public boolean isBuild() {
        return true;
    }

    @Override
    public List<Position> getModifiedPositions() {
        return new ArrayList<>(Arrays.asList(this.targetPosition));
    }
}
