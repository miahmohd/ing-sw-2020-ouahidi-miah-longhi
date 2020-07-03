package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.AbstractLobbyView;
import it.polimi.ingsw.psp44.client.gui.properties.LobbyProperty;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;


public class LobbyView extends AbstractLobbyView implements Initializable {
    private final LobbyProperty property;

    @FXML
    private Label infoLabel;
    @FXML
    private Button joinGameButton;
    @FXML
    private Button newGameButton;
    @FXML
    private Button startButton;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField gameOptionField;

    public LobbyView() {
        this.property = new LobbyProperty(true, "Number Of Players", "Game Id", "", "");
    }

    @Override
    public void newJoin(Message joinOrNew) {
        readHeaders(joinOrNew.getHeader());
        ViewScene.setViewAndShow("Lobby", "/gui/lobby.fxml", this);
    }

    @Override
    public void gameCreated(Message gameCreated) {
        Platform.runLater(() ->
                property.setInfoText(String.format("game created with id %s, now wait for the other players", gameCreated.getBody())));
        changeView();
    }

    @Override
    public void gameJoined(Message gameJoined) {
        Platform.runLater(() ->
                property.setInfoText("game joined, wait for the others"));
        changeView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newGameButton.setOnAction((ae) -> property.flipGameOptions());
        joinGameButton.setOnAction((ae) -> property.flipGameOptions());
        startButton.setOnAction(this::startGame);

        newGameButton.disableProperty().bindBidirectional(property.newGameProperty());
        joinGameButton.disableProperty().bindBidirectional(property.joinGameProperty());

        gameOptionField.promptTextProperty().bind(property.optionPromptTextProperty());

        gameOptionField.textProperty().bindBidirectional(property.optionTextProperty());
        nicknameField.textProperty().bindBidirectional(property.nicknameTextProperty());

        infoLabel.textProperty().bindBidirectional(property.infoTextProperty());
    }

    public void startGame(ActionEvent actionEvent) {
        String body;
        Message message;
        Message.Code messageCode;

        int gameOption;

        try {
            gameOption = Integer.parseInt(property.getOptionText());
        } catch (NumberFormatException e) {
            property.setInfoText("Provide a number please");
            return;
        }

        if (property.isNewGame()) {
            messageCode = Message.Code.NEW_GAME;
            body = BodyFactory.toNewGame(property.getNicknameText(), gameOption);
        } else {
            messageCode = Message.Code.JOIN_GAME;
            body = BodyFactory.toJoinGame(property.getNicknameText(), gameOption);
        }
        message = new Message(messageCode, body);
        this.virtualServer.sendMessage(message);
    }

    private void changeView() {
        SetupView setupController = new SetupView(property.getNicknameText());
        setupController.setServer(this.virtualServer);
    }

    private void readHeaders(Map<MessageHeader, String> header) {
        if (header == null) return;

        if (header.containsKey(MessageHeader.ERROR_DESCRIPTION))
            Platform.runLater(() -> property.setInfoText(header.get(MessageHeader.ERROR_DESCRIPTION)));
    }
}
