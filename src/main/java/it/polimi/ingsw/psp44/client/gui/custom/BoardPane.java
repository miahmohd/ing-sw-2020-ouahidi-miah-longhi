package it.polimi.ingsw.psp44.client.gui.custom;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;

public class BoardPane extends GridPane {
    private final SimpleIntegerProperty dimension;

    public BoardPane(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/custom/BoardPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        dimension = new SimpleIntegerProperty();
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setDimension(int dimension) {
        this.dimension.set(dimension);
        init();
    }

    public int getDimension() {
        return this.dimension.get();
    }

    public IntegerProperty dimensionProperty(){
        return this.dimension;
    }

    private void init() {
        initConstrains();
        initBoard();
    }

    private void initBoard() {
        for(int row = 0; row < this.dimension.get(); row++){
            for(int column = 0; column < this.dimension.get(); column++){
                this.add(new CellPane(), row, column);
            }
        }
    }

    private void initConstrains() {
        ColumnConstraints columnConstraint = this.getColumnConstraints().stream().findAny().orElse(new ColumnConstraints());
        RowConstraints rowConstraint = this.getRowConstraints().stream().findAny().orElse(new RowConstraints());

        for(int i = 1; i < this.dimension.get(); i++){
            this.getRowConstraints().add(rowConstraint);
            this.getColumnConstraints().add(columnConstraint);
        }
    }
}
