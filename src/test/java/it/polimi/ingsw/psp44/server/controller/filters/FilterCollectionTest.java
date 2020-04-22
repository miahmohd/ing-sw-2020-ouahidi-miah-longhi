package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Player;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.exception.FilterException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FilterCollectionTest {
    private Board gameBoard;
    private FilterCollection filterCollection;

    @Before
    public void setUp() {
        gameBoard = new Board();
        filterCollection = new FilterCollection();

    }

    @Test
    public void addAndRemove() {
        Filter dome = new FilterDome();
        Filter upByTwo = new FilterUpByTwo();

        filterCollection.add(dome);
        filterCollection.add(upByTwo);

        Filter duplicateDome = new FilterDome();
        Filter duplicateUpByTwo = new FilterUpByTwo();

        assertThrows(IllegalArgumentException.class, () -> filterCollection.add(null));
        assertThrows(FilterException.class, () -> filterCollection.add(duplicateDome));
        assertThrows(FilterException.class, () -> filterCollection.add(duplicateUpByTwo));


        filterCollection.remove(duplicateDome);
        assertThrows(FilterException.class, () -> filterCollection.remove(dome));
        assertThrows(IllegalArgumentException.class, () -> filterCollection.remove(null));


    }

    @Test
    public void filter() {
        Filter dome = new FilterDome();
        Filter upByTwo = new FilterUpByTwo();
        Filter myWorkers = new FilterMyWorkers();
        Filter otherWorkers = new FilterOtherWorkers();

        Position startingPosition = new Position(2, 2);

        Position positionOtherWorker2 = new Position(4, 4);

        Position positionMyWorker = new Position(3, 3);
        Position positionOtherWorker1 = new Position(2, 1);
        Position level1 = startingPosition;
        Position level2 = new Position(1, 1);
        Position level3 = new Position(2, 3);
        Position level0 = new Position(1, 2);
        Position level00 = new Position(1, 3);
        Position level01 = new Position(3, 1);
        Position domePosition = new Position(3, 2);

        Player myPlayer = new Player("test");
        Player otherPlayer = new Player("another test");

        Worker myWorkerMale = new Worker(myPlayer.getNickname(), Worker.Sex.MALE);
        Worker myWorkerFemale = new Worker(myPlayer.getNickname(), Worker.Sex.FEMALE);

        Worker otherWorkerMale = new Worker(otherPlayer.getNickname(), Worker.Sex.MALE);
        Worker otherWorkerFemale = new Worker(otherPlayer.getNickname(), Worker.Sex.FEMALE);

        gameBoard.buildUp(positionMyWorker);
        gameBoard.buildUp(positionMyWorker);
        gameBoard.buildUp(positionOtherWorker2);
        gameBoard.buildUp(positionOtherWorker1);

        gameBoard.buildDome(domePosition);

        gameBoard.buildUp(level1);
        gameBoard.buildUp(level2);
        gameBoard.buildUp(level2);
        gameBoard.buildUp(level3);
        gameBoard.buildUp(level3);
        gameBoard.buildUp(level3);

        gameBoard.setWorker(startingPosition, myWorkerMale);
        gameBoard.setWorker(positionOtherWorker1, otherWorkerMale);
        gameBoard.setWorker(positionOtherWorker2, otherWorkerFemale);
        gameBoard.setWorker(positionMyWorker, myWorkerFemale);

        List<Position> expectedPositions = new ArrayList<>(Arrays.asList(
                level2,
                level0,
                level00,
                level01,
                positionOtherWorker1
        ));

        filterCollection.add(dome);
        filterCollection.add(upByTwo);
        filterCollection.add(myWorkers);

        List<Position> actualPositions = gameBoard.getNeighbouringPositions(startingPosition);
        filterCollection.filter(startingPosition, actualPositions, gameBoard, false);

        assertEquals(expectedPositions.size(), actualPositions.size());
        assertTrue(expectedPositions.containsAll(actualPositions));


        filterCollection.add(otherWorkers);
        filterCollection.remove(upByTwo);

        expectedPositions = new ArrayList<>(Arrays.asList(
                level2,
                level0,
                level00,
                level01,
                level3
        ));

        actualPositions = gameBoard.getNeighbouringPositions(startingPosition);
        filterCollection.filter(startingPosition, actualPositions, gameBoard, false);

        filterCollection.filter(startingPosition, actualPositions, gameBoard, false);

        assertEquals(expectedPositions.size(), actualPositions.size());
        assertTrue(expectedPositions.containsAll(actualPositions));
    }
}