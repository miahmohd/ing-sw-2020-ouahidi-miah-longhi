package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.ISetupView;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.message.Message;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

//https://stackoverflow.com/questions/47511132/javafx-custom-listview
public class SetupView implements Initializable, ISetupView {

    private VirtualServer virtualServer;
    private String playerNickname;

    @FXML
    //https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/list-view.htm#CEGGEDBF
    public ListView<String> cardList;

    SimpleListProperty<String> ekkle;

    public SetupView(String playerNickname){
        this.playerNickname = playerNickname;
        ekkle = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    @Override
    public void chooseCardsFrom(Message cards) {
        View.setViewAndShow("Setup", "/gui/setup.fxml", this);

    }

    @Override
    public void chooseCardFrom(Message cards) {
    }

    @Override
    public void allPlayerCards(Message players) {
    }

    @Override
    public void start(Message start) {
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
        cardList.itemsProperty().bindBidirectional(ekkle);
        cardList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // Your action here
                    System.out.println("Selected item: " + newValue);
                });
    }

    private void startGame(ActionEvent actionEvent){
    }
}
