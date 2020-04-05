package it.polimi.ingsw.psp44.server.model;

import java.util.LinkedList;
import java.util.List;

import it.polimi.ingsw.psp44.util.AppProperties;
import it.polimi.ingsw.psp44.util.IObservable;
import it.polimi.ingsw.psp44.util.IObserver;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;
import it.polimi.ingsw.psp44.util.exception.PlayerException;

public class GameModel implements IObservable {
    private Board gameBoard;
    private LinkedList<Player> players;

    public GameModel(Board gameBoard, LinkedList<Player> players) {
        this.gameBoard = gameBoard;
        this.players = players;
    }

    public GameModel(){
        this.gameBoard = new Board();
        this.players = new LinkedList<>();
    }

    /**
     * Adds a player to the list of players in the game
     * @param player
     * @throws IllegalArgumentException if player is null
     * @throws PlayerException if player is already in the game
     */
    public void addPlayer(Player player){
        if(player == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_PLAYER));
        if(players.contains(player))
            throw new PlayerException(AppProperties.getInstance().getMessage(ErrorCodes.PLAYER_IN_GAME));
        this.players.addFirst(player);
    }

    /**
     * Removes the player and its associated workers from the game 
     * @param player is the player i want to remove
     * @throws IllegalArgumentException if player is null
     * @throws PlayerException if a player is not in the game
     */
    public void removePlayer(Player player){
        if(player == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_PLAYER));
        if(!players.contains(player))
            throw new PlayerException(AppProperties.getInstance().getMessage(ErrorCodes.PLAYER_NOT_IN_GAME));
        
        List<Position> playerWorkers = gameBoard.getPlayerWorkersPositions(player.getNickname());
        
        for (Position position : playerWorkers) 
            gameBoard.setWorker(position, null);
    
        //TODO notify(); ci sta anche creare delle mosse ma non esageriamo

        this.players.remove(player);
    }

    /**
     * Changes the state of the game model by alterating the current player
     * @throws IllegalStateException if there are no players in the game
     */
    public void nextTurn(){
        if(players.isEmpty())
            throw new IllegalStateException(AppProperties.getInstance().getMessage(ErrorCodes.NO_PLAYERS_IN_GAME));
        Player currentPlayer = players.removeFirst();
        players.addLast(currentPlayer);
    }

    /**
     * @return nickname of the current player
     * @throws IllegalStateException if there are no players in the game
     */
    public String getCurrentPlayerNickname() {
        if(players.isEmpty())
            throw new IllegalStateException(AppProperties.getInstance().getMessage(ErrorCodes.NO_PLAYERS_IN_GAME));
        Player currentPlayer = players.getFirst();
        return currentPlayer.getNickname();
    }

    /**
     * ATTENTION: CAUTION is advised about any changes made to the board
     * @return board reference.
     */
    public Board getBoard(){
        return gameBoard;
    }

}
