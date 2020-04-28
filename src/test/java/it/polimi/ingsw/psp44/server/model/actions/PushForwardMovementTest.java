package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PushForwardMovementTest {

    @Test
    public void execute() {
        Board board = new Board();

        Worker sourceWorkerM = new Worker("p1", Worker.Sex.MALE);
        Worker sourceWorkerF = new Worker("p1", Worker.Sex.FEMALE);
        Worker targetWorkerM = new Worker("p2", Worker.Sex.MALE);
        Worker targetWorkerF = new Worker("p2", Worker.Sex.FEMALE);

        Position source1 = new Position(0, 0);
        Position target1 = new Position(0, 1);
        Position forward1 = new Position(0, 2);
        PushForwardMovement move1 = new PushForwardMovement(source1, target1,forward1);

        Position source2 = new Position(1, 0);
        Position target2 = new Position(2, 1);
        Position forward2 = new Position(3, 2);
        PushForwardMovement move2 = new PushForwardMovement(source2, target2,forward2);


        board.setWorker(source1, sourceWorkerM);
        board.setWorker(target1, targetWorkerM);
        assertEquals(null, board.getWorker(forward1));
        move1.execute(board);
        assertEquals(targetWorkerM, board.getWorker(forward1));


        board.setWorker(source2, sourceWorkerF);
        board.setWorker(target2, targetWorkerF);
        assertEquals(null, board.getWorker(forward2));
        move2.execute(board);
        assertEquals(targetWorkerF, board.getWorker(forward2));

        assertEquals(3,move1.getModifiedPositions().size());
        assertEquals(3,move2.getModifiedPositions().size());
        assertTrue(move1.getModifiedPositions().containsAll(Arrays.asList(source1,target1,forward1)));
        assertTrue(move2.getModifiedPositions().containsAll(Arrays.asList(source2,target2,forward2)));
    }
}