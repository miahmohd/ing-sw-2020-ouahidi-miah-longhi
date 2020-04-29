package it.polimi.ingsw.psp44.server.controller.VictoryCondition;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.SimpleMovement;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Test;

import static org.junit.Assert.*;

public class DownByTwoVictoryConditionTest {

    @Test
    public void check() {
        Board testBoard =new Board();
        Position level0 = new Position(1,0);
        Position level1 = new Position(0,0);
        Position level2 = new Position(0,1);
        Position level3 = new Position(0,2);
        testBoard.buildUp(level1);
        testBoard.buildUp(level2);
        testBoard.buildUp(level2);
        testBoard.buildUp(level3);
        testBoard.buildUp(level3);
        testBoard.buildUp(level3);
        DownByTwoVictoryCondition condition=new DownByTwoVictoryCondition();
        //1 -->0
        SimpleMovement dw1=new SimpleMovement(level1,level0);
        assertFalse(condition.check(dw1,testBoard));
        //2 -->0
        SimpleMovement dw2=new SimpleMovement(level2,level0);
        assertTrue(condition.check(dw2,testBoard));
        //2 -->1
        dw1=new SimpleMovement(level2,level1);
        assertFalse(condition.check(dw1,testBoard));
        //3-->2
        dw1=new SimpleMovement(level3,level2);
        assertFalse(condition.check(dw1,testBoard));
        //3-->1
        dw2=new SimpleMovement(level3,level1);
        assertTrue(condition.check(dw2,testBoard));
        //3-->0
        SimpleMovement dw3=new SimpleMovement(level3,level0);
        assertTrue(condition.check(dw3,testBoard));

    }
}