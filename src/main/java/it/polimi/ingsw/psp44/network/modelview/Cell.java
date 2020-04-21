package it.polimi.ingsw.psp44.network.modelview;

import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.Worker.Sex;
import it.polimi.ingsw.psp44.util.Position;

public class Cell {

    /**
     * Where the cell is positioned
     */
    private Position target;

    private int level;
    private boolean dome;

    /**
     * sex of worker if present
     */
    private Worker.Sex sex;

    /**
     * player nickname if the workers is present in the cell
     */
    private String playerNickname;

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

    public Cell(Position target, int level, boolean dome, Sex sex, String playerNickname) {
        this.target = target;
        this.level = level;
        this.dome = dome;
        this.sex = sex;
        this.playerNickname = playerNickname;
    }


    public Cell(){
        //FIXME: Just a proof of concept
        this(null, 0, false, null, null);
    }

	public Position getPosition() {
		return target;
	}
    
}














/*
* ======
* G1FL2|
* ======
*
* */































