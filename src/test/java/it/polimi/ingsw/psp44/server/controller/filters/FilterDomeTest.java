package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterDomeTest {
    private Board gameBoard;
    private Filter filterDomeTest;

    @Before
    public void setUp() {
        gameBoard = new Board();
        filterDomeTest = new FilterDome();

    }

    @Test
    public void filter() {
        Position startingPosition = new Position(2,2);
        
        Position positionDome1 = new Position(1, 1);
        Position positionDome2 = new Position(1, 2);
        Position positionDome3 = new Position(0, 0);

        gameBoard.buildUp(positionDome1);

        gameBoard.buildUp(positionDome2);
        gameBoard.buildUp(positionDome2);

        gameBoard.buildDome(positionDome1);
        gameBoard.buildDome(positionDome2);
        gameBoard.buildDome(positionDome3);


        gameBoard.buildUp( new Position(2, 3));
        gameBoard.buildUp( new Position(2, 3));
        gameBoard.buildUp( new Position(2, 1));


        List<Position> expectedPositions = new ArrayList<Position>(Arrays.asList(
            new Position(1, 3),
            new Position(2, 3),
            new Position(3, 3),
            new Position(3, 2),
            new Position(3, 1),
            new Position(2, 1)
        ));

        List<Position> actualPositions = gameBoard.getNeighbouringPositions(startingPosition);


        filterDomeTest.filter(startingPosition, actualPositions, gameBoard, false);
        assertEquals(actualPositions.size(), expectedPositions.size());
        assertTrue(actualPositions.containsAll(expectedPositions));


    }
    
}