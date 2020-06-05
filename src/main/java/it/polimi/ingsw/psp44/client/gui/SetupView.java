package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.ISetupView;
import it.polimi.ingsw.psp44.client.VirtualServer;
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
import javafx.scene.control.SelectionMode;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

//https://stackoverflow.com/questions/47511132/javafx-custom-listview
public class SetupView implements Initializable, ISetupView {

    private VirtualServer virtualServer;
    private String playerNickname;
    private int cardinality;

    @FXML
    //https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/list-view.htm#CEGGEDBF
    public ListView<String> cardList;
    public ListView<String> chosenCards;
    public Button clearButton;
    public Button startButton;



    private SimpleListProperty<String> chooseCardsProperty;
    private SimpleListProperty<String> chosenCardsProperty;
    private SimpleBooleanProperty isStartGame;
    private SimpleBooleanProperty isNotStartGame;

    private Card[] chooseCards;

    public SetupView(String playerNickname){
        this.playerNickname = playerNickname;

        this.chooseCardsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.chosenCardsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.isStartGame = new SimpleBooleanProperty(false);
        this.isNotStartGame = new SimpleBooleanProperty(true);
    }

    //TODO: MERGE THESE TWO
    @Override
    public void chooseCardsFrom(Message cards) {
        this.cardinality = Integer.parseInt(
                cards.getHeader().get(MessageHeader.CARDINALITY));

        this.chooseCards = BodyFactory.fromCards(cards.getBody());

        for (Card card : this.chooseCards)
            chooseCardsProperty.add(card.getTitle());
    }

    @Override
    public void chooseCardFrom(Message cards) {
        this.cardinality = 1;
        this.chooseCards = BodyFactory.fromCards(cards.getBody());
        for (Card card : this.chooseCards)
            chooseCardsProperty.add(card.getTitle());
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
        cardList.disableProperty().bindBidirectional(isStartGame);

        chosenCards.itemsProperty().bindBidirectional(chosenCardsProperty);

        startButton.disableProperty().bindBidirectional(isNotStartGame);

        cardList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        cardList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    chosenCardsProperty.add(newValue);

                    if(chosenCardsProperty.size() == cardinality)
                        refreshGame();
                });
        /**
         * TODO: https://stackoverflow.com/questions/27350618/remove-item-from-listview-in-addlistener
         */

        startButton.setOnAction(this::startGame);
        clearButton.setOnAction(this::clearSelection);

    }
    private void refreshGame() {
        isStartGame.set(!isStartGame.get());
        isNotStartGame.set(!isNotStartGame.get());
    }

    private void startGame(ActionEvent actionEvent){
        String body;
        Message message;
        Message.Code messageCode;

        Stream<Card> cardStream = Arrays.stream(this.chooseCards).filter(
                card -> chosenCardsProperty.contains(card.getTitle()));


        if(cardinality == 1){
            messageCode = Message.Code.CHOSEN_CARD;
            body = BodyFactory.toCard(cardStream.findFirst().get());

        }else {
            messageCode = Message.Code.CHOSEN_CARDS;
            body = BodyFactory.toCards(cardStream.toArray(Card[]::new));
        }
        message = new Message(messageCode, body);
        virtualServer.sendMessage(message);
    }

    private void clearSelection(ActionEvent actionEvent){
        chosenCardsProperty.clear();
        isNotStartGame.set(true);
        isStartGame.set(false);
    }
}
