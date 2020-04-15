package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.controller.filters.FilterDome;
import it.polimi.ingsw.psp44.server.controller.filters.FilterUpByTwo;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.SimpleMovement;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleMoveStateTest {

    private Board boardTest;
    private SimpleMoveState moveStateTest;
    private List<Filter> filtersTest;
    private List<Position> expectedPosition;
    private Position workerPosition;

    @Before
    public void setUp() {
        workerPosition= new Position(1,2);
        filtersTest=new ArrayList<>();
        filtersTest.add(new FilterDome());
        filtersTest.add(new FilterUpByTwo());
        moveStateTest= new SimpleMoveState();
        boardTest = new Board();
        Position level1 = new Position(0, 1);
        Position level2 = new Position(0, 2);
        Position level3 = new Position(0, 3);
        Position dome = new Position(1, 1);
        boardTest.buildUp(level1);
        boardTest.buildUp(level2);
        boardTest.buildUp(level2);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);
        boardTest.buildDome(dome);
        expectedPosition=new ArrayList<>();
        expectedPosition.add(new Position(0,1));
        expectedPosition.add(new Position(1,3));
        expectedPosition.add(new Position(2,1));
        expectedPosition.add(new Position(2,2));
        expectedPosition.add(new Position(2,3));
    }



    @Test
    public void getAvailableActions() {
        List<Position> computedPositions=new ArrayList<>();
        for(Action a: moveStateTest.getAvailableActions(boardTest,workerPosition)){
            assertTrue(a instanceof SimpleMovement);
            computedPositions.add(a.getTargetPosition());
        }
        assertTrue(computedPositions.containsAll(expectedPosition)&& computedPositions.size()==expectedPosition.size());


    }
}