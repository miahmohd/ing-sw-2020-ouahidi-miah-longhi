package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.R;
import it.polimi.ingsw.psp44.util.exception.ConstructionException;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * the playground of the match, it keep information about buildings and workers positions
 */
public class Board {
    private static final int DIMENSION = 5;
    private final Space[][] field;
    private Position selectedWorker;

    public Board() {
        this.field = new Space[DIMENSION][DIMENSION];
        this.selectedWorker = null;
        for (int row = 0; row < DIMENSION; ++row) {
            for (int column = 0; column < DIMENSION; ++column) {
                this.field[row][column] = new Space();
            }
        }
    }


    /**
     * Builds the next block at the specified position.
     *
     * @param position to build
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildUp(Position position) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));
        if (isDome(position))
            throw new ConstructionException(R.getAppProperties().getProperty(ErrorCodes.DOME_PRESENT));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        if (targetSpace.isFinalLevel())
            throw new ConstructionException(R.getAppProperties().getProperty(ErrorCodes.CAN_NOT_BUILD));

        targetSpace.setLevel(targetSpace.getLevel() + 1);

    }

    /**
     * Removes the last block at the specified position.
     *
     * @param position to remove a block
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildDown(Position position) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));
        if (isDome(position))
            throw new ConstructionException(R.getAppProperties().getProperty(ErrorCodes.DOME_PRESENT));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        if (targetSpace.isGroundLevel())
            throw new ConstructionException(R.getAppProperties().getProperty(ErrorCodes.CAN_NOT_UNBUILD));

        targetSpace.setLevel(targetSpace.getLevel() - 1);
    }

    /**
     * @param position to check the building height
     * @return the level of the building at the specified position
     * @throws IllegalArgumentException if position is null or outside the board.
     */
    public int getLevel(Position position) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];

        return targetSpace.getLevel();
    }

    /**
     * Builds a dome at the specified position
     *
     * @param position to build the dome
     * @throws IllegalArgumentException if position is null or if <code>isDome() == true</code> or outside the board.
     */
    public void buildDome(Position position) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));
        if (isDome(position))
            throw new ConstructionException(R.getAppProperties().getProperty(ErrorCodes.DOME_PRESENT));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        targetSpace.setDome(true);
    }

    /**
     * Remove a dome at the specified position
     *
     * @param position to remove the dome
     * @throws IllegalArgumentException if position is null or if <code>isDome() == false</code> or outside the board.
     */
    public void removeDome(Position position) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));
        if (!isDome(position))
            throw new ConstructionException(R.getAppProperties().getProperty(ErrorCodes.CAN_NOT_UNBUILD));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        targetSpace.setDome(false);
    }

    /**
     * Check if a dome have been built at the specified position
     *
     * @param position to check
     * @return true if there is a dome false otherwise
     */
    public boolean isDome(Position position) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        return targetSpace.isDome();
    }

    /**
     * checks if all the three blocks have been built in a space
     *
     * @param position to check
     * @return <code>true</code> if level 3 have been reached, <code>false</code> otherwise
     */
    public boolean isFinalLevel(Position position) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        return targetSpace.isFinalLevel();
    }

    /**
     * Check if there is a worker at the specified position
     *
     * @param position to check
     * @return true if there is a worker false otherwise
     */
    public boolean isWorker(Position position) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        return targetSpace.isWorker();
    }

    /**
     * Add the worker to the space at the specified position
     *
     * @param position where add the worker
     * @param worker   to add
     */
    public void setWorker(Position position, Worker worker) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));
        if (isDome(position))
            throw new ConstructionException(R.getAppProperties().getProperty(ErrorCodes.DOME_PRESENT));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        targetSpace.setWorker(worker);
    }

    /**
     * return the worker at the specified position
     *
     * @param position
     * @return the worker at the position
     */
    public Worker getWorker(Position position) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));
        if (isDome(position))
            throw new ConstructionException(R.getAppProperties().getProperty(ErrorCodes.DOME_PRESENT));

        Space targetSpace = this.field[position.getRow()][position.getColumn()];
        return targetSpace.getWorker();
    }


    /**
     * @param position to check neighbouring spaces
     * @return adjacent positions of the specified position
     */
    public List<Position> getNeighbouringPositions(Position position) {
        if (position == null)
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.NULL_POS));
        if (!isPositionInBounds(position))
            throw new IllegalArgumentException(R.getAppProperties().getProperty(ErrorCodes.OUT_OF_BOUNDS));

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
     * Filter all the spaces without dome or workers (unoccupied)
     *
     * @return a list with all the unoccupied spaces
     */
    public List<Position> getUnoccupiedPosition() {
        List<Position> unoccupiedPositions = new ArrayList<>();
        Position unoccupiedPosition;
        for (int r = 0; r < DIMENSION; r++) {
            for (int c = 0; c < DIMENSION; c++) {
                if (field[r][c].isUnoccupied())
                    unoccupiedPositions.add(new Position(r, c));

            }
        }
        return unoccupiedPositions;
    }

    /**
     * @param nickname of the player whose workers have to search for
     * @return all player's worker positions
     */
    public List<Position> getPlayerWorkersPositions(String nickname) {
        List<Position> playerWorkerPositions = new ArrayList<>();
        Worker selectedWorker;
        Position selectedPosition;
        for (int row = 0; row < DIMENSION; row++) {
            for (int column = 0; column < DIMENSION; column++) {
                selectedPosition = new Position(row, column);
                try {
                    selectedWorker = this.getWorker(selectedPosition);
                    if (selectedWorker != null && nickname.equals(selectedWorker.getPlayerNickname()))
                        playerWorkerPositions.add(selectedPosition);
                } catch (ConstructionException e) {
                    continue;
                }
            }
        }

        return playerWorkerPositions;
    }


    /**
     * Check if a position is inside the game field
     *
     * @param position to check
     * @return <code>true</code> if is inside <code>false</code> otherwise
     */
    public boolean isPositionInBounds(Position position) {
        return position.getRow() >= 0 && position.getRow() < DIMENSION
                && position.getColumn() >= 0 && position.getColumn() < DIMENSION;
    }

    /**
     * return the worker selected for this turn
     *
     * @return selected worker position
     */
    public Position getSelectedWorker() {
        return this.selectedWorker;
    }

    /**
     * Change position of the selected worker in this turn
     *
     * @param selectedWorkerPosition
     */
    public void setSelectedWorker(Position selectedWorkerPosition) {
        this.selectedWorker = selectedWorkerPosition;
    }

}

