package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.ILobbyView;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.message.Message;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LobbyView extends Application implements ILobbyView {

    private VirtualServer virtualServer;
    private String playerNickname;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent view = FXMLLoader.load(
                (getClass().getResource("/lobby.fxml")));

        Scene scene = new Scene(view, 300, 300);

        primaryStage.setTitle("Lobby View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void newJoin(Message joinOrNew) {
        launch();
    }

    @Override
    public void gameCreated(Message gameCreated) {
    }

    @Override
    public void gameJoined(Message gameJoined) {
    }


    @Override
    public void setServer(VirtualServer virtual) {
        this.virtualServer = virtual;

        virtualServer.addMessageHandler(Message.Code.NEW_OR_JOIN, this::newJoin);
        virtualServer.addMessageHandler(Message.Code.GAME_CREATED, this::gameCreated);
        virtualServer.addMessageHandler(Message.Code.GAME_JOINED, this::gameJoined);
    }
}
