package it.polimi.ingsw.psp44.network.communication;

import it.polimi.ingsw.psp44.util.Position;

public class Action {

    private final Position target;
    private final boolean build;
    private final boolean move;
    private final int id;
    private final String description;

    public Action(int id, String description, Position target, boolean build, boolean move) {
        this.id = id;
        this.description = description;
        this.target = target;
        this.build = build;
        this.move = move;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Position getTarget() {
        return target;
    }

    public boolean isBuild() {
        return build;
    }

    public boolean isMove() {
        return move;
    }

}