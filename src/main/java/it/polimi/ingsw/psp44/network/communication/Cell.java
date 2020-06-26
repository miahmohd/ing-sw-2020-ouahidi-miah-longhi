package it.polimi.ingsw.psp44.network.communication;


import it.polimi.ingsw.psp44.server.model.Worker.Sex;
import it.polimi.ingsw.psp44.util.Position;


public class Cell {

    /**
     * Where the cell is positioned
     */
    private final Position target;

    private final int level;
    private final boolean dome;

    /**
     * sex of worker if present
     */
    private final Sex sex;

    /**
     * player nickname if the workers is present in the cell
     */
    private final String playerNickname;

    public Cell(Position target, int level, boolean dome, Sex sex, String playerNickname) {
        this.target = target;
        this.level = level;
        this.dome = dome;
        this.sex = sex;
        this.playerNickname = playerNickname;
    }


    /**
     * Initializes an empty Cell
     */
    public Cell() {
        this(null, 0, false, null, null);
    }

    public int getLevel() {
        return this.level;
    }

    public boolean isEmpty() {
        return playerNickname == null || playerNickname.isEmpty();
    }

    public boolean isDome() {
        return dome;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public Position getPosition() {
        return target;
    }

    public Sex getSex(){
        return this.sex;
    }

}














/*
 * ======
 * G1FL2|
 * ======
 *
 * */































