package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.ISetupView;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.message.Message;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SetupView implements Initializable, ISetupView {


    private VirtualServer virtualServer;
    private String playerNickname;

    public SetupView(String playerNickname){
        this.playerNickname = playerNickname;
    }

    @Override
    public void chooseCardsFrom(Message cards) {
        //SetupView.launch(SetupView.class);
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

    }
}
