package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleBuildTest {

    @Test
    public void execute() {
        Board board = new Board();
        Position buildPos = new Position(0, 0);
        Position unbuildPos = new Position(1, 0);
        SimpleBuild build = new SimpleBuild(buildPos);
        SimpleBuild unbuild = new SimpleBuild(unbuildPos, true);


        assertEquals(0, board.getLevel(buildPos));
        build.execute(board);
        assertEquals(1, board.getLevel(buildPos));

        board.buildUp(unbuildPos);
        unbuild.execute(board);
        assertEquals(0, board.getLevel(unbuildPos));
        assertTrue(build.getModifiedPositions().size() == 1);
        assertEquals(build.getModifiedPositions().get(0), buildPos);
        assertTrue(unbuild.getModifiedPositions().size() == 1);
        assertEquals(unbuild.getModifiedPositions().get(0), unbuildPos);

    }
}