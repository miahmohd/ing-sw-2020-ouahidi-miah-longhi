package it.polimi.ingsw.psp44.client.gui.custom;

import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.util.Position;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BoardPane extends GridPane {
    private final SimpleIntegerProperty dimension;
    private final Map<String, String> playerColors;
    private CellPane[][] cells;

    public BoardPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/custom/BoardPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        dimension = new SimpleIntegerProperty();
        this.playerColors = new HashMap<>();

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public int getDimension() {
        return this.dimension.get();
    }

    public void setDimension(int dimension) {
        this.dimension.set(dimension);
        init();
    }

    public IntegerProperty dimensionProperty() {
        return this.dimension;
    }

    private void init() {
        initConstrains();
        initBoard();
    }

    private void initBoard() {
        cells = new CellPane[dimension.get()][dimension.get()];

        for (int row = 0; row < this.dimension.get(); row++) {
            for (int column = 0; column < this.dimension.get(); column++) {
                cells[row][column] = new CellPane();
                this.add(cells[row][column], column, row);
            }
        }
    }

    private void initConstrains() {
        ColumnConstraints columnConstraint = this.getColumnConstraints().stream().findAny().orElse(new ColumnConstraints());
        RowConstraints rowConstraint = this.getRowConstraints().stream().findAny().orElse(new RowConstraints());

        for (int i = 1; i < this.dimension.get(); i++) {
            this.getRowConstraints().add(rowConstraint);
            this.getColumnConstraints().add(columnConstraint);
        }
    }

    /**
     * Binds an action to a specific collection of positions on BoardPane
     *
     * @param positionsToChooseFrom positions to activate
     * @param sendWorkers           action to attach to the positions
     */
    public void setActionIn(Collection<Position> positionsToChooseFrom, EventHandler<MouseEvent> sendWorkers) {
        Position currentPosition;
        boolean isContained;
        for (int row = 0; row < this.dimension.get(); row++) {
            for (int column = 0; column < this.dimension.get(); column++) {
                currentPosition = new Position(row, column);
                isContained = positionsToChooseFrom.contains(currentPosition);

                cells[row][column].disableProperty().set(!isContained);
                if (isContained) cells[row][column].setOnMouseClicked(sendWorkers);
            }
        }
    }

    /**
     * Disable the specified cell at row and column
     *
     * @param row    of cell
     * @param column of cell
     */
    public void disableCell(int row, int column) {
        cells[row][column].disableProperty().set(true);
    }

    /**
     * Updates the specified cells
     *
     * @param cellsToUpdate cells to update
     */
    public void update(Cell[] cellsToUpdate) {
        for (Cell cell : cellsToUpdate)
            cells[cell.getPosition().getRow()][cell.getPosition().getColumn()].setCell(cell, this.playerColors);
    }

    /**
     * Sets players, cards and their colors to be displayed on Board
     *
     * @param playersAndCards list of players and cards
     */
    public void setPlayerColors(ObservableList<PlayerAndCard> playersAndCards) {
        for (PlayerAndCard playerAndCard : playersAndCards) {
            this.playerColors.put(playerAndCard.getPlayerNickname(), playerAndCard.getColor());
        }
    }

    /**
     * Disable all cells
     */
    public void disableAllCells() {
        for (int row = 0; row < this.dimension.get(); row++) {
            for (int column = 0; column < this.dimension.get(); column++)
                this.disableCell(row, column);
        }
    }
}
