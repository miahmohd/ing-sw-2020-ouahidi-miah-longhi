package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.util.Position;

/**
 * Representation of an action
 */
public class Action {

    /**
     * Action ID, each action has a unique id
     */
    private final int id;

    /**
     * Position to which the action is targeted
     */
    private final Position target;

    private final boolean build;
    private final boolean move;

    /**
     * Not set if the action is move
     */
    private final int level;


    /**
     * Not set if the action is move
     */
    private final boolean dome;

    public Action(int id, Position target, boolean build, boolean move, int level, boolean dome) {
        this.id = id;
        this.target = target;
        this.build = build;
        this.move = move;
        this.level = level;
        this.dome = dome;
    }

    public int getID() {
        return id;
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

    public int getLevel() {
        return level;
    }

    public boolean isDome() {
        return dome;
    }
}
