package it.polimi.ingsw.psp44.client.gui.custom;

import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.util.R;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CellPane extends StackPane implements Initializable {
    private final String BUILDING_PREFIX = "BUILDING";
    private final String DOME = "DOME";
    private final String WORKER_PREFIX = "WORKER";

    @FXML private Pane layer1;
    @FXML private Pane layer2;
    @FXML private Pane layer3;
    @FXML private Pane layer4;
    @FXML private StackPane root;

    private Map<Integer, Pane> layers;

    public CellPane(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/custom/CellPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        layers = new HashMap<>();
        this.disableProperty().set(true);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setCell(Cell cell, Map<String, String> playerColors) {
        //TODO: sistemare questa cosa
        for(Pane layer : layers.values()){
            layer.setStyle("-fx-background-image: null;");
        }

        if(!cell.isEmpty() && cell.getLevel() == 0) {
            String worker = R.getAssetPathProperties().get(WORKER_PREFIX+cell.getSex()+playerColors.get(cell.getPlayerNickname()));
            layers.get(2).setStyle("-fx-background-image: url('" + worker + "'); ");
            return;
        }

        if(cell.isDome() && cell.getLevel() == 0) {
            String dome = R.getAssetPathProperties().get(DOME);
            layers.get(2).setStyle("-fx-background-image: url('" + dome + "'); ");
            return;
        }

        for(int level= 1; level <= cell.getLevel(); level++){
            String building = R.getAssetPathProperties().get(BUILDING_PREFIX+level);
            layers.get(level).setStyle("-fx-background-image: url('" + building + "'); ");
        }


        if(cell.isDome()){
            String dome = R.getAssetPathProperties().get(DOME);
            layers.get(cell.getLevel()+1).setStyle("-fx-background-image: url('" + dome + "'); ");
            return;
        }

        if(!cell.isEmpty()){
            String worker = R.getAssetPathProperties().get(WORKER_PREFIX+cell.getSex()+playerColors.get(cell.getPlayerNickname()));
            layers.get(cell.getLevel()+1).setStyle("-fx-background-image: url('" + worker + "'); ");
        }
    }

    private void highlight() {
        if(this.disableProperty().get())
            root.setStyle("-fx-background-color: null;");
        else
            root.setStyle("-fx-background-color: #3773C9; -fx-opacity: 50%");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLayers();
        root.disableProperty().addListener(disable -> {
            highlight();
        });
    }

    private void initLayers(){
        layers.put(1, this.layer1);
        layers.put(2, this.layer2);
        layers.put(3, this.layer3);
        layers.put(4, this.layer4);
    }
}