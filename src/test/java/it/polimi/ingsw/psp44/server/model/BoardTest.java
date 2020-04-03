package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.exception.ConstructionException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {
    private Board boardTest;

    @Before
    public void setUp() {
        boardTest = new Board();

    }

    @Test
    public void buildUp() {
        Position level0 = new Position(0, 0);
        Position level1 = new Position(0, 1);
        Position level2 = new Position(0, 2);
        Position level3 = new Position(0, 3);
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

        assertThrows(IllegalArgumentException.class,
                () -> boardTest.buildUp(null));
        assertThrows(IllegalArgumentException.class,
                () -> boardTest.buildUp(new Position(20, 20)));
        assertThrows(ConstructionException.class,
                () -> {
                    Position p = new Position(1, 0);
                    boardTest.buildDome(p);
                    boardTest.buildUp(p);
                });
        assertThrows(ConstructionException.class,
                () -> boardTest.buildUp(level3));

    }

    @Test
    public void buildDown() {
        Position level0 = new Position(0, 0);
        Position level1 = new Position(0, 1);
        Position level2 = new Position(0, 2);
        Position level3 = new Position(0, 3);

        boardTest.buildUp(level1);
        boardTest.buildUp(level2);
        boardTest.buildUp(level2);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);

        boardTest.buildDown(level3);
        boardTest.buildDown(level3);
        boardTest.buildDown(level2);
        boardTest.buildDown(level1);

        assertEquals(0, boardTest.getLevel(level0));
        assertEquals(0, boardTest.getLevel(level1));
        assertEquals(1, boardTest.getLevel(level2));
        assertEquals(1, boardTest.getLevel(level3));

        assertThrows(IllegalArgumentException.class,
                () -> boardTest.buildDown(null));
        assertThrows(IllegalArgumentException.class,
                () -> boardTest.buildDown(new Position(20, 20)));
        assertThrows(ConstructionException.class,
                () -> {
                    Position p = new Position(1, 0);
                    boardTest.buildDome(p);
                    boardTest.buildDown(p);
                });
        assertThrows(ConstructionException.class,
                () -> boardTest.buildDown(level0));

    }

    @Test
    public void buildDome() {
        Position level0 = new Position(0, 0);
        Position level1 = new Position(0, 1);
        Position level2 = new Position(0, 2);
        Position level3 = new Position(0, 3);

        boardTest.buildUp(level1);
        boardTest.buildUp(level2);
        boardTest.buildUp(level2);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);

        boardTest.buildDome(level0);
        boardTest.buildDome(level1);
        boardTest.buildDome(level3);


        assertTrue(boardTest.isDome(level0));
        assertTrue(boardTest.isDome(level1));
        assertTrue(boardTest.isDome(level3));
        assertFalse(boardTest.isDome(level2));

        assertThrows(IllegalArgumentException.class,
                () -> boardTest.buildDome(null));
        assertThrows(IllegalArgumentException.class,
                () -> boardTest.buildDome(new Position(20, 20)));
        assertThrows(ConstructionException.class,
                () -> {
                    Position p = new Position(1, 0);
                    boardTest.buildDome(p);
                    boardTest.buildDome(p);
                });

    }

    @Test
    public void removeDome() {
        Position level0 = new Position(0, 0);
        Position level1 = new Position(0, 1);
        Position level2 = new Position(0, 2);
        Position level3 = new Position(0, 3);

        boardTest.buildUp(level1);
        boardTest.buildUp(level2);
        boardTest.buildUp(level2);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);

        boardTest.buildDome(level0);
        boardTest.buildDome(level1);
        boardTest.buildDome(level3);

        boardTest.removeDome(level0);
        boardTest.removeDome(level3);

        assertFalse(boardTest.isDome(level0));
        assertFalse(boardTest.isDome(level3));
        assertFalse(boardTest.isDome(level2));

        assertTrue(boardTest.isDome(level1));

        assertThrows(IllegalArgumentException.class,
                () -> boardTest.removeDome(null));
        assertThrows(IllegalArgumentException.class,
                () -> boardTest.removeDome(new Position(20, 20)));
        assertThrows(ConstructionException.class,
                () -> {
                    Position p = new Position(1, 0);
                    boardTest.removeDome(p);
                });
    }

    @Test
    public void getNeighbouringPositions() {
        Position edge = new Position(0, 0);
        Position side = new Position(4, 2);
        Position middle = new Position(2, 2);

        List<Position> edgeNeighbouringPositionsExpected = new ArrayList<Position>(Arrays.asList(
                new Position(1, 0),
                new Position(1, 1),
                new Position(0, 1)
        ));


        List<Position> sideNeighbouringPositionsExpected = new ArrayList<Position>(Arrays.asList(
                new Position(3, 2),
                new Position(3, 1),
                new Position(3, 3),
                new Position(4, 1),
                new Position(4, 3)
        ));

        List<Position> middleNeighbouringPositionsExpected = new ArrayList<Position>(Arrays.asList(
                new Position(1, 1),
                new Position(1, 2),
                new Position(1, 3),
                new Position(2, 3),
                new Position(3, 3),
                new Position(3, 2),
                new Position(3, 1),
                new Position(2, 1)
        ));

        List<Position> edgeNeighbouringPositionsActual = boardTest.getNeighbouringPositions(edge);
        List<Position> sideNeighbouringPositionsActual = boardTest.getNeighbouringPositions(side);
        List<Position> middleNeighbouringPositionsActual = boardTest.getNeighbouringPositions(middle);

        assertEquals(edgeNeighbouringPositionsActual.size(), edgeNeighbouringPositionsExpected.size());
        assertEquals(sideNeighbouringPositionsActual.size(), sideNeighbouringPositionsExpected.size());
        assertEquals(middleNeighbouringPositionsActual.size(), middleNeighbouringPositionsExpected.size());

        assertTrue(edgeNeighbouringPositionsActual.containsAll(edgeNeighbouringPositionsExpected));
        assertTrue(sideNeighbouringPositionsActual.containsAll(sideNeighbouringPositionsExpected));
        assertTrue(middleNeighbouringPositionsActual.containsAll(middleNeighbouringPositionsExpected));

        assertThrows(IllegalArgumentException.class,
                () -> boardTest.getNeighbouringPositions(null));
        assertThrows(IllegalArgumentException.class,
                () -> boardTest.getNeighbouringPositions(new Position(20, 20)));


    }

    @Test
    public void getPlayerWorkersPositions() {
        Position level0 = new Position(0, 0);
        Position level1 = new Position(0, 1);
        Position level2 = new Position(0, 2);
        Position level3 = new Position(0, 3);

        List<Position> workersTestExpected = new ArrayList<>(Arrays.asList(level0, level1));
        List<Position> workersAnotherTestsExpected = new ArrayList<>(Arrays.asList(level2, level3));

        boardTest.buildUp(level1);
        boardTest.buildUp(level2);
        boardTest.buildUp(level2);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);
        boardTest.buildUp(level3);

        Player testPlayer = new Player("test");
        Player anotherTestPlayer = new Player("another test");

        Worker testWorkerMale = new Worker(testPlayer.getNickname(), Worker.Sex.MALE);
        Worker testWorkerFemale = new Worker(testPlayer.getNickname(), Worker.Sex.FEMALE);

        Worker anotherTestWorkerMale = new Worker(anotherTestPlayer.getNickname(), Worker.Sex.MALE);
        Worker anotherTestWorkerFemale = new Worker(anotherTestPlayer.getNickname(), Worker.Sex.FEMALE);

        boardTest.setWorker(level0, testWorkerMale);
        boardTest.setWorker(level1, testWorkerFemale);

        List<Position> workersTestActual = boardTest.getPlayerWorkersPositions(testPlayer.getNickname());
        assertEquals(workersTestExpected.size(), workersTestActual.size());
        assertTrue(workersTestActual.containsAll(workersTestExpected));

        boardTest.setWorker(level2, anotherTestWorkerMale);
        boardTest.setWorker(level3, anotherTestWorkerFemale);
        List<Position> workersAnotherTestActual = boardTest.getPlayerWorkersPositions(anotherTestPlayer.getNickname());
        assertEquals(workersAnotherTestsExpected.size(), workersAnotherTestActual.size());
        assertTrue(workersAnotherTestActual.containsAll(workersAnotherTestsExpected));

    }
}