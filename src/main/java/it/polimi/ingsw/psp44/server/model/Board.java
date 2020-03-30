package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.util.Position;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int DIMENSION=5;
    private Space[][] board;

    public Board() {
        this.board = new Space[DIMENSION][DIMENSION];

        for (int row=0;row<DIMENSION;++row){
            for(int column=0;column<DIMENSION;++column){
                this.board[row][column]=new Space();
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
            throw new IllegalArgumentException("Position out of bounds");
        if(isDome(position))
            throw new IllegalArgumentException("Position is dome");

        Space targetSpace=this.board[position.getRow()][position.getColumn()];
        if(targetSpace.isFinalLevel())
            throw new IllegalArgumentException("Can't build anymore on this position");

        targetSpace.setLevel(targetSpace.getLevel()+1);

    }

    /**
     * Removes the last block at the specified position.
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildDown(Position position) {
        if(position==null)
            throw new IllegalArgumentException("Position null");
        if(!isPositionInBounds(position))
            throw new IllegalArgumentException("Position out of bounds");
        if(isDome(position))
            throw new IllegalArgumentException("Position is dome");

        Space targetSpace=this.board[position.getRow()][position.getColumn()];
        if(targetSpace.isGroundLevel())
            throw new IllegalArgumentException("You are already on the ground level");

        targetSpace.setLevel(targetSpace.getLevel()-1);
    }

    /**
     * @param position
     * @return the level of the building at the specified position
     * @throws IllegalArgumentException if position is null or outside the board.
     */
    public int getLevel(Position position) {
        if(position==null)
            throw new IllegalArgumentException("Position null");
        if(!isPositionInBounds(position))
            throw  new  IllegalArgumentException("Position out of bounds");

        Space targetSpace=this.board[position.getRow()][position.getColumn()];

        return targetSpace.getLevel();
    }

    /**
     * Builds a dome at the specified position
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildDome(Position position) {
        if(position==null)
            throw new IllegalArgumentException("Position null");
        if(!isPositionInBounds(position))
            throw new IllegalArgumentException("Position out of bounds");
        if(isDome(position))
            throw new IllegalArgumentException("Position is dome");

        Space targetSpace=this.board[position.getRow()][position.getColumn()];
        targetSpace.setDome(true);
    }
    /**
     * Remove a dome at the specified position
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == false</code> or outside the board.
     */
    public void removeDome(Position position) {
        if(position==null)
            throw new IllegalArgumentException("Position null");
        if(!isPositionInBounds(position))
            throw new IllegalArgumentException("Position out of bounds");
        if(!isDome(position))
            throw new IllegalArgumentException("Position is not dome");

        Space targetSpace=this.board[position.getRow()][position.getColumn()];
        targetSpace.setDome(false);
    }

    public boolean isDome(Position position) {
        if(position==null)
            throw new IllegalArgumentException("Position null");
        if(!isPositionInBounds(position))
            throw new IllegalArgumentException("Position out of bounds");

        Space targetSpace=this.board[position.getRow()][position.getColumn()];
        return targetSpace.isDome();
    }

    public void setWorker(Position position, Worker worker) {
        if(position==null)
            throw new IllegalArgumentException("Position null");
        if(!isPositionInBounds(position))
            throw new IllegalArgumentException("Position out of bounds");
        if(isDome(position))
            throw new IllegalArgumentException("Position is dome");

        Space targetSpace=this.board[position.getRow()][position.getColumn()];
        targetSpace.setWorker(worker);
    }

    public Worker getWorker(Position position) {
        if(position==null)
            throw new IllegalArgumentException("Position null");
        if(!isPositionInBounds(position))
            throw new IllegalArgumentException("Position out of bounds");
        if(isDome(position))
            throw new IllegalArgumentException("Position is dome");

        Space targetSpace=this.board[position.getRow()][position.getColumn()];
        return targetSpace.getWorker();
    }

    /**
     * @param position
     * @return adjacent positions of the specified position
     */
    public List<Position> getNeighbouringPositions(Position position) {
        if(position==null)
            throw new IllegalArgumentException("Position null");
        if(!isPositionInBounds(position))
            throw new IllegalArgumentException("Position out of bounds");

        List<Position> neighbouringPositions = new ArrayList<>();

        int row = position.getRow();
        int column = position.getColumn();
        Position neighbourPosition;
        for(int r = -1; r < 2; r++){
            for(int c = -1; c < 2; c++){

                neighbourPosition = new Position(row+r, column+c);

                if(isPositionInBounds(neighbourPosition) && !position.equals(neighbourPosition))
                    neighbouringPositions.add(neighbourPosition);

            }
        }

        return neighbouringPositions;
    }

    /**
     * @param nickname
     * @return all player's worker positions
     */
    public List<Position> getPlayerWorkersPositions(String nickname) {
        List<Position> playerWorkerPositions = new ArrayList<>();
        Worker selectedWorker;
        Position selectedPosition;
        for (int row = 0; row < DIMENSION; row++){
            for (int column = 0; column < DIMENSION; column++){
                selectedPosition = new Position(row, column);
                selectedWorker = this.getWorker(selectedPosition);

                if(selectedWorker != null && nickname.equals(selectedWorker.getPlayerNickname()))
                    playerWorkerPositions.add(selectedPosition);
            }
        }

        return playerWorkerPositions;
    }


    private boolean isPositionInBounds(Position position) {
        return position.getRow()>=0 && position.getRow()<DIMENSION
                && position.getColumn()>=0 && position.getColumn()<DIMENSION;
    }


}

