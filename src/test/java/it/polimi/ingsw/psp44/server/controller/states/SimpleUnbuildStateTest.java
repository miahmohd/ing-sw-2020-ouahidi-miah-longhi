package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.*;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.SimpleBuild;
import it.polimi.ingsw.psp44.server.model.actions.SimpleMovement;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleUnbuildStateTest {
    private State stateUnderTest;
    private Position[] field;
    private List<Action> allowedActionExpected;
    private List<Action> allowedActionActual;
    private FilterCollection activeBuildFilter;
    private Board boardTest;
    private GameModel gm;

    @Before
    public void setUp() {
        gm = new GameModel();
        boardTest= gm.getBoard();

        Worker testWorker0 = new Worker("p1", Worker.Sex.FEMALE);
        Worker testWorker1 = new Worker("p1", Worker.Sex.MALE);
        Worker testWorker2 = new Worker("a1", Worker.Sex.FEMALE);
        Worker testWorker3 = new Worker("a1", Worker.Sex.MALE);

        field = new Position[25];
        activeBuildFilter = new FilterCollection();
        allowedActionExpected = new ArrayList<>();
        allowedActionActual = new ArrayList<>();
        for (int i = 0, level = 0; i < field.length; ++i) {
            field[i] = new Position(i / 5, i % 5);
            for (int z = 0; z < level; ++z) {
                boardTest.buildUp(field[i]);
            }
            level = (level + 1) % 4;
        }
        boardTest.buildDome(field[17]);
        boardTest.setWorker(field[11], testWorker0);
        boardTest.setWorker(field[12], testWorker1);
        boardTest.setWorker(field[6], testWorker2);
        boardTest.setWorker(field[0], testWorker3);

        activeBuildFilter.add(new FilterDome());
        activeBuildFilter.add(new FilterMyWorkers());
        activeBuildFilter.add(new FilterOtherWorkers());
        activeBuildFilter.add(new FilterUpByTwo());


    }

    @Test
    public void getAvailableActions() {
        stateUnderTest = new SimpleUnbuildState();

        allowedActionActual = stateUnderTest.getAvailableActions(boardTest, field[11], null, activeBuildFilter);
        allowedActionExpected.add(new SimpleBuild( field[7],true));
        allowedActionExpected.add(new SimpleBuild( field[18],true));
        allowedActionExpected.add(new SimpleBuild( field[13],true));
        assertTrue(allowedActionExpected.size() == allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        for (Action a:allowedActionExpected)
            gm.doAction(a);
        assertEquals(2,boardTest.getLevel(field[7]));
        assertEquals(1,boardTest.getLevel(field[18]));
        assertEquals(0,boardTest.getLevel(field[13]));



    }
}