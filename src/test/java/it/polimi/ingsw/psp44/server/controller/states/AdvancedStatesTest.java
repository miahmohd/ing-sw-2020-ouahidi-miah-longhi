package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.*;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.DomeBuild;
import it.polimi.ingsw.psp44.server.model.actions.ForceBackwardsMovement;
import it.polimi.ingsw.psp44.server.model.actions.SimpleBuild;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AdvancedStatesTest {
    private State stateUnderTest;
    private Position[] field;
    private Worker[] testWorker;
    private List<Action> allowedActionExpected;
    private List<Action> allowedActionActual;
    private FilterCollection activeBuildFilter;
    private Board boardTest;
    private GameModel gm;

    @Before
    public void setUp() {
        gm = new GameModel();
        boardTest= gm.getBoard();

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


        activeBuildFilter.add(new FilterDome());
        activeBuildFilter.add(new FilterMyWorkers());
        activeBuildFilter.add(new FilterOtherWorkers());


    }

    @Test
    public void forceBackwardsMoveState() {
        testWorker= new Worker[9];
        testWorker[0] = new Worker("p1", Worker.Sex.FEMALE);
        testWorker[1] = new Worker("p1", Worker.Sex.MALE);
        testWorker[2] = new Worker("p1", Worker.Sex.FEMALE);
        testWorker[3] = new Worker("p1", Worker.Sex.MALE);
        testWorker[4] = new Worker("p1", Worker.Sex.MALE);

        testWorker[5] = new Worker("a1", Worker.Sex.MALE);
        testWorker[6] = new Worker("a1", Worker.Sex.MALE);
        testWorker[7] = new Worker("a2", Worker.Sex.MALE);
        testWorker[8] = new Worker("a2", Worker.Sex.MALE);
        boardTest.buildDome(field[21]);
        boardTest.setWorker(field[4], testWorker[0]);
        boardTest.setWorker(field[8], testWorker[1]);
        boardTest.setWorker(field[12], testWorker[2]);
        boardTest.setWorker(field[16], testWorker[3]);
        boardTest.setWorker(field[20], testWorker[4]);
        boardTest.setWorker(field[15], testWorker[5]);
        boardTest.setWorker(field[11], testWorker[6]);
        boardTest.setWorker(field[7], testWorker[7]);
        boardTest.setWorker(field[3], testWorker[8]);
        stateUnderTest = new ForceBackwardsMoveState();

        allowedActionActual = stateUnderTest.getAvailableActions(boardTest, field[20], null, activeBuildFilter);
        assertTrue(allowedActionActual.isEmpty());

        allowedActionActual = stateUnderTest.getAvailableActions(boardTest, field[16], null, activeBuildFilter);
        allowedActionExpected.add(new ForceBackwardsMovement(field[16], field[15],field[17]));
        assertTrue(allowedActionExpected.size() == allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        gm.doAction(allowedActionActual.get(0));
        assertEquals(testWorker[5],boardTest.getWorker(field[17]));
        allowedActionActual.clear();
        allowedActionExpected.clear();

        allowedActionActual = stateUnderTest.getAvailableActions(boardTest, field[12], null, activeBuildFilter);
        allowedActionExpected.add(new ForceBackwardsMovement(field[12], field[11],field[13]));
        assertTrue(allowedActionExpected.size() == allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        gm.doAction(allowedActionActual.get(0));
        assertEquals(testWorker[6],boardTest.getWorker(field[13]));
        allowedActionActual.clear();
        allowedActionExpected.clear();

        allowedActionActual = stateUnderTest.getAvailableActions(boardTest, field[8], null, activeBuildFilter);
        allowedActionExpected.add(new ForceBackwardsMovement(field[8], field[7],field[9]));
        assertTrue(allowedActionExpected.size() == allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        gm.doAction(allowedActionActual.get(0));
        assertEquals(testWorker[7],boardTest.getWorker(field[9]));
        allowedActionActual.clear();
        allowedActionExpected.clear();

        allowedActionActual = stateUnderTest.getAvailableActions(boardTest, field[4], null, activeBuildFilter);
        assertTrue(allowedActionActual.isEmpty());

    }

    @Test
    public void femaleDomeBuildTest(){
        testWorker= new Worker[6];
        testWorker[0] = new Worker("p1", Worker.Sex.FEMALE);
        testWorker[1] = new Worker("p1", Worker.Sex.MALE);
        testWorker[2] = new Worker("p2", Worker.Sex.FEMALE);
        testWorker[3] = new Worker("p2", Worker.Sex.MALE);
        testWorker[4] = new Worker("p3", Worker.Sex.FEMALE);
        testWorker[5] = new Worker("a3", Worker.Sex.MALE);
        boardTest.setWorker(field[12], testWorker[0]);
        boardTest.setWorker(field[6], testWorker[1]);
        boardTest.setWorker(field[20], testWorker[2]);
        boardTest.setWorker(field[4], testWorker[3]);
        boardTest.setWorker(field[22], testWorker[4]);
        boardTest.setWorker(field[2], testWorker[5]);
        boardTest.buildDome(field[16]);
        boardTest.buildDome(field[8]);
        boardTest.buildDome(field[18]);
        stateUnderTest = new FemaleDomeBuildState();

        allowedActionActual = stateUnderTest.getAvailableActions(boardTest, field[6], null, activeBuildFilter);
        allowedActionExpected.add(new SimpleBuild(field[0]));
        allowedActionExpected.add(new SimpleBuild(field[1]));
        allowedActionExpected.add(new SimpleBuild(field[5]));
        allowedActionExpected.add(new SimpleBuild(field[10]));
        allowedActionExpected.add(new DomeBuild(field[7]));
        allowedActionExpected.add(new DomeBuild(field[11]));
        allowedActionExpected.add(new DomeBuild(field[13]));
        allowedActionExpected.add(new DomeBuild(field[17]));

        assertTrue(allowedActionExpected.size() == allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        allowedActionActual.clear();
        allowedActionExpected.clear();

        allowedActionActual = stateUnderTest.getAvailableActions(boardTest, field[4], null, activeBuildFilter);
        allowedActionExpected.add(new DomeBuild(field[3]));
        allowedActionExpected.add(new DomeBuild(field[15]));
        allowedActionExpected.add(new DomeBuild(field[21]));
        allowedActionExpected.add(new SimpleBuild(field[9]));

        assertTrue(allowedActionExpected.size() == allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        allowedActionActual.clear();
        allowedActionExpected.clear();

        allowedActionActual = stateUnderTest.getAvailableActions(boardTest, field[22], null, activeBuildFilter);
        allowedActionExpected.add(new SimpleBuild(field[17]));
        allowedActionExpected.add(new SimpleBuild(field[21]));
        allowedActionExpected.add(new DomeBuild(field[21]));
        allowedActionExpected.add(new DomeBuild(field[17]));
        allowedActionExpected.add(new DomeBuild(field[23]));
        assertTrue(allowedActionExpected.size() == allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        allowedActionActual.clear();
        allowedActionExpected.clear();


    }

}