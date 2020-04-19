package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FilterUpByTwoTest {
    private Board gameBoard;
    private Filter filterUpByTwo;

    @Before
    public void setUp() {
        gameBoard = new Board();
        filterUpByTwo = new FilterUpByTwo();

    }

    @Test
    public void filter() {
        Position startingPosition = new Position(2, 2);
        Position level0 = new Position(2, 3);
        Position level1 = new Position(2, 1);
        Position level2 = new Position(3, 2);
        Position level3 = new Position(3, 3);

        gameBoard.buildUp(level1);
        gameBoard.buildUp(level2);
        gameBoard.buildUp(level2);
        gameBoard.buildUp(level3);
        gameBoard.buildUp(level3);
        gameBoard.buildUp(level3);
    

        List<Position> expectedPositions = new ArrayList<Position>(Arrays.asList(
            new Position(1, 1),
            new Position(1, 2),
            new Position(1, 3),
            level0,
            new Position(3, 1),
            level1
        ));

        List<Position> actualPositions = gameBoard.getNeighbouringPositions(startingPosition);


        filterUpByTwo.filter(startingPosition, actualPositions, gameBoard, false);
        
        assertEquals(expectedPositions.size(), actualPositions.size());
        assertTrue(expectedPositions.containsAll(actualPositions));


    }

}