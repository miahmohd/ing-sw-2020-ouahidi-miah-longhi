package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private Space[][] board;

    public Board(int dimension) {
        this.board = new Space[dimension][dimension];
    }

    public Board(Space[][] board) {
        this.board = board;
    }

    /**
     * Builds the next block at the specified position.
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildUp(Position position) {

    }

    /**
     * Removes the last block at the specified position.
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildDown(Position position) {

    }

    public int getLevel(Position position) {
        return 1;
    }

    public void buildDome(Position position) {

    }

    public void removeDome(Position position) {

    }

    public boolean isDome(Position position) {
        return false;
    }

    public void setWorker(Position position, Worker worker) {

    }

    public Worker getWorker(Position position) {
        return null;
    }

    public List<Position> getNeighbouringPositions(Position position) {
        return new ArrayList<Position>();
    }

    public List<Position> getPlayerWorkersPositions(Position position) {
        return new ArrayList<Position>();
    }


}

