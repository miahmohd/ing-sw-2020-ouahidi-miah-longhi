package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SwapMovementTest {

    @Test
    public void execute() {
        Board board = new Board();

        Worker sourceWorker = new Worker("p1", Worker.Sex.MALE);
        Worker targetWorker = new Worker("p2", Worker.Sex.MALE);

        Position source = new Position(0, 0);
        Position target = new Position(0, 1);
        SwapMovement move = new SwapMovement(source, target);


        board.setWorker(source, sourceWorker);
        board.setWorker(target, targetWorker);
        move.execute(board);
        assertEquals(targetWorker, board.getWorker(source));
        assertEquals(sourceWorker, board.getWorker(target));
        assertTrue(move.getModifiedPositions().size() == 2);
        assertTrue(move.getModifiedPositions().containsAll(Arrays.asList(source, target)));

    }
}