package it.polimi.ingsw.psp44.server.model.actions;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Test;

import static org.junit.Assert.*;

public class DomeBuildTest {

    @Test
    public void execute() {
        Board board = new Board();
        Position buildPos = new Position(0, 0);
        Position unbuildPos = new Position(1, 0);
        DomeBuild build = new DomeBuild(buildPos);
        DomeBuild unbuild = new DomeBuild(unbuildPos, true);


        assertFalse(board.isDome(buildPos));
        build.execute(board);
        assertTrue(board.isDome(buildPos));

        board.buildDome(unbuildPos);
        unbuild.execute(board);
        assertFalse(board.isDome(unbuildPos));
        assertTrue(build.getModifiedPositions().size()==1);
        assertTrue(unbuild.getModifiedPositions().size()==1);
        assertEquals(build.getModifiedPositions().get(0),buildPos);
        assertEquals(unbuild.getModifiedPositions().get(0),unbuildPos);
    }
}