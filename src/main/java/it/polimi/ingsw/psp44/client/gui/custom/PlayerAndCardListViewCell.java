package it.polimi.ingsw.psp44.client.gui.custom;

import it.polimi.ingsw.psp44.util.R;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * ListView PlayerCard template
 */
public class PlayerAndCardListViewCell extends ListCell<PlayerAndCard> {

    @FXML private Label playerNickname;
    @FXML private Pane workerPane;
    @FXML private Pane godPane;
    @FXML private HBox root;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(PlayerAndCard playerCard, boolean empty) {
        super.updateItem(playerCard, empty);

        if(empty || playerCard == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {

                mLLoader = new FXMLLoader(getClass().getResource("/gui/custom/PlayerAndCardListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            playerNickname.setText(playerCard.getPlayerNickname());

            String image = getClass().getResource(String.format("/gui/assets/images/gods/%d.png", playerCard.getCardId())).toExternalForm();
            godPane.setStyle("-fx-background-image: url('" + image + "'); ");

            image =  R.getAssetPathProperties().get("WORKERFEMALE"+playerCard.getColor());
            workerPane.setStyle("-fx-background-image: url('" + image + "'); ");

            setText(null);
            setGraphic(root);
        }
    }
}
