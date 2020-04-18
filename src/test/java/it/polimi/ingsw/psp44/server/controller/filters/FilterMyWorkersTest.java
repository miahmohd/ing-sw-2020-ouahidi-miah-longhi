package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Player;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class FilterMyWorkersTest {
    private Board gameBoard;
    private Filter filterMyWorkersTest;

    @Before
    public void setUp() {
        gameBoard = new Board();
        filterMyWorkersTest = new FilterMyWorkers();

    }

    @Test
    public void filter() {
        Position startingPosition = new Position(2, 2);
        Position level1 = new Position(2, 1);
        Position level2 = new Position(3, 2);
        Position level3 = new Position(3, 3);

        Player myPlayer = new Player("test");
        Player otherPlayer = new Player("another test");
        
        Worker myWorkerMale = new Worker(myPlayer.getNickname(), Worker.Sex.MALE);
        Worker myWorkerFemale = new Worker(myPlayer.getNickname(), Worker.Sex.FEMALE);

        Worker otherWorkerMale = new Worker(otherPlayer.getNickname(), Worker.Sex.MALE);
        Worker otherWorkerFemale = new Worker(otherPlayer.getNickname(), Worker.Sex.FEMALE);

        gameBoard.buildUp(level1);
        gameBoard.buildUp(level2);
        gameBoard.buildUp(level2);
        gameBoard.buildUp(level3);
        gameBoard.buildUp(level3);
        gameBoard.buildUp(level3);

        gameBoard.setWorker(startingPosition, myWorkerMale);
        gameBoard.setWorker(level1, otherWorkerMale);
        gameBoard.setWorker(level2, otherWorkerFemale);
        gameBoard.setWorker(level3, myWorkerFemale);
    

        

        List<Position> expectedPositions = new ArrayList<Position>(Arrays.asList(
            new Position(1, 1),
            new Position(1, 2),
            new Position(1, 3),
            new Position(2, 3),
            new Position(3, 2),
            new Position(3, 1),
            new Position(2, 1)
        ));

        List<Position> actualPositions = gameBoard.getNeighbouringPositions(startingPosition);


        filterMyWorkersTest.filter(startingPosition, actualPositions, gameBoard, false);
        
        assertEquals(expectedPositions.size(), actualPositions.size());
        assertTrue(expectedPositions.containsAll(actualPositions));


    }
}