package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.util.Position;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int DIMENSION=5;
    private Space[][] board;

    public Board() {
        this.board = new Space[DIMENSION][DIMENSION];
        for (int i=0;i<DIMENSION;++i){
            for(int j=0;j<DIMENSION;++j){
                this.board[i][j]=new Space();
            }
        }
    }


    /**
     * Builds the next block at the specified position.
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildUp(Position position) {
        if(position==null)
            throw new IllegalArgumentException("Position null");
        if(!isPositionInBounds(position))
            throw  new  IllegalArgumentException("Position out of bounds");
        if(isDome(position))
            throw  new  IllegalArgumentException("Position is dome");

        Space targetSpace=this.board[position.getRow()][position.getColumn()];
        if(targetSpace.isFinalLevel())
            targetSpace.setLevel(targetSpace.getLevel()+1);
        else
            throw  new  IllegalArgumentException("Can't build anymore on this position");


    }

    /**
     * Removes the last block at the specified position.
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildDown(Position position) {

    }

    /**
     * @param position
     * @return the level of the building at the specified position
     * @throws IllegalArgumentException if position is null or outside the board.
     */
    public int getLevel(Position position) {
        return 1;
    }

    /**
     * Builds a dome at the specified position
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildDome(Position position) {

    }
    /**
     * Remove a dome at the specified position
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == false</code> or outside the board.
     */
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

    /**
     * @param position
     * @return adiacent positions of the specified position
     */
    public List<Position> getNeighbouringPositions(Position position) {
        return new ArrayList<Position>();
    }

    /**
     * @param nickname
     * @return all player's worker positions
     */
    public List<Position> getPlayerWorkersPositions(String nickname) {
        return new ArrayList<Position>();
    }


    private boolean isPositionInBounds(Position position) {
        return position.getRow()>=0 && position.getRow()<DIMENSION
                && position.getColumn()>=0 && position.getColumn()<DIMENSION;
    }


}

