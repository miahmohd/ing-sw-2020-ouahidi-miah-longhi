package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.controller.filters.FilterDome;
import it.polimi.ingsw.psp44.server.controller.filters.FilterUpByTwo;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.SimpleBuild;
import it.polimi.ingsw.psp44.server.model.actions.SimpleMovement;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.exception.StateException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompoundStateTest {
    private Board boardTest;

    private CompoundState compoundStateTest;
    private SimpleMoveState moveStateTest;
    private SimpleBuildState buildStateTest;

    private List<Filter> moveFiltersTest;
    private List<Filter> buildFiltersTest;

    private List<Position> expectedBuildPosition;
    private List<Position> expectedMovePosition;

    private Position workerPosition;

    @Before
    public void setUp() {
        workerPosition= new Position(1,2);

        moveFiltersTest=new ArrayList<>();
        moveFiltersTest.add(new FilterDome());
        moveFiltersTest.add(new FilterUpByTwo());

        compoundStateTest=new CompoundState();

        buildFiltersTest=new ArrayList<>();
        buildFiltersTest.add(new FilterDome());

        moveStateTest=new SimpleMoveState();

        buildStateTest= new SimpleBuildState();


    }


    @Test
    public void addRemoveEmptyTest(){
        compoundStateTest.addState(moveStateTest);
        assertThrows(IllegalArgumentException.class, () -> compoundStateTest.addState(null));
        assertTrue(compoundStateTest.getSimpleStates().get(0) instanceof SimpleMoveState);

        assertThrows(IllegalArgumentException.class, () -> compoundStateTest.removeState(null));
        assertThrows(StateException.class, () -> compoundStateTest.removeState(buildStateTest));
        compoundStateTest.removeState(moveStateTest);
        assertTrue(compoundStateTest.getSimpleStates().isEmpty());

        compoundStateTest.addState(moveStateTest);
        compoundStateTest.addState(buildStateTest);
        compoundStateTest.empty();
        assertTrue(compoundStateTest.getSimpleStates().isEmpty());
    }





    @Test
    public void getAvailableActions() {
        boardTest = new Board();
        compoundStateTest.addState(moveStateTest);
        compoundStateTest.addState(buildStateTest);
        Position level1 = new Position(0, 1);
        Position level2 = new Position(0, 2);
        Position level3 = new Position(0, 3);
        Position dome = new Position(1, 1);
        boardTest.buildUp(level1);
        boardTest.buildUp(level2);
        boardTest.buildUp(level2);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);
        boardTest.buildDome(dome);

        Position level12 = new Position(2, 1);
        Position level22 = new Position(2, 2);
        Position level32 = new Position(2, 3);
        Position dome2 = new Position(1, 3);
        boardTest.buildUp(level12);
        boardTest.buildUp(level22);
        boardTest.buildUp(level22);
        boardTest.buildUp(level32);
        boardTest.buildUp(level32);
        boardTest.buildUp(level32);
        boardTest.buildDome(dome2);
        boardTest.buildDome(level22);
        boardTest.buildDome(level32);

        expectedMovePosition=new ArrayList<>();
        expectedMovePosition.add(level1);
        expectedMovePosition.add(level12);
        expectedBuildPosition=new ArrayList<>();
        expectedBuildPosition.add(level1);
        expectedBuildPosition.add(level2);
        expectedBuildPosition.add(level3);
        expectedBuildPosition.add(level12);

        List<Action> computedActions= compoundStateTest.getAvailableActions(boardTest,workerPosition);

        assertTrue(computedActions.size()==expectedBuildPosition.size()+expectedMovePosition.size());
        for (Action a:computedActions){
            if(a instanceof SimpleMovement)
                assertTrue(expectedMovePosition.contains(a.getTargetPosition()));
            if(a instanceof SimpleBuild)
                assertTrue(expectedBuildPosition.contains(a.getTargetPosition()));
        }
    }
}