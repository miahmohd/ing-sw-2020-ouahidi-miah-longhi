package it.polimi.ingsw.psp44.server.model;

import java.util.LinkedList;

import it.polimi.ingsw.psp44.util.AppProperties;
import it.polimi.ingsw.psp44.util.IObservable;
import it.polimi.ingsw.psp44.util.IObserver;
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

    public void addPlayer(Player player){
        if(player == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_PLAYER));
        if(players.contains(player))
            throw new PlayerException(AppProperties.getInstance().getMessage(ErrorCodes.PLAYER_IN_GAME));
        this.players.addFirst(player);
    }

    public void removePlayer(Player player){
        if(player == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_PLAYER));
        if(!players.contains(player))
            throw new PlayerException(AppProperties.getInstance().getMessage(ErrorCodes.PLAYER_NOT_IN_GAME));
        this.players.remove(player);
    }

    public void nextTurn(){
        if(players.isEmpty())
            throw new PlayerException(AppProperties.getInstance().getMessage(ErrorCodes.NO_PLAYERS_IN_GAME));
        Player currentPlayer = players.removeFirst();
        players.addLast(currentPlayer);
    }

    public String getCurrentPlayerNickname() {
        if(players.isEmpty())
            throw new PlayerException(AppProperties.getInstance().getMessage(ErrorCodes.NO_PLAYERS_IN_GAME));
        Player currentPlayer = players.getFirst();
        return currentPlayer.getNickname();
    }

    public Board getBoard(){
        return gameBoard;
    }

}
