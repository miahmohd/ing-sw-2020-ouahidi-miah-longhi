package it.polimi.ingsw.psp44.client.gui.custom;

import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.util.R;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

    private final String BACKGROUND_IMAGE_STYLE = "-fx-background-image: url('%s');";
    private final String BACKGROUND_COLOR_STYLE = "-fx-background-color: %s;";
    private final String OPACITY_STYLE = "-fx-opacity: %s";
    private final String HIGHLIGHT_COLOR = "#3773C9";
    private final Map<Integer, Pane> layers;
    @FXML
    private Pane layer1;
    @FXML
    private Pane layer2;
    @FXML
    private Pane layer3;
    @FXML
    private Pane layer4;
    @FXML
    private StackPane root;

    public CellPane() {
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

    /**
     * Formats the cell
     *
     * @param cell         info
     * @param playerColors player colors
     */
    public void setCell(Cell cell, Map<String, String> playerColors) {
        cleanLayers();
        setLayers(cell);
        setPlayerAndDome(cell, playerColors);
    }

    private void setPlayerAndDome(Cell cell, Map<String, String> playerColors) {
        String block = null;
        Pane targetLayer;
        if (!cell.isEmpty())
            block = R.getAssetPathProperties().get(WORKER_PREFIX + cell.getSex() + playerColors.get(cell.getPlayerNickname()));
        if (cell.isDome())
            block = R.getAssetPathProperties().get(DOME);

        if (cell.getLevel() == 0)
            targetLayer = layers.get(2); //Layer 2 is just for beauty
        else
            targetLayer = layers.get(cell.getLevel() + 1);

        targetLayer.setStyle(String.format(BACKGROUND_IMAGE_STYLE, block));
    }

    private void setLayers(Cell cell) {
        String building;

        for (int level = 1; level <= cell.getLevel(); level++) {
            building = R.getAssetPathProperties().get(BUILDING_PREFIX + level);
            layers.get(level).setStyle(String.format(BACKGROUND_IMAGE_STYLE, building));
        }
    }

    private void cleanLayers() {
        for (Pane layer : layers.values()) {
            layer.setStyle(String.format(BACKGROUND_IMAGE_STYLE, null));
        }
    }

    private void highlight() {
        String color;
        String opacity;
        if (this.disableProperty().get()) {
            color = null;
            opacity = "100%";
        } else {
            color = HIGHLIGHT_COLOR;
            opacity = "50%";
        }
        root.setStyle(String.format(BACKGROUND_COLOR_STYLE, color) + String.format(OPACITY_STYLE, opacity));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLayers();
        root.disableProperty().addListener(disable -> highlight());
    }

    private void initLayers() {
        layers.put(1, this.layer1);
        layers.put(2, this.layer2);
        layers.put(3, this.layer3);
        layers.put(4, this.layer4);
    }
}