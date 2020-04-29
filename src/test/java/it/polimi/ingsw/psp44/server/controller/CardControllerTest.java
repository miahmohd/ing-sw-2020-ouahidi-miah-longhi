package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.VictoryCondition.BaseVictoryCondition;
import it.polimi.ingsw.psp44.server.controller.VictoryCondition.VictoryCondition;
import it.polimi.ingsw.psp44.server.controller.filters.*;
import it.polimi.ingsw.psp44.server.controller.states.*;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CardControllerTest {
    private CardController controller;
    private Board boardTest;
    private GameModel gm;
    private Position[] field;
    private List<Transition> transitionList;
    private List<VictoryCondition> victoryConditions;

    @Before
    public void setUp() {
        FilterCollection activeBuildFilter = new FilterCollection();
        FilterCollection activeMoveFilter = new FilterCollection();

        activeBuildFilter.add(new FilterDome());
        activeBuildFilter.add(new FilterMyWorkers());
        activeBuildFilter.add(new FilterOtherWorkers());
        activeBuildFilter.add(new FilterOnlyLastPosition());
        activeBuildFilter.add(new FilterLastMovePosition());

        activeMoveFilter.add(new FilterDome());
        activeMoveFilter.add(new FilterMyWorkers());
        activeMoveFilter.add(new FilterOtherWorkers());
        activeMoveFilter.add(new FilterUpByTwo());
        activeMoveFilter.add(new FilterLastMovePosition());
        activeMoveFilter.add(new FilterLastBuildPosition());


        State initialState = new SimpleMoveState();
        initialState.setInitialState(true);
        State finalState = new SecondBuildState();
        finalState.setFinalState(true);
        State moveBuild = new CompoundState(new SimpleMoveState(), new SimpleBuildState());
        transitionList = new ArrayList<>();
        //0 -MOVE->5
        transitionList.add(new Transition(initialState, moveBuild, null, null, null));
        // 5 -BUILD-> 1
        transitionList.add(new Transition(moveBuild, new SimpleMoveState(), Transition.Condition.BUILD, null, Arrays.asList(new FilterLastBuildPosition())));
        transitionList.add(new Transition(moveBuild, new SimpleMoveState(), Transition.Condition.MOVE, null, null));
        //5 -MOVE-> 10
        transitionList.add(new Transition(new SimpleMoveState(), moveBuild, Transition.Condition.MOVE, Arrays.asList(new FilterLastMovePosition()), Arrays.asList(new FilterLastMovePosition())));
        //10 -MOVE-> 11
        transitionList.add(new Transition(moveBuild, new SimpleMoveState(), Transition.Condition.MOVE, null, null));
        //11 -MOVE->17
        transitionList.add(new Transition(new SimpleMoveState(), new SimpleBuildState(), null, null, null));
        // 17 -BUILD->13
        transitionList.add(new Transition(new SimpleBuildState(), finalState, null, Arrays.asList(new FilterOnlyLastPosition()), null));
        // LB
        transitionList.add(new Transition(finalState, initialState, null, Arrays.asList(new FilterOnlyLastPosition()), null));

        victoryConditions = Arrays.asList(new BaseVictoryCondition());
        controller = new CardController(transitionList, victoryConditions, null, activeBuildFilter, activeMoveFilter);
        gm = new GameModel();
        boardTest = gm.getBoard();

        Worker testWorker0 = new Worker("p1", Worker.Sex.FEMALE);
        Worker testWorker2 = new Worker("p1", Worker.Sex.FEMALE);
        Worker testWorker4 = new Worker("p1", Worker.Sex.FEMALE);
        Worker testWorker5 = new Worker("a1", Worker.Sex.FEMALE);
        Worker testWorker6 = new Worker("a1", Worker.Sex.FEMALE);
        Worker testWorker7 = new Worker("a1", Worker.Sex.FEMALE);

        field = new Position[25];

        for (int i = 0, level = 0; i < field.length; ++i) {
            field[i] = new Position(i / 5, i % 5);
            for (int z = 0; z < level; ++z) {
                boardTest.buildUp(field[i]);
            }
            level = (level + 1) % 4;
        }
        boardTest.buildDome(field[8]);
        boardTest.buildDome(field[15]);
        boardTest.buildDome(field[18]);
        boardTest.buildDome(field[23]);
        boardTest.setWorker(field[0], testWorker0);
        boardTest.setWorker(field[12], testWorker2);
        boardTest.setWorker(field[24], testWorker4);
        boardTest.setWorker(field[6], testWorker5);
        boardTest.setWorker(field[19], testWorker6);
        boardTest.setWorker(field[16], testWorker7);
    }

    @Test
    public void cardControllerTest() {
        Action lastaction;
        List<Action> availableActionExpected;
        List<Action> availableActionActual;

        //0 -MOVE->5
        lastaction = new SimpleMovement(field[0], field[5]);
        gm.applyAction(lastaction);

        assertFalse(controller.checkVictory(lastaction, boardTest));
        assertTrue(controller.nextState(lastaction, boardTest));
        assertFalse(controller.isEndableTurn());
        availableActionExpected = Arrays.asList(
                new SimpleMovement(field[5], field[0]),
                new SimpleMovement(field[5], field[1]),
                new SimpleMovement(field[5], field[10]),
                new SimpleBuild(field[0]),
                new SimpleBuild(field[1]),
                new SimpleBuild(field[10]),
                new DomeBuild(field[11]));
        availableActionActual = controller.getAvailableAction(boardTest, field[5]);
        assertTrue(availableActionActual.containsAll(availableActionExpected));
        assertTrue(availableActionActual.size() == availableActionExpected.size());
        transitionList.remove(0);

        // 5 -BUILD-> 1
        lastaction = new SimpleBuild(field[1]);
        gm.applyAction(lastaction);

        assertFalse(controller.checkVictory(lastaction, boardTest));
        assertTrue(controller.nextState(lastaction, boardTest));
        assertFalse(controller.isEndableTurn());
        availableActionExpected = Arrays.asList(
                new SimpleMovement(field[5], field[0]),
                new SimpleMovement(field[5], field[10]));
        availableActionActual = controller.getAvailableAction(boardTest, field[5]);
        assertTrue(availableActionActual.containsAll(availableActionExpected));
        assertTrue(availableActionActual.size() == availableActionExpected.size());
        transitionList.remove(0);
        transitionList.remove(0);
        //5 -MOVE-> 10
        lastaction = new SimpleMovement(field[5], field[10]);
        gm.applyAction(lastaction);

        assertFalse(controller.checkVictory(lastaction, boardTest));
        assertTrue(controller.nextState(lastaction, boardTest));
        assertFalse(controller.isEndableTurn());
        availableActionExpected = Arrays.asList(
                new SimpleMovement(field[10], field[11]),
                new DomeBuild(field[11]));
        availableActionActual = controller.getAvailableAction(boardTest, field[10]);
        assertTrue(availableActionActual.containsAll(availableActionExpected));
        assertTrue(availableActionActual.size() == availableActionExpected.size());
        transitionList.remove(0);
        //10 -MOVE-> 11
        lastaction = new SimpleMovement(field[10], field[11]);
        gm.applyAction(lastaction);
        assertTrue(controller.checkVictory(lastaction, boardTest));
        assertTrue(controller.nextState(lastaction, boardTest));
        assertFalse(controller.isEndableTurn());
        availableActionExpected = Arrays.asList(
                new SimpleMovement(field[11], field[5]),
                new SimpleMovement(field[11], field[7]),
                new SimpleMovement(field[11], field[10]),
                new SimpleMovement(field[11], field[17]));
        availableActionActual = controller.getAvailableAction(boardTest, field[11]);
        assertTrue(availableActionActual.containsAll(availableActionExpected));
        assertTrue(availableActionActual.size() == availableActionExpected.size());
        transitionList.remove(0);
        //11 -MOVE-> 17
        lastaction = new SimpleMovement(field[11], field[17]);
        gm.applyAction(lastaction);
        assertFalse(controller.checkVictory(lastaction, boardTest));
        assertTrue(controller.nextState(lastaction, boardTest));
        assertFalse(controller.isEndableTurn());
        availableActionExpected = Arrays.asList(
                new DomeBuild(field[11]),
                new SimpleBuild(field[13]),
                new SimpleBuild(field[21]),
                new SimpleBuild(field[22]));
        availableActionActual = controller.getAvailableAction(boardTest, field[17]);
        assertTrue(availableActionActual.containsAll(availableActionExpected));
        assertTrue(availableActionActual.size() == availableActionExpected.size());
        transitionList.remove(0);
        //17 -BUILD-> 13
        lastaction = new SimpleBuild(field[13]);
        gm.applyAction(lastaction);

        assertFalse(controller.checkVictory(lastaction, boardTest));
        assertTrue(controller.nextState(lastaction, boardTest));
        assertTrue(controller.isEndableTurn());
        availableActionExpected = Arrays.asList(
                new SimpleBuild(field[13]));
        availableActionActual = controller.getAvailableAction(boardTest, field[17]);
        assertTrue(availableActionActual.containsAll(availableActionExpected));
        assertTrue(availableActionActual.size() == availableActionExpected.size());

        //17 -BUILD-> 13
        lastaction = new SimpleBuild(field[13]);
        gm.applyAction(lastaction);

        assertFalse(controller.checkVictory(lastaction, boardTest));
        assertFalse(controller.nextState(lastaction, boardTest));


        assertEquals(2, boardTest.getLevel(field[1]));
        assertEquals(3, boardTest.getLevel(field[13]));
        assertTrue(boardTest.isWorker(field[17]));

    }

    @Test
    public void getWorkers() {
        List<Position> p1Actual = controller.getWorkers(boardTest, "p1");
        List<Position> a1Actual = controller.getWorkers(boardTest, "a1");
        assertTrue(p1Actual.size() == 2);
        assertTrue(a1Actual.size() == 3);
        assertTrue(p1Actual.containsAll(Arrays.asList(field[0], field[12])));
        assertTrue(a1Actual.containsAll(Arrays.asList(field[6], field[16], field[19])));
    }
}