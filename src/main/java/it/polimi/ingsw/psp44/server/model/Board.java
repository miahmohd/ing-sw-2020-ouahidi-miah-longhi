package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.util.AppProperties;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.exception.ConstructionException;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int DIMENSION = 5;
    private Space[][] field;

    public Board() {
        this.field = new Space[DIMENSION][DIMENSION];

        for (int row = 0; row < DIMENSION; ++row) {
            for (int column = 0; column < DIMENSION; ++column) {
                this.field[row][column] = new Space();
            }
        }
    }


    /**
     * Builds the next block at the specified position.
     *
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildUp(Position position) {
        if (position == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.OUT_OF_BOUNDS));
        if (isDome(position))
            throw new ConstructionException(AppProperties.getInstance().getMessage(ErrorCodes.DOME_PRESENT));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        if (targetSpace.isFinalLevel())
            throw new ConstructionException(AppProperties.getInstance().getMessage(ErrorCodes.CAN_NOT_BUILD));

        targetSpace.setLevel(targetSpace.getLevel() + 1);

    }

    /**
     * Removes the last block at the specified position.
     *
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildDown(Position position) {
        if (position == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.OUT_OF_BOUNDS));
        if (isDome(position))
            throw new ConstructionException(AppProperties.getInstance().getMessage(ErrorCodes.DOME_PRESENT));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        if (targetSpace.isGroundLevel())
            throw new ConstructionException(AppProperties.getInstance().getMessage(ErrorCodes.CAN_NOT_UNBUILD));

        targetSpace.setLevel(targetSpace.getLevel() - 1);
    }

    /**
     * @param position
     * @return the level of the building at the specified position
     * @throws IllegalArgumentException if position is null or outside the board.
     */
    public int getLevel(Position position) {
        if (position == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.OUT_OF_BOUNDS));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];

        return targetSpace.getLevel();
    }

    /**
     * Builds a dome at the specified position
     *
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildDome(Position position) {
        if (position == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.OUT_OF_BOUNDS));
        if (isDome(position))
            throw new ConstructionException(AppProperties.getInstance().getMessage(ErrorCodes.DOME_PRESENT));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        targetSpace.setDome(true);
    }

    /**
     * Remove a dome at the specified position
     *
     * @param position
     * @throws IllegalArgumentException if position is null or if <code>isDome() == false</code> or outside the board.
     */
    public void removeDome(Position position) {
        if (position == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.OUT_OF_BOUNDS));
        if (!isDome(position))
            throw new ConstructionException(AppProperties.getInstance().getMessage(ErrorCodes.CAN_NOT_UNBUILD));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        targetSpace.setDome(false);
    }

    public boolean isDome(Position position) {
        if (position == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.OUT_OF_BOUNDS));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        return targetSpace.isDome();
    }

    public void setWorker(Position position, Worker worker) {
        if (position == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.OUT_OF_BOUNDS));
        if (isDome(position))
            throw new ConstructionException(AppProperties.getInstance().getMessage(ErrorCodes.DOME_PRESENT));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        targetSpace.setWorker(worker);
    }

    public Worker getWorker(Position position) {
        if (position == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.OUT_OF_BOUNDS));
        if (isDome(position))
            throw new ConstructionException(AppProperties.getInstance().getMessage(ErrorCodes.DOME_PRESENT));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        return targetSpace.getWorker();
    }

    /**
     * @param position
     * @return adjacent positions of the specified position
     */
    public List<Position> getNeighbouringPositions(Position position) {
        if (position == null)
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(AppProperties.getInstance().getMessage(ErrorCodes.OUT_OF_BOUNDS));

        List<Position> neighbouringPositions = new ArrayList<>();

        int row = position.getRow();
        int column = position.getColumn();
        Position neighbourPosition;
        for (int r = -1; r < 2; r++) {
            for (int c = -1; c < 2; c++) {
                neighbourPosition = new Position(row + r, column + c);
                if (isPositionInBounds(neighbourPosition) && !position.equals(neighbourPosition))
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
        for (int row = 0; row < DIMENSION; row++) {
            for (int column = 0; column < DIMENSION; column++) {
                selectedPosition = new Position(row, column);
                selectedWorker = this.getWorker(selectedPosition);

                if (selectedWorker != null && nickname.equals(selectedWorker.getPlayerNickname()))
                    playerWorkerPositions.add(selectedPosition);
            }
        }

        return playerWorkerPositions;
    }


    private boolean isPositionInBounds(Position position) {
        return position.getRow() >= 0 && position.getRow() < DIMENSION
                && position.getColumn() >= 0 && position.getColumn() < DIMENSION;
    }


}

