package it.polimi.ingsw.psp44.client.gui.custom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class CellPane extends StackPane {
    @FXML private Pane layer1;
    @FXML private Pane layer2;
    @FXML private Pane layer3;
    @FXML private Pane layer4;

    public CellPane(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/custom/CellPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
