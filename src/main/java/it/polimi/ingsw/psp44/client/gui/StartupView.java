package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.View;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.SocketConnection;
import it.polimi.ingsw.psp44.util.ConfigCodes;
import it.polimi.ingsw.psp44.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class StartupView implements Initializable {

    @FXML private Label infoLabel;
    @FXML private Button connectButton;
    @FXML private TextField ipField;

    //Puoi cambiarlo con un run
    public void start(){
        ViewScene.setViewAndShow("Santorini", "/gui/startup.fxml", this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectButton.setOnAction(this::startGame);
    }

    private void startGame(ActionEvent actionEvent) {
        int port = Integer.parseInt(R.getAppProperties().get(ConfigCodes.PORT));
        String hostname = ipField.getText();
        try {
            Socket socket = new Socket(hostname, port);
            IConnection<String> socketConnection = new SocketConnection(socket);
            View view = new LobbyView();
            VirtualServer virtualServer = new VirtualServer(socketConnection);
            virtualServer.startPingTask();

            view.setServer(virtualServer);

            Thread server = new Thread(virtualServer);
            server.start();

            //server.join();

        } catch (IOException e) {
            infoLabel.setText("ERROR: " + e.getMessage());
        }
    }
}
