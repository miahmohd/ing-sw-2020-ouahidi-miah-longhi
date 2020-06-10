package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.ISetupView;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.client.gui.properties.SetupProperty;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.communication.BodyTemplates;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.Card;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

//https://stackoverflow.com/questions/47511132/javafx-custom-listview
public class SetupView implements Initializable, ISetupView {

    private VirtualServer virtualServer;
    private String playerNickname;
    private int cardinality;

    @FXML
    //https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/list-view.htm#CEGGEDBF
    public ListView<Card> cardList;
    public ListView<Card> chosenCards;
    public Button startButton;
    public VBox chosenCardsSection;


    private SimpleListProperty<Card> chooseCardsProperty;
    private SimpleListProperty<Card> chosenCardsProperty;
    private SimpleBooleanProperty isStartGame;
    private SimpleBooleanProperty isNotStartGame;
    private SimpleBooleanProperty visibleAndManaged;


    public SetupView(String playerNickname){
        this.playerNickname = playerNickname;

        this.chooseCardsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.chosenCardsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.isStartGame = new SimpleBooleanProperty(false);
        this.isNotStartGame = new SimpleBooleanProperty(true);

        this.visibleAndManaged = new SimpleBooleanProperty(true);
    }

    //TODO: MERGE THESE TWO
    @Override
    public void chooseCardsFrom(Message cards) {
        this.cardinality = Integer.parseInt(
                cards.getHeader().get(MessageHeader.CARDINALITY));

        chooseCardsProperty.addAll(Arrays.asList(
                BodyFactory.fromCards(cards.getBody())
        ));
    }

    @Override
    public void chooseCardFrom(Message cards) {
        this.cardinality = 1;
        chooseCardsProperty.addAll(Arrays.asList(
                BodyFactory.fromCards(cards.getBody())
        ));
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
        cardList.itemsProperty().bindBidirectional(chooseCardsProperty);
        chosenCards.itemsProperty().bindBidirectional(chosenCardsProperty);

        cardList.disableProperty().bindBidirectional(isStartGame);
        startButton.disableProperty().bindBidirectional(isNotStartGame);

        chosenCardsSection.visibleProperty().bindBidirectional(visibleAndManaged);
        chosenCardsSection.managedProperty().bindBidirectional(visibleAndManaged);

        startButton.setOnAction(this::startGame);

        cardList.setCellFactory(cardListView -> {
            CardListViewCell card = new CardListViewCell();

            card.setOnMouseClicked(mouseEvent -> {
                visibleAndManaged.set(false);

                Card selectedCard = card.getItem();
                chooseCardsProperty.remove(selectedCard);
                chosenCardsProperty.add(selectedCard);
                cardList.getSelectionModel().clearSelection();

                if(chosenCardsProperty.size() > 0) visibleAndManaged.set(true);

                if(chosenCardsProperty.size() == cardinality){
                    isStartGame.set(true);
                    isNotStartGame.set(false);
                }
            });
            return card;
        });


        chosenCards.setCellFactory(cardListView -> {
            ChosenCardListViewCell card = new ChosenCardListViewCell();
            card.setOnMouseClicked(mouseEvent -> {
                Card selectedCard = card.getItem();
                chosenCardsProperty.remove(selectedCard);
                chooseCardsProperty.add(selectedCard);
                chosenCards.getSelectionModel().clearSelection();

                if(chosenCardsProperty.size() == 0) visibleAndManaged.set(true);

                if(chosenCardsProperty.size() < cardinality){
                    isStartGame.set(false);
                    isNotStartGame.set(true);
                }

            });

            return card;
        });


    }

    private void startGame(ActionEvent actionEvent){
        String body;
        Message message;
        Message.Code messageCode;


        if(cardinality == 1){
            messageCode = Message.Code.CHOSEN_CARD;
            body = BodyFactory.toCard(chosenCardsProperty.stream().findFirst().get());

        }else {
            messageCode = Message.Code.CHOSEN_CARDS;
            body = BodyFactory.toCards(chosenCardsProperty.toArray(Card[]::new));
        }
        message = new Message(messageCode, body);
        virtualServer.sendMessage(message);
    }
}
