package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.gui.custom.BoardPane;
import it.polimi.ingsw.psp44.client.gui.custom.PlayerAndCard;
import it.polimi.ingsw.psp44.client.gui.custom.PlayerAndCardListViewCell;
import it.polimi.ingsw.psp44.client.gui.properties.GameProperty;
import it.polimi.ingsw.psp44.network.communication.Action;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.communication.BodyTemplates;
import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.Position;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class GameView extends it.polimi.ingsw.psp44.client.GameView implements Initializable {

    private final int DIMENSION = 5;
    private final String[] colors = {"BLUE", "GREY", "WHITE"};

    private final GameProperty property;
    private final List<Position> workerPositions;
    private Map<Position, List<Action>> actionsPerPosition;

    @FXML
    private BoardPane board;
    @FXML
    private Button playersButton;
    @FXML
    private ListView<PlayerAndCard> playersList;
    @FXML
    private Button endTurnButton;
    @FXML
    private Label infoLabel;

    public GameView(String playerNickname, BodyTemplates.PlayerCard[] playersAndCards) {
        this.property = new GameProperty(true, FXCollections.observableArrayList(), "");
        this.playerNickname = playerNickname;
        this.workerPositions = new ArrayList<>();
        initColors(playersAndCards);
    }


    @Override
    public void chooseWorkersInitialPositionFrom(Message workers) {
        List<Position> positionsToChooseFrom = new ArrayList<>(
                Arrays.asList(BodyFactory.fromPositions(workers.getBody()))
        );
        board.setActionIn(positionsToChooseFrom, this::sendWorkers);
        Platform.runLater(() -> {
            property.infoProperty().set("choose first female and then male worker");
        });
    }

    @Override
    public void chooseWorkerFrom(Message workers) {
        List<Position> positionsToChooseFrom = new ArrayList<>(
                Arrays.asList(BodyFactory.fromPositions(workers.getBody()))
        );
        board.setActionIn(positionsToChooseFrom, this::sendWorker);
    }

    @Override
    public void chooseActionFrom(Message actions) {
        Map<MessageHeader, String> headers;

        List<Action> actionList = new ArrayList<>(Arrays.asList(
                BodyFactory.fromActions(actions.getBody())
        ));

        this.actionsPerPosition = actionList.stream().collect(groupingBy(Action::getTarget));
        board.setActionIn(actionsPerPosition.keySet(), this::sendAction);
        headers = actions.getHeader();

        this.property.isTurnEndableProperty().set(!Boolean.parseBoolean(headers.get(MessageHeader.IS_TURN_ENDABLE)));
    }

    @Override
    public void start(Message start) {
        board.disableProperty().set(false);
    }

    @Override
    public void end(Message end) {
        board.disableProperty().set(true);
        board.disableAllCells();
    }

    @Override
    public void lost(Message lost) {
        Platform.runLater(() -> {
            InfoView infoView = new InfoView("You lost!");
            infoView.setServer(this.virtualServer);
            ViewScene.showNewWindow("Santorini", "/gui/info.fxml", infoView);
        });
    }

    @Override
    public void won(Message won) {
        Platform.runLater(() -> {
            InfoView infoView = new InfoView("You won!");
            infoView.setServer(this.virtualServer);
            ViewScene.showNewWindow("Santorini", "/gui/info.fxml", infoView);
        });
    }

    @Override
    public void update(Message update) {
        Cell[] cellsToUpdate = BodyFactory.fromCells(update.getBody());
        board.update(cellsToUpdate);
    }

    @Override
    public void activeTurn(Message activePlayer) {
        String activePlayerNickname = activePlayer.getBody();
        Platform.runLater(() -> {
            String infoString;
            if (playerNickname.equals(activePlayerNickname))
                infoString = "It's Your Turn";
            else
                infoString = String.format("%s's turn", activePlayerNickname);
            this.property.infoProperty().set(infoString);
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        board.setDimension(DIMENSION);
        board.setPlayerColors(this.property.playersAndCardsProperty());

        playersList.itemsProperty().bindBidirectional(this.property.playersAndCardsProperty());
        infoLabel.textProperty().bindBidirectional(this.property.infoProperty());

        endTurnButton.disableProperty().bindBidirectional(this.property.isTurnEndableProperty());
        endTurnButton.setOnMouseClicked(this::sendNoAction);

        playersButton.hoverProperty().addListener((invalidationListener) -> {
            playersList.visibleProperty().set(playersButton.isHover());
            playersList.managedProperty().set(playersButton.isHover());
        });

        playersList.setCellFactory(playerCardListView -> new PlayerAndCardListViewCell());
        playersList.maxHeightProperty().bindBidirectional(this.property.playersAndColorsMaxHeightProperty());
    }

    private void sendNoAction(MouseEvent mouseEvent) {
        Map<MessageHeader, String> headersToSend = new EnumMap<>(MessageHeader.class);
        headersToSend.put(MessageHeader.IS_TURN_END, String.valueOf(true));
        this.property.isTurnEndableProperty().set(true);
        this.virtualServer.sendMessage(new Message(Message.Code.CHOSEN_ACTION, headersToSend, null));
    }

    private void sendAction(MouseEvent actionEvent) {
        Position actionPosition = getEventPosition(actionEvent);
        List<Action> actions = this.actionsPerPosition.get(actionPosition);
        Action chosenAction = actions.stream().findFirst().orElse(null);

        this.property.isTurnEndableProperty().set(true);
        if (actions.size() > 1) {
            ChoiceDialog<Action> chooseActionDialog = new ChoiceDialog<>(chosenAction, actions);
            chooseActionDialog.setTitle("Choose Action");
            chooseActionDialog.setHeaderText("Choose Action Boyyy");
            chooseActionDialog.showAndWait();

            chosenAction = chooseActionDialog.getSelectedItem();
        }

        virtualServer.sendMessage(
                new Message(Message.Code.CHOSEN_ACTION, BodyFactory.toAction(chosenAction))
        );
    }

    private void sendWorker(MouseEvent actionEvent) {
        Position actionPosition = getEventPosition(actionEvent);
        virtualServer.sendMessage(new Message(Message.Code.CHOSEN_WORKER,
                BodyFactory.toPosition(actionPosition)));
    }

    private void sendWorkers(MouseEvent actionEvent) {
        Position actionPosition = getEventPosition(actionEvent);
        workerPositions.add(actionPosition);
        board.disableCell(actionPosition.getRow(), actionPosition.getColumn());

        if (isComplete(workerPositions)) {
            virtualServer.sendMessage(new Message(Message.Code.CHOSEN_WORKERS_INITIAL_POSITION,
                    BodyFactory.toPositions(workerPositions.toArray(Position[]::new))));
            Platform.runLater(() -> this.property.infoProperty().set(""));
        }
    }

    private void initColors(BodyTemplates.PlayerCard[] playersAndCards) {
        int i = 0;
        for (BodyTemplates.PlayerCard playerAndCard : playersAndCards) {
            this.property.playersAndCardsProperty().add(new PlayerAndCard(playerAndCard, colors[i]));
            i++;
        }
    }

    private boolean isComplete(List<Position> workerPositions) {
        return workerPositions.size() == 2; //It's empirical
    }

    private Position getEventPosition(MouseEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();

        int row = GridPane.getColumnIndex(node);
        int column = GridPane.getRowIndex(node);
        return new Position(row, column);
    }
}
