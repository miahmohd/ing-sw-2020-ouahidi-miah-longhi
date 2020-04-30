package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.*;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.DomeBuild;
import it.polimi.ingsw.psp44.server.model.actions.SimpleBuild;
import it.polimi.ingsw.psp44.server.model.actions.SimpleMovement;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompoundStateTest {

    private GameModel gmActual;
    private GameModel gmExpected;
    private Board boardTestExpected;
    private Board boardTestActual;
    private Position[] field;
    private FilterCollection activeMoveFilter;
    private List<Action> allowedActionExpected;
    private List<Action> allowedActionActual;
    private FilterCollection activeBuildFilter;
    private CompoundState stateUnderTest;

    @Before
    public void setUp() {
        gmActual = new GameModel();
        gmExpected = new GameModel();
        boardTestExpected = gmExpected.getBoard();
        boardTestActual = gmActual.getBoard();

        Worker testWorker0 = new Worker("p1", Worker.Sex.FEMALE);
        Worker testWorker2 = new Worker("p1", Worker.Sex.FEMALE);
        Worker testWorker4 = new Worker("p1", Worker.Sex.FEMALE);
        Worker testWorker5 = new Worker("a1", Worker.Sex.FEMALE);
        Worker testWorker6 = new Worker("a1", Worker.Sex.FEMALE);
        Worker testWorker7 = new Worker("a1", Worker.Sex.FEMALE);

        field = new Position[25];
        activeMoveFilter = new FilterCollection();
        activeBuildFilter = new FilterCollection();
        allowedActionExpected = new ArrayList<>();
        allowedActionActual = new ArrayList<>();
        for (int i = 0, level = 0; i < field.length; ++i) {
            field[i] = new Position(i / 5, i % 5);
            for (int z = 0; z < level; ++z) {
                boardTestExpected.buildUp(field[i]);
                boardTestActual.buildUp(field[i]);
            }
            level = (level + 1) % 4;
        }
        boardTestActual.buildUp(field[0]);
        boardTestActual.buildUp(field[0]);
        boardTestActual.buildUp(field[0]);
        boardTestActual.buildDome(field[8]);
        boardTestActual.buildDome(field[15]);
        boardTestActual.buildDome(field[18]);
        boardTestActual.setWorker(field[0], testWorker0);
        boardTestActual.setWorker(field[12], testWorker2);
        boardTestActual.setWorker(field[24], testWorker4);
        boardTestActual.setWorker(field[6], testWorker5);
        boardTestActual.setWorker(field[19], testWorker6);
        boardTestActual.setWorker(field[16], testWorker7);

        boardTestExpected.buildUp(field[0]);
        boardTestExpected.buildUp(field[0]);
        boardTestExpected.buildUp(field[0]);
        boardTestExpected.buildDome(field[8]);
        boardTestExpected.buildDome(field[15]);
        boardTestExpected.buildDome(field[18]);
        boardTestExpected.setWorker(field[0], testWorker0);
        boardTestExpected.setWorker(field[12], testWorker2);
        boardTestExpected.setWorker(field[24], testWorker4);
        boardTestExpected.setWorker(field[6], testWorker5);
        boardTestExpected.setWorker(field[19], testWorker6);
        boardTestExpected.setWorker(field[16], testWorker7);

        activeBuildFilter.add(new FilterDome());
        activeBuildFilter.add(new FilterMyWorkers());
        activeBuildFilter.add(new FilterOtherWorkers());
        activeMoveFilter.add(new FilterDome());
        activeMoveFilter.add(new FilterMyWorkers());
        activeMoveFilter.add(new FilterOtherWorkers());

        assertEquals(activeBuildFilter, activeMoveFilter);
        activeMoveFilter.add(new FilterUpByTwo());

    }

    @Test
    public void getAvailableActions() {
        stateUnderTest = new CompoundState();
        stateUnderTest.addState(new SimpleMoveState());
        stateUnderTest.addState(new SimpleBuildState());

        DynamicFilter f1 = new FilterLastMovePosition();
        if (!activeMoveFilter.contains(f1))
            activeMoveFilter.add(f1);

        //0,0

        activeMoveFilter.update(f1, new SimpleMovement(field[5], field[0]), boardTestActual);
        allowedActionActual = stateUnderTest.getAvailableActions(boardTestActual, field[0], activeMoveFilter, activeBuildFilter);
        allowedActionExpected.add(new SimpleMovement(field[0], field[1]));
        allowedActionExpected.add(new SimpleBuild(field[1]));
        allowedActionExpected.add(new SimpleBuild(field[5]));
        assertTrue(allowedActionExpected.size() == allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        assertFalse(f1.isActive());

        //2,2
        stateUnderTest.removeState(new SimpleBuildState());
        assertTrue(stateUnderTest.getSimpleStates().size() == 1);


        activeMoveFilter.update(f1, new SimpleMovement(field[17], field[12]), boardTestActual);
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[12], activeMoveFilter, activeBuildFilter));
        allowedActionExpected.add(new SimpleMovement(field[12], field[13]));
        assertTrue(allowedActionExpected.size() == allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));


        //4,4
        stateUnderTest = new CompoundState(new SimpleBuildState(), new SimpleMoveState());
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[24], activeMoveFilter, activeBuildFilter));
        allowedActionExpected.add(new DomeBuild(field[23]));
        assertTrue(allowedActionExpected.size() == allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));

        for (Action a : allowedActionExpected)
            gmExpected.doAction(a);
        for (Action a : allowedActionActual)
            gmActual.doAction(a);
        for (Position p : field) {
            assertEquals(boardTestExpected.getLevel(p), boardTestActual.getLevel(p));
            assertEquals(boardTestExpected.isDome(p), boardTestActual.isDome(p));
            assertEquals(boardTestActual.isWorker(p), boardTestExpected.isWorker(p));
        }


    }
}