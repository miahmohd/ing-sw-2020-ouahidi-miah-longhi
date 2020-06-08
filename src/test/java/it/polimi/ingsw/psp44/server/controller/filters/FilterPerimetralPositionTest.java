package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.SimpleMovement;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FilterPerimetralPositionTest {
    private Board gameBoard;
    private Filter filterPerimetralPosition;

    @Before
    public void setUp() {
        gameBoard = new Board();
        filterPerimetralPosition = new FilterPerimetralPosition();

    }

    @Test
    public void filter() {
        List<Position> actualPositions, expectedPositions;
        expectedPositions=new ArrayList<>();
        actualPositions=gameBoard.getUnoccupiedPosition();
        filterPerimetralPosition.update(new SimpleMovement(new Position(0,0),new Position(1,1)),gameBoard);
        filterPerimetralPosition.filter(new Position(0,0),actualPositions,gameBoard);

        for (int row=1;row<4;++row){
            for (int column=1;column<4;++column){
                expectedPositions.add(new Position(row,column));
            }
        }

        assertEquals(expectedPositions.size(),actualPositions.size());
        assertTrue(expectedPositions.containsAll(actualPositions));

    }
}