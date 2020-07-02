package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.View;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.SocketConnection;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.ConfigCodes;
import it.polimi.ingsw.psp44.util.R;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class StartupView extends View implements Initializable {

    private static final String DEFAULT_HOSTNAME = R.getAppProperties().get(ConfigCodes.HOSTNAME);
    private static final int DEFAULT_PORT = Integer.parseInt(R.getAppProperties().get(ConfigCodes.PORT));
    @FXML
    private VBox container;
    @FXML
    private Label infoLabel;
    @FXML
    private Button connectButton;
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;

    public void start() {
        ViewScene.setViewAndShow("Santorini", "/gui/startup.fxml", this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectButton.setOnAction(this::startGame);
        Platform.runLater( () -> container.requestFocus() );

    }

    private void startGame(ActionEvent actionEvent) {
        int port = portField.getText().isEmpty() ? DEFAULT_PORT : Integer.parseInt(portField.getText());
        String hostname = ipField.getText().isEmpty() ? DEFAULT_HOSTNAME : ipField.getText();

        try {
            Socket socket = new Socket(hostname, port);
            IConnection<String> socketConnection = new SocketConnection(socket);
            View view = new LobbyView();
            this.virtualServer = new VirtualServer(socketConnection);
            this.virtualServer.startPingTask();

            this.setErrorHandlers();

            view.setServer(this.virtualServer);

            Thread server = new Thread(this.virtualServer);

            server.start();

        } catch (IOException e) {
            infoLabel.setText("ERROR: connection refused to " + hostname);
        }

    }

    private void setErrorHandlers() {
        this.virtualServer.addErrorHandler(Message.Code.NETWORK_ERROR, () -> Platform.runLater(() -> {
            InfoView infoView = new InfoView("Network error.\n It seems there are problems on the network,\n try later maybe");
            infoView.setServer(this.virtualServer);
            ViewScene.showNewWindow("Santorini", "/gui/info.fxml", infoView);
        }));

        this.virtualServer.addErrorHandler(Message.Code.DISCONNECTED, () -> Platform.runLater(() -> {
            InfoView infoView = new InfoView("The server kicked you out,\n the game was forcefully ended.");
            infoView.setServer(this.virtualServer);
            ViewScene.showNewWindow("Santorini", "/gui/info.fxml", infoView);
        }));
    }

    @Override
    public void setServer(VirtualServer virtualServer) {
//        the first view does not need a virtual server to be setted, it creates the virtual server itself
    }
}
