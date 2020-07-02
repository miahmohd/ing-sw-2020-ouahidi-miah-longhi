package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.cli.Graphics.Color;
import it.polimi.ingsw.psp44.network.communication.Action;
import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private static final int DIMENSION = 5;

    private final Cell[][] cells;
    private final Map<String, Graphics.Color> playerColors;
    private final Map<Integer, Graphics.Color> levelColors;
    private final Map <String, String> playerCards;


    public Board() {
        this.cells = new Cell[DIMENSION][DIMENSION];
        this.playerColors = new HashMap<>();
        this.levelColors = new HashMap<>();
        this.playerCards = new HashMap<>();
        initBoard();
        initLevelColors();
    }

    /**
     * Sets the color and card for each player
     * @param myPlayer player's nickname
     * @param myCard player's card
     * @param opponentNamesAndCards opponents nicknames and their respective cards
     */
    public void setPlayersAndCards(String myPlayer, String myCard, Map<String, String> opponentNamesAndCards) {
        Color[] opponentColors = {Color.OPPONENT_1, Color.OPPONENT_2};
        int count = 0;

        this.playerColors.put(myPlayer, Color.MY_PLAYER);
        this.playerCards.put(myPlayer, myCard);

        for (String opponent : opponentNamesAndCards.keySet()) {
            this.playerColors.put(opponent, opponentColors[count]);
            this.playerCards.put(opponent, opponentNamesAndCards.get(opponent));
            count++;
        }
    }

    /**
     * @return a formatted string of players and their respective cards
     */
    public String getPlayers() {
        StringBuilder players = new StringBuilder();
        for (String player : this.playerColors.keySet()) {
            players.append(this.playerColors.get(player));
            players.append(player);
            players.append(Graphics.Element.SEPARATOR);
            players.append(this.playerCards.get(player));
            players.append(Graphics.Behaviour.NEW_LINE);
        }
        players.append(Color.RESET);
        return players.toString();
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
     * @return String formatted according to Graphics specification standard
     */
    public String getBoard() {
        Cell currentCell;
        StringBuilder sb = new StringBuilder();
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

    /**
     * Highlights positions specified by the parameter
     * @param positionsToHighlight positions to Highlight
     * @return a formatted representation of the board according to Graphics specs
     */
    public String highlightPositions(Collection<Position> positionsToHighlight) {
        Cell currentCell;
        Position currentPosition;

        StringBuilder sb = new StringBuilder();

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

    /**
     * Gets the legend of the board
     * for each level the associated color
     * @return string representation of legend
     */
    public String getLegend() {
        String LEVEL_PREFIX = "level";
        StringBuilder sb = new StringBuilder();

        for (int level : this.levelColors.keySet()) {
            sb.append(this.levelColors.get(level));
            sb.append(LEVEL_PREFIX);
            sb.append(level);
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
            if (cell.getSex() == Worker.Sex.MALE)
                cellBuffer.append(Graphics.Element.MALE_WORKER);
            else
                cellBuffer.append(Graphics.Element.FEMALE_WORKER);
        }

        return cellBuffer;
    }

}