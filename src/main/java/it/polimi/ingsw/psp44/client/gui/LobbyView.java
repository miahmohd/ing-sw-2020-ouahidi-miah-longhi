package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.ILobbyView;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.message.Message;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;


public class LobbyView implements ILobbyView, Initializable {


    private VirtualServer virtualServer;
    private String playerNickname;

    /**
     * Some resources for binding
     * https://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm
     * https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/doc-files/introduction_to_fxml.html#instance_property_attributes
     * https://stackoverflow.com/questions/41563589/javafx-binding-expression-in-fxml-document
     * http://www.java2s.com/Tutorials/Java/JavaFX/0210__JavaFX_Binding.htm
     */
    private SimpleBooleanProperty visibleError;

    private SimpleBooleanProperty isNewGame;
    private SimpleBooleanProperty isJoinGame;

    private SimpleStringProperty nicknameText;
    private SimpleStringProperty numberOfPlayersText;
    private SimpleStringProperty gameIdText;


    @FXML
    public Label errorLabel;
    public Button joinGameButton;
    public Button newGameButton;
    public Button startButton;

    public Label numberOfPlayersLabel;
    public Label gameIdLabel;

    public TextField nicknameField;
    public TextField numberOfPlayersField;
    public TextField gameIdField;


    public LobbyView() {
        visibleError = new SimpleBooleanProperty(false);
        isNewGame = new SimpleBooleanProperty(false);
        isJoinGame = new SimpleBooleanProperty(true);

        nicknameText = new SimpleStringProperty("");
        numberOfPlayersText = new SimpleStringProperty("");
        gameIdText = new SimpleStringProperty("");
    }


    @Override
    public void newJoin(Message joinOrNew) {
        System.out.println("ho ekkkle new or join");
        View.setViewAndShow("Lobby View", "/gui/lobby.fxml", this);
    }

    @Override
    public void gameCreated(Message gameCreated) {
        System.out.println("eccoci qui");
        System.out.println(gameCreated.getBody());
        changeView();
    }

    @Override
    public void gameJoined(Message gameJoined) {
        System.out.println("eccoci qui");
        changeView();
    }

    @Override
    public void setServer(VirtualServer virtual) {
        this.virtualServer = virtual;

        this.virtualServer.addMessageHandler(Message.Code.NEW_OR_JOIN, this::newJoin);
        this.virtualServer.addMessageHandler(Message.Code.GAME_CREATED, this::gameCreated);
        this.virtualServer.addMessageHandler(Message.Code.GAME_JOINED, this::gameJoined);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newGameButton.setOnAction(this::setGameOption);
        joinGameButton.setOnAction(this::setGameOption);

        newGameButton.disableProperty().bindBidirectional(isNewGame);
        joinGameButton.disableProperty().bindBidirectional(isJoinGame);

/*        numberOfPlayersLabel.disableProperty().bindBidirectional(isNewGame);
        gameIdLabel.disableProperty().bindBidirectional(isJoinGame);*/

/*        startButton.setOnAction(this::startGame);*/

/*        nicknameField.textProperty().bindBidirectional(nicknameText);*/
/*        gameIdField.textProperty().bindBidirectional(gameIdText);
        numberOfPlayersField.textProperty().bindBidirectional(numberOfPlayersText);*/
    }

    private void setGameOption(ActionEvent actionEvent){
        isNewGame.set(!isNewGame.get());
        isJoinGame.set(!isJoinGame.get());
    }

    public void startGame(ActionEvent actionEvent){
        String body;
        Message message;
        Message.Code messageCode;

        if (isNewGame.get()){
            messageCode = Message.Code.JOIN_GAME;
            body = BodyFactory.toJoinGame(nicknameText.get(), Integer.parseInt(gameIdText.get()));
        }
        else {
            messageCode = Message.Code.NEW_GAME;
            body = BodyFactory.toNewGame(nicknameText.get(), Integer.parseInt(gameIdText.get()));
        }

        message = new Message(messageCode, body);
        this.virtualServer.sendMessage(message);
    }

    private void changeView() {
        LobbyProperty setupController = new LobbyProperty(nicknameText.get());
        setupController.setServer(this.virtualServer);
    }
}
