package it.polimi.ingsw.psp44.server.controller.VictoryCondition;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.SimpleMovement;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseVictoryConditionTest {

    @Test
    public void check() {
        Board testBoard =new Board();
        Position level1 = new Position(0,0);
        Position level2 = new Position(0,1);
        Position level3 = new Position(0,2);
        testBoard.buildUp(level1);
        testBoard.buildUp(level2);
        testBoard.buildUp(level2);
        testBoard.buildUp(level3);
        testBoard.buildUp(level3);
        testBoard.buildUp(level3);
        BaseVictoryCondition condition=new BaseVictoryCondition();
        //1 --> 2
        SimpleMovement up1=new SimpleMovement(level1,level2);
        assertFalse(condition.check(up1,testBoard));
        //2 --> 3
        SimpleMovement up2=new SimpleMovement(level2,level3);
        assertTrue(condition.check(up2,testBoard));
        //3 --> 3
        SimpleMovement up3=new SimpleMovement(level3,level3);
        assertFalse(condition.check(up3,testBoard));
    }
}