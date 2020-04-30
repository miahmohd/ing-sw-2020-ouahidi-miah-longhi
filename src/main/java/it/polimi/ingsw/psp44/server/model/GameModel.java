package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.IObservable;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.R;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;
import it.polimi.ingsw.psp44.util.exception.PlayerException;

import java.util.LinkedList;
import java.util.List;


public class GameModel extends IObservable<Message> {
    private final Board gameBoard;
    private final LinkedList<Player> players;
    private int turnNumber;


    public GameModel(Board gameBoard, LinkedList<Player> players, int turnNumber) {
        this.gameBoard = gameBoard;
        this.players = players;
        this.turnNumber = turnNumber;
    }

    public GameModel() {
        this(new Board(), new LinkedList<>(), 0);
    }


    /**
     * Execute the action and applay the changes to the board.
     *
     * @param action object representing a change to the board.
     */
    public void doAction(Action action) {
        action.execute(this.gameBoard);
        Message toSend = new Message(Message.Code.MODIFIED_POSITIONS, BodyFactory.toCells(action.getModifiedPositions(), this.getBoard()));
        this.notifyObservers(toSend);
    }

    /**
     * Adds a player to the list of players in the game
     *
     * @param player the player to add
     * @throws IllegalArgumentException if player is null
     * @throws PlayerException          if player is already in the game
     */
    public void addPlayer(Player player) {
        if (player == null)
            throw new IllegalArgumentException(R.getAppProperties().get(ErrorCodes.NULL_PLAYER));
        if (players.contains(player))
            throw new PlayerException(R.getAppProperties().get(ErrorCodes.PLAYER_IN_GAME));
        this.players.addLast(player);

    }

    /**
     * Removes the player and its associated workers from the game
     *
     * @param player is the player i want to remove
     * @throws IllegalArgumentException if player is null
     * @throws PlayerException          if a player is not in the game
     */
    public void removePlayer(String player) {
        if (player == null)
            throw new IllegalArgumentException(R.getAppProperties().get(ErrorCodes.NULL_PLAYER));
        if (players.stream().noneMatch(p -> p.getNickname().equals(player)))
            throw new PlayerException(R.getAppProperties().get(ErrorCodes.PLAYER_NOT_IN_GAME));

        List<Position> playerWorkers = gameBoard.getPlayerWorkersPositions(player);

        for (Position position : playerWorkers)
            gameBoard.setWorker(position, null);

        //TODO notify(); ci sta anche creare delle mosse ma non esageriamo

        this.players.removeIf((p) -> p.getNickname().equals(player));
    }

    /**
     * Changes the state of the game model by alterating the current player
     *
     * @throws IllegalStateException if there are no players in the game
     */
    public void nextTurn() {
        if (players.isEmpty())
            throw new IllegalStateException(R.getAppProperties().get(ErrorCodes.NO_PLAYERS_IN_GAME));

        Player currentPlayer = players.removeFirst();
        players.addLast(currentPlayer);
        this.turnNumber++;
    }

    /**
     * @return nickname of the current player
     * @throws IllegalStateException if there are no players in the game
     */
    public String getCurrentPlayerNickname() {
        if (players.isEmpty())
            throw new IllegalStateException(R.getAppProperties().get(ErrorCodes.NO_PLAYERS_IN_GAME));
        Player currentPlayer = players.getFirst();
        return currentPlayer.getNickname();
    }

    /**
     * ATTENTION: CAUTION is advised about any changes made to the board
     *
     * @return board reference.
     */
    public Board getBoard() {
        return gameBoard;
    }

    /**
     * @return number of players
     */
    public int getNumberOfPlayer() {
        return this.players.size();
    }

    /**
     * A full round is when all players have the seme number of turns.
     *
     * @return true if there has been a full round, false otherwise
     */
    public boolean isFullRound() {
        return this.turnNumber % this.players.size() == 0;
    }

    /**
     * return the worker selected for this turn
     *
     * @return selected worker position
     */
    public Position getWorker() {
        return gameBoard.getSelectedWorker();
    }

    /**
     * Change position of the selected worker in this turn
     *
     * @param selectedWorkerPosition
     */
    public void setWorker(Position selectedWorkerPosition) {
        gameBoard.setSelectedWorker(selectedWorkerPosition);
    }

}
