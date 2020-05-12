package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.cli.Graphics.Color;
import it.polimi.ingsw.psp44.network.communication.Action;
import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.util.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private static final int DIMENSION = 5;


    private final Cell[][] cells;
    private final Map<String, Graphics.Color> playerColors;
    private final Map<Integer, Graphics.Color> levelColors;
    private final StringBuffer sb;


    public Board() {
        this.cells = new Cell[DIMENSION][DIMENSION];
        this.playerColors = new HashMap<>();
        this.levelColors = new HashMap<>();
        this.sb = new StringBuffer();

        initBoard();
        initLevelColors();
    }


    /**
     * Sets the color of the players in the game, colors cannot be chosen
     * They are set automatically
     *
     * @param myPlayer  this client's player
     * @param opponents this client's opponents
     */
    public void setPlayers(String myPlayer, List<String> opponents) {
        Color[] opponentColors = {Color.OPPONENT_1, Color.OPPONENT_2};
        int count = 0;

        this.playerColors.put(myPlayer, Color.MY_PLAYER);

        for (String opponent : opponents) {
            this.playerColors.put(opponent, opponentColors[count]);
            count++;
        }

    }

    public String getPlayers() {
        StringBuffer players = new StringBuffer();
        for (String player : this.playerColors.keySet()) {
            players.append(this.playerColors.get(player));
            players.append(player);
            players.append(Graphics.Behaviour.NEW_LINE);
        }
        players.append(Color.RESET);
        return players.toString();
    }

    /**
     * @param actionsPerPosition
     * @return String formatted according to Graphics specification standard
     */
    public String highlightActions(Map<Position, List<Action>> actionsPerPosition) {
        Position currentPosition;
        Cell currentCell;
        List<Action> currentActionList;

        StringBuffer sb = new StringBuffer();

        for (int row = 0; row < DIMENSION; row++) {
            for (int column = 0; column < DIMENSION; column++) {
                currentPosition = new Position(row, column);
                currentCell = cells[row][column];

                if (actionsPerPosition.keySet().contains(currentPosition)) {

                    currentActionList = actionsPerPosition.get(currentPosition);

                    if (currentActionList.size() > 1)
                        sb.append(Color.BOTH_HIGHLIGHT);
                    else if (currentActionList.get(0).isBuild())
                        sb.append(Color.BUILD_HIGHLIGHT);
                    else
                        sb.append(Color.MOVE_HIGHLIGHT);

                } else {
                    sb.append(this.levelColors.get(currentCell.getLevel()));
                }
                sb.append(getStringFromCell(currentCell));
            }
            sb.append(Graphics.Behaviour.NEW_LINE);
        }

        return sb.toString();
    }

    /**
     * Updated cells in the board
     *
     * @param cellsToUpdate cells that need to be updated
     * @return String formatted according to Graphics specification standard
     */
    public String update(Cell[] cellsToUpdate) {
        Position positionToUpdate;
        for (Cell cellToUpdate : cellsToUpdate) {
            positionToUpdate = cellToUpdate.getPosition();
            cells[positionToUpdate.getRow()][positionToUpdate.getColumn()] = cellToUpdate;
        }
        return this.getBoard();
    }

    /**
     * Method that generates the string board representation.
     *
     * @return String formatted according to Graphics specification standard
     */
    public String getBoard() {
        Cell currentCell;
        cleanBoard();
        for (int row = 0; row < DIMENSION; row++) {
            for (int column = 0; column < DIMENSION; column++) {
                currentCell = cells[row][column];
                sb.append(this.levelColors.get(currentCell.getLevel()));
                sb.append(getStringFromCell(currentCell));
            }
            sb.append(Graphics.Behaviour.NEW_LINE);
        }
        sb.append(Color.RESET);

        return sb.toString();
    }

    public String highlightPositions(List<Position> positionsToHighlight) {
        Cell currentCell;
        Position currentPosition;
        cleanBoard();
        for (int row = 0; row < DIMENSION; row++) {
            for (int column = 0; column < DIMENSION; column++) {
                currentPosition = new Position(row, column);
                currentCell = cells[row][column];

                if (positionsToHighlight.contains(currentPosition))
                    sb.append(Color.POSITION_HIGHLIGHT);
                else
                    sb.append(this.levelColors.get(currentCell.getLevel()));

                sb.append(getStringFromCell(currentCell));

            }
            sb.append(Graphics.Behaviour.NEW_LINE);
        }
        sb.append(Color.RESET);

        return sb.toString();
    }

    private void initLevelColors() {
        this.levelColors.put(0, Color.GROUND_LEVEL);
        this.levelColors.put(1, Color.FIRST_LEVEL);
        this.levelColors.put(2, Color.SECOND_LEVEL);
        this.levelColors.put(3, Color.THIRD_LEVEL);
    }

    private void initBoard() {
        for (int row = 0; row < DIMENSION; row++) {
            for (int column = 0; column < DIMENSION; column++) {
                cells[row][column] = new Cell();
            }
        }
    }

    private StringBuffer getStringFromCell(Cell cell) {
        StringBuffer cellBuffer = new StringBuffer();

        if (cell.isEmpty()) {
            if (cell.isDome()) {
                cellBuffer.append(Graphics.Color.DOME);
                cellBuffer.append(Graphics.Element.DOME);
            } else
                cellBuffer.append(Graphics.Element.EMPTY);
        } else {
            cellBuffer.append(playerColors.get(cell.getPlayerNickname()));
            cellBuffer.append(Graphics.Element.MALE_WORKER);
        }

        return cellBuffer;
    }

    private void cleanBoard() {
        sb.delete(0, sb.length());
    }

}