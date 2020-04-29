package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.server.model.Worker;


public class Cell {


    private final int level;
    private final boolean dome;

    /**
     * player nickname if the workers is present in the cell
     */
    private final String playerNickname;

    /**
     * sex of worker if present
     */
    private final Worker.Sex sex;


    public Cell(int level, boolean dome, Worker.Sex sex, String playerNickname) {
        this.level = level;
        this.dome = dome;
        this.sex = sex;
        this.playerNickname = playerNickname;
    }

    public Cell() {
        this(0, false, null, null);
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
}
