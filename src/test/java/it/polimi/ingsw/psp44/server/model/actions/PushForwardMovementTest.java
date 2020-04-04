package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PushForwardMovementTest {

    @Test
    public void execute() {
        Board board = new Board();

        Worker sourceWorker = new Worker("p1", Worker.Sex.MALE);
        Worker targetWorker = new Worker("p2", Worker.Sex.MALE);

        Position source1 = new Position(0, 0);
        Position target1 = new Position(0, 1);
        Position forward1 = new Position(0, 2);
        PushForwardMovement move1 = new PushForwardMovement(source1, target1);

        Position source2 = new Position(1, 0);
        Position target2 = new Position(2, 1);
        Position forward2 = new Position(3, 2);
        PushForwardMovement move2 = new PushForwardMovement(source2, target2);


        board.setWorker(source1, sourceWorker);
        board.setWorker(target1, targetWorker);
        assertEquals(null, board.getWorker(forward1));
        move1.execute(board);
        assertEquals(targetWorker, board.getWorker(forward1));


        board.setWorker(source2, sourceWorker);
        board.setWorker(target2, targetWorker);
        assertEquals(null, board.getWorker(forward2));
        move2.execute(board);
        assertEquals(targetWorker, board.getWorker(forward2));

    }
}