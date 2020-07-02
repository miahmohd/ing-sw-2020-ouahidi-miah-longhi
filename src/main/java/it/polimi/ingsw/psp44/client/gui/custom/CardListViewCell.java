package it.polimi.ingsw.psp44.client.gui.custom;

import it.polimi.ingsw.psp44.util.Card;
import it.polimi.ingsw.psp44.util.R;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;

import java.io.IOException;

/**
 * ListView Card template
 */
public class CardListViewCell extends ListCell<Card> {
    @FXML private Label title;
    @FXML private Label subtitle;
    @FXML private Label description;
    @FXML private Pane godImage;
    @FXML private HBox root;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Card card, boolean empty) {
        super.updateItem(card, empty);

        if(empty || card == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {

                mLLoader = new FXMLLoader(getClass().getResource("/gui/custom/CardListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            title.setText(card.getTitle());
            description.setText(card.getDescription());
            subtitle.setText(card.getSubtitle());

            String image = getClass().getResource(String.format("/gui/assets/images/gods/%d.png", card.getId())).toExternalForm();
            godImage.setStyle("-fx-background-image: url('" + image + "'); ");

            setText(null);
            setGraphic(root);
        }
    }

    
}
