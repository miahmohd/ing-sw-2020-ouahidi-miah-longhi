package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.server.model.Worker.Sex;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.exception.PlayerException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameModelTest {
    GameModel gameModel;

    @Before
    public void setUp() {
        gameModel = new GameModel();
    }

    @Test
    /**
     * Tests the addition and removal of players with thjeir concequences on the board 
     * and on the currentPlayer
     */
    public void addAndRemovePlayers() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");

        Worker worker11 = new Worker(player1.getNickname(), Sex.FEMALE);
        Worker worker12 = new Worker(player1.getNickname(), Sex.MALE);
        Worker worker21 = new Worker(player2.getNickname(), Sex.FEMALE);
        Worker worker22 = new Worker(player2.getNickname(), Sex.MALE);


        gameModel.addPlayer(player3);
        assertEquals(player3.getNickname(), gameModel.getCurrentPlayerNickname());
        
        gameModel.addPlayer(player2);
        gameModel.addPlayer(player1);

        assertEquals(player1.getNickname(), gameModel.getCurrentPlayerNickname());
        assertThrows(IllegalArgumentException.class, () -> gameModel.addPlayer(null));
        assertThrows(PlayerException.class, () -> gameModel.addPlayer(player1));


        assertThrows(IllegalArgumentException.class, () -> gameModel.removePlayer(null));
        assertThrows(PlayerException.class, () -> gameModel.removePlayer(player4));

        Board gameBoard = gameModel.getBoard();

        gameBoard.setWorker(new Position(1,1), worker11);
        gameBoard.setWorker(new Position(1,2), worker12);

        gameBoard.setWorker(new Position(2,1), worker21);
        gameBoard.setWorker(new Position(2,2), worker22);

        gameModel.removePlayer(player2);
        assertEquals(player1.getNickname(), gameModel.getCurrentPlayerNickname());
        assertTrue(gameBoard.getPlayerWorkersPositions(player2.getNickname()).isEmpty());

        gameModel.removePlayer(player1);
        assertEquals(player3.getNickname(), gameModel.getCurrentPlayerNickname());
        assertTrue(gameBoard.getPlayerWorkersPositions(player1.getNickname()).isEmpty());


    }

    @Test
    public void nextTurn() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");

        assertThrows(IllegalStateException.class, () -> gameModel.getCurrentPlayerNickname());
        assertThrows(IllegalStateException.class, () -> gameModel.nextTurn());

        gameModel.addPlayer(player1);
        gameModel.addPlayer(player2);
        gameModel.addPlayer(player3);
        
        assertEquals(player3.getNickname(), gameModel.getCurrentPlayerNickname());
        gameModel.nextTurn();
        assertEquals(player2.getNickname(), gameModel.getCurrentPlayerNickname());
        gameModel.nextTurn();
        assertEquals(player1.getNickname(), gameModel.getCurrentPlayerNickname());
        gameModel.nextTurn();
        assertEquals(player3.getNickname(), gameModel.getCurrentPlayerNickname());

    }

}