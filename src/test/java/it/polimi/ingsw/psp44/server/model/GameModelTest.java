package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.server.model.Worker.Sex;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.PushForwardMovement;
import it.polimi.ingsw.psp44.server.model.actions.SimpleMovement;
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
     * Tests the addition and removal of players with their concequences on the board
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

        assertEquals(0, gameModel.getNumberOfPlayer());

        gameModel.addPlayer(player3);

        assertEquals(player3.getNickname(), gameModel.getCurrentPlayerNickname());

        gameModel.addPlayer(player2);
        gameModel.addPlayer(player1);

        assertEquals(3, gameModel.getNumberOfPlayer());
        assertEquals(player3.getNickname(), gameModel.getCurrentPlayerNickname());

        assertThrows(IllegalArgumentException.class, () -> gameModel.addPlayer(null));
        assertThrows(PlayerException.class, () -> gameModel.addPlayer(player1));
        assertThrows(IllegalArgumentException.class, () -> gameModel.removePlayer(null));
        assertThrows(PlayerException.class, () -> gameModel.removePlayer(player4.getNickname()));

        Board gameBoard = gameModel.getBoard();

        gameBoard.setWorker(new Position(1, 1), worker11);
        gameBoard.setWorker(new Position(1, 2), worker12);

        gameBoard.setWorker(new Position(2, 1), worker21);
        gameBoard.setWorker(new Position(2, 2), worker22);

        gameModel.removePlayer(player2.getNickname());
        gameModel.nextTurn();
        assertEquals(player1.getNickname(), gameModel.getCurrentPlayerNickname());
        assertTrue(gameBoard.getPlayerWorkersPositions(player2.getNickname()).isEmpty());

        gameModel.removePlayer(player1.getNickname());
        assertEquals(player3.getNickname(), gameModel.getCurrentPlayerNickname());
        assertTrue(gameBoard.getPlayerWorkersPositions(player1.getNickname()).isEmpty());
        assertEquals(1, gameModel.getNumberOfPlayer());

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

        assertTrue(gameModel.isFullRound());
        assertEquals(player1.getNickname(), gameModel.getCurrentPlayerNickname());
        gameModel.nextTurn();
        assertEquals(player2.getNickname(), gameModel.getCurrentPlayerNickname());
        gameModel.nextTurn();
        assertEquals(player3.getNickname(), gameModel.getCurrentPlayerNickname());
        gameModel.nextTurn();
        assertEquals(player1.getNickname(), gameModel.getCurrentPlayerNickname());
        assertTrue(gameModel.isFullRound());

    }

    @Test
    public void applyAction(){
        Worker worker1 = new Worker("p1", Sex.FEMALE);
        gameModel.setWorker(new Position(0,1));
        Action move= new SimpleMovement(new Position(0,1),new Position(1,1));
        Action move2= new PushForwardMovement(new Position(1,1),new Position(1,2),new Position(1,3));
        Action move3= new SimpleMovement(new Position(1,2),new Position(1,3));
        gameModel.applyAction(move);
        gameModel.applyAction(move2);
        gameModel.applyAction(move3);
        assertEquals(new Position(1,3),gameModel.getWorker());


    }

}