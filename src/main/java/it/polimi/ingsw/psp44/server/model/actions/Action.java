package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;
import java.util.Objects;

/**
 * A class that represents a generic operation that changes the game board.
 */
public abstract class Action {

    protected Position targetPosition;
    protected Position sourcePosition;
    private String description;

    protected Action(Position target, String description) {
        this(null, target, description);

    }

    protected Action(Position source, Position target, String description) {
        this.sourcePosition = source;
        this.targetPosition = target;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;
        Action action = (Action) o;
        return Objects.equals(getTargetPosition(), action.getTargetPosition()) &&
                Objects.equals(getSourcePosition(), action.getSourcePosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTargetPosition(), getSourcePosition());
    }

    public Position getSourcePosition() {
        return sourcePosition;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Execute the action and apply the changes to the board
     *
     * @param board the board to change
     */
    public abstract void execute(Board board);

    /**
     * @return the list of positions, on the board, involved in the action
     */
    public abstract List<Position> getModifiedPositions();

    /**
     * Indicate the type of action
     *
     * @return true if it is a movement action false otherwise
     */
    public abstract boolean isMovement();

    /**
     * Indicate the type of action
     *
     * @return true if it is a movement action false otherwise
     */
    public abstract boolean isBuild();
}

