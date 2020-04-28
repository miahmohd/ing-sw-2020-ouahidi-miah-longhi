package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class InitialPlacementTest {

    @Test
    public void execute() {
    }

    @Test
    public void getModifiedPositions() {
        Board boardTest=new Board();
        Worker w00 =new Worker("ply0", Worker.Sex.MALE);
        Worker w10 =new Worker("ply1", Worker.Sex.MALE);
        Position positionW00 = new Position(0, 2);
        Position positionW10 = new Position(0, 4);
        Action initPos00= new InitialPlacement(positionW00,w00);
        Action initPos10= new InitialPlacement(positionW10,w10);

        initPos00.execute(boardTest);
        initPos10.execute(boardTest);

        assertTrue(boardTest.isWorker(positionW00));
        assertTrue(boardTest.isWorker(positionW10));
        assertTrue(initPos00.getModifiedPositions().size()==1);
        assertTrue(initPos10.getModifiedPositions().size()==1);
        assertEquals(initPos00.getModifiedPositions().get(0),positionW00);
        assertEquals(initPos10.getModifiedPositions().get(0),positionW10);
    }
}