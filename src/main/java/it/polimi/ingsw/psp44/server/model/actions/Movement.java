package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.util.Position;

public abstract class Movement extends Action {

    protected Position sourcePosition;

    public Movement(Position sourcePosition, Position targetPosition) {
        super(targetPosition);
        this.sourcePosition = sourcePosition;
    }
}
