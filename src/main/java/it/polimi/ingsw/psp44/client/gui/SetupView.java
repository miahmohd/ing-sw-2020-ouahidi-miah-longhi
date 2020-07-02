package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.gui.custom.CardListViewCell;
import it.polimi.ingsw.psp44.client.gui.custom.ChosenCardListViewCell;
import it.polimi.ingsw.psp44.client.gui.properties.SetupProperty;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.communication.BodyTemplates;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.Card;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class SetupView extends it.polimi.ingsw.psp44.client.SetupView implements Initializable {
    private final SetupProperty property;

    @FXML
    private ListView<Card> cardList;
    @FXML
    private ListView<Card> chosenCards;
    @FXML
    private Button startButton;


    public SetupView(String playerNickname) {
        this.playerNickname = playerNickname;
        this.property = new SetupProperty(false, FXCollections.observableArrayList(), FXCollections.observableArrayList(), "Play");
    }

    @Override
    public void chooseCardsFrom(Message cards) {
        this.property.setMaxChosenCardsSize(Integer.parseInt(
                cards.getHeader().get(MessageHeader.CARDINALITY)));
        this.property.addCards(BodyFactory.fromCards(cards.getBody()));
    }

    @Override
    public void chooseCardFrom(Message cards) {
        this.property.setMaxChosenCardsSize(1);
        this.property.addCards(BodyFactory.fromCards(cards.getBody()));
    }

    //https://docs.oracle.com/javafx/2/fxml_get_started/custom_control.htm
    @Override
    public void allPlayerCards(Message players) {
        BodyTemplates.PlayerCard[] playerCards = BodyFactory.fromPlayerCards(players.getBody());
        GameView gameView = new GameView(this.playerNickname, playerCards);
        ViewScene.setViewAndShow("Santorini", "/gui/game.fxml", gameView);

        gameView.setServer(virtualServer);
    }

    @Override
    public void start(Message start) {
        ViewScene.setViewAndShow("Setup", "/gui/setup.fxml", this);
    }

    @Override
    public void end(Message end) {
        Platform.runLater(() -> property.setStartText("Now Wait"));
        property.disableAll();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardList.itemsProperty().bindBidirectional(property.chooseCardsProperty());
        chosenCards.itemsProperty().bindBidirectional(property.chosenCardsProperty());

        cardList.disableProperty().bindBidirectional(property.isStartGameProperty());
        startButton.disableProperty().bindBidirectional(property.isNotStartGameProperty());

        chosenCards.disableProperty().bindBidirectional(property.isGameStartedProperty());
        startButton.textProperty().bindBidirectional(property.startTextProperty());
        startButton.setOnAction(this::startGame);

        cardList.setCellFactory(cardListView -> {
            CardListViewCell card = new CardListViewCell();

            card.setOnMouseClicked(mouseEvent -> {
                Card selectedCard = card.getItem();
                property.moveCardFromChooseToChosen(selectedCard);
                cardList.getSelectionModel().clearSelection();
            });
            return card;
        });

        chosenCards.setCellFactory(cardListView -> {
            ChosenCardListViewCell card = new ChosenCardListViewCell();
            card.setOnMouseClicked(mouseEvent -> {
                Card selectedCard = card.getItem();
                property.moveCardFromChosenToChoose(selectedCard);
                chosenCards.getSelectionModel().clearSelection();
            });

            return card;
        });
    }

    private void startGame(ActionEvent actionEvent) {
        String body;
        Message message;
        Message.Code messageCode;

        Card[] chosenCards = this.property.getChosenCards();
        if (chosenCards.length == 1) { //empirical
            messageCode = Message.Code.CHOSEN_CARD;
            body = BodyFactory.toCard(chosenCards[0]);
        } else {
            messageCode = Message.Code.CHOSEN_CARDS;
            body = BodyFactory.toCards(chosenCards);
        }
        message = new Message(messageCode, body);
        virtualServer.sendMessage(message);
    }
}
