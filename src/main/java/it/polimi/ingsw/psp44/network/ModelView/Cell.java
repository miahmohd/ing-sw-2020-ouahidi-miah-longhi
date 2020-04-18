package it.polimi.ingsw.psp44.network.ModelView;

import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;

public class Cell {

    /**
     * Where the cell is positioned
     */
    private Position target;

    
    private int level;
    private boolean dome;

    /**
     * FIXME: maybe redundunt
    */
    private boolean worker;




    /**
     * sex of worker if present
     */
    private Worker.Sex sex;

    /**
     * player nickname if the workers is present in the cell
     */
    private String playerNickname;


    
}