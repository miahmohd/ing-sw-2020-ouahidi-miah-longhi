package it.polimi.ingsw.psp44.network.communication;

import it.polimi.ingsw.psp44.util.Position;

/**
 * A descriptive class rappresenting it.polimi.ingsw.psp44.server.model.actions.Action for transmission
 */
public class Action {


    private Position target;
    private boolean build;
    private boolean move;
    private int id;
    private String description;

    /**
     * Needed for Gson serialization-deserialization
     * see https://github.com/google/gson/blob/master/UserGuide.md#writing-an-instance-creator
     */
    private Action() {

    }

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

    @Override
    public String toString() {
        return this.description;
    }

}