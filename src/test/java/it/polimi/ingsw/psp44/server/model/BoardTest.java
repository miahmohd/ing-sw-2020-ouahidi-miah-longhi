package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardTest {
    private Board boardTest;
    @Before
    public void setUp(){
        boardTest=new Board();

    }

    @Test
    public void buildUp() {
        Position level0=new Position(0,0);
        Position level1=new Position(0,1);
        Position level2=new Position(0,2);
        Position level3=new Position(0,3);
        boardTest.buildUp(level1);
        boardTest.buildUp(level2);
        boardTest.buildUp(level2);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);

        assertEquals(0, boardTest.getLevel(level0));
        assertEquals(1, boardTest.getLevel(level1));
        assertEquals(2, boardTest.getLevel(level2));
        assertEquals(3, boardTest.getLevel(level3));

    }

    @Test
    public void buildDown() {
    }

    @Test
    public void buildDome() {
    }

    @Test
    public void removeDome() {

    }

    @Test
    public void getNeighbouringPositions() {
    }

    @Test
    public void getPlayerWorkersPositions() {
    }
}