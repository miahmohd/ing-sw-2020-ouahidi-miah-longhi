package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.ISetupView;
import it.polimi.ingsw.psp44.client.VirtualServer;
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
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SetupView implements Initializable, ISetupView {

    private VirtualServer virtualServer;
    private String playerNickname;

    @FXML
    public ListView<Card> cardList;
    public ListView<Card> chosenCards;
    public Button startButton;

    private SetupProperty property;

    public SetupView(String playerNickname){
        this.playerNickname = playerNickname;
        this.property = new SetupProperty(false, FXCollections.observableArrayList(), FXCollections.observableArrayList(), "Play");
    }

    //TODO: MERGE THESE TWO
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
        BodyTemplates.PlayerCard[] playerCards;
        Map<String, String> opponentNamesAndCards;
        String myCard = "";

        playerCards = BodyFactory.fromPlayerCards(players.getBody());
        opponentNamesAndCards = new HashMap<>();

        for (BodyTemplates.PlayerCard playerCard : playerCards){
            if(!playerCard.getPlayerNickname().equals(this.playerNickname))
                opponentNamesAndCards.put(playerCard.getPlayerNickname() ,playerCard.getCardName());
            else
                myCard = playerCard.getCardName();
        }

        GameView gameView = new GameView(this.playerNickname, myCard, opponentNamesAndCards);
        gameView.setServer(virtualServer);

        View.setViewAndShow("Santorini", "/gui/game.fxml", gameView);
    }

    @Override
    public void start(Message start) {
        View.setViewAndShow("Setup", "/gui/setup.fxml", this);
    }

    @Override
    public void end(Message end) {
        Platform.runLater(() -> property.setStartText("Now Wait"));
        property.disableAll();
    }

    @Override
    public void setServer(VirtualServer virtual) {
        this.virtualServer = virtual;

        virtualServer.cleanMessageHandlers();

        virtualServer.addMessageHandler(Message.Code.ALL_PLAYER_CARDS, this::allPlayerCards);
        virtualServer.addMessageHandler(Message.Code.START_TURN, this::start);
        virtualServer.addMessageHandler(Message.Code.CHOOSE_CARD, this::chooseCardFrom);
        virtualServer.addMessageHandler(Message.Code.CHOOSE_CARDS, this::chooseCardsFrom);
        virtualServer.addMessageHandler(Message.Code.END_TURN, this::end);
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

    private void startGame(ActionEvent actionEvent){
        String body;
        Message message;
        Message.Code messageCode;

        Card[] chosenCards = this.property.getChosenCards();
        if(chosenCards.length == 1){
            messageCode = Message.Code.CHOSEN_CARD;
            body = BodyFactory.toCard(chosenCards[0]);
        }
        else {
            messageCode = Message.Code.CHOSEN_CARDS;
            body = BodyFactory.toCards(chosenCards);
        }
        message = new Message(messageCode, body);
        virtualServer.sendMessage(message);
    }
}
