package it.polimi.ingsw.psp44.network.ModelView;

import it.polimi.ingsw.psp44.util.Position;

public class Action {

    private Position target;
    private boolean build;
    private boolean move;

    /**
     * Not set if the action is move
     */
    private int level;

    
    /**
     * Not set if the action is move
     */
    private boolean dome;

    public Action(Position target, boolean build, boolean move, int level, boolean dome) {
        this.target = target;
        this.build = build;
        this.move = move;
        this.level = level;
        this.dome = dome;
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