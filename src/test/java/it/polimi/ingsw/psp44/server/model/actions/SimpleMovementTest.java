package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleMovementTest {

    @Test
    public void execute() {
        Board board = new Board();
        Position source = new Position(0, 0);
        Position target = new Position(0, 1);
        Worker worker = new Worker("test", Worker.Sex.MALE);
        board.setWorker(source, worker);

        SimpleMovement move = new SimpleMovement(source, target);

        assertEquals(null, board.getWorker(target));
        move.execute(board);
        assertEquals(worker, board.getWorker(target));
        assertEquals(null, board.getWorker(source));
        assertTrue(move.getModifiedPositions().size() == 2);
        assertTrue(move.getModifiedPositions().containsAll(Arrays.asList(source, target)));
    }
}