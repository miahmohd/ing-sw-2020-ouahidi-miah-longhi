package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.IGameView;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.client.gui.custom.BoardPane;
import it.polimi.ingsw.psp44.client.gui.custom.PlayerAndCard;
import it.polimi.ingsw.psp44.client.gui.custom.PlayerAndCardListViewCell;
import it.polimi.ingsw.psp44.network.communication.Action;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.communication.BodyTemplates;
import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.Position;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class GameView implements IGameView, Initializable {

    private final int DIMENSION = 5;
    private VirtualServer virtualServer;

    private SimpleBooleanProperty isTurnEndable;

    private int cardinality;
    private List<Position> workerPositions;

    private SimpleListProperty<PlayerAndCard> playersAndCards;

    private Map<Position, List<Action>> actionsPerPosition;

    @FXML private BoardPane board;
    @FXML private Button playersButton;
    @FXML private ListView<PlayerAndCard> playersList;
    @FXML private Button endTurnButton;

    public GameView(String playerNickname, BodyTemplates.PlayerCard[] playersAndCards) {
        String[] colors = {"BLUE", "GREY", "WHITE"};
        this.playersAndCards = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.isTurnEndable = new SimpleBooleanProperty(true);

        int i = 0;
        for(BodyTemplates.PlayerCard playerAndCard : playersAndCards){
            this.playersAndCards.add(new PlayerAndCard(playerAndCard, colors[i]));
            i++;
        }
    }

    @Override
    public void chooseWorkersInitialPositionFrom(Message workers) {
        List<Position> positionsToChooseFrom = new ArrayList<>(
                Arrays.asList(BodyFactory.fromPositions(workers.getBody()))
        );
        board.setActionIn(positionsToChooseFrom, this::sendWorkers);
        cardinality = 2;
        workerPositions = new ArrayList<>();
    }

    @Override
    public void chooseWorkerFrom(Message workers) {
        List<Position> positionsToChooseFrom = new ArrayList<>(
                Arrays.asList(BodyFactory.fromPositions(workers.getBody()))
        );
        board.setActionIn(positionsToChooseFrom, this::sendWorker);
        cardinality = 1;
        workerPositions = new ArrayList<>();
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

        if(Boolean.parseBoolean(headers.get(MessageHeader.IS_TURN_ENDABLE)))
            isTurnEndable.set(false);
    }
    @Override
    public void start(Message start) {
        board.disableProperty().set(false);
    }

    @Override
    public void end(Message end) {
        board.disableAllCells();
    }

    @Override
    public void lost(Message lost) {
        //TODO: NON FUNZIA
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "You won, congratulations");
        alert.showAndWait();
    }

    @Override
    public void won(Message won) {
        //TODO: NON FUNZIA
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "You lost, looser");
        alert.showAndWait();
    }

    @Override
    public void update(Message update) {
        Cell[] cellsToUpdate = BodyFactory.fromCells(update.getBody());
        board.update(cellsToUpdate);
    }
    @Override
    public void activeTurn(Message activePlayer) {
/*        Platform.runLater(() ->
            turnProperty.set(String.format("%s's turn",activePlayer.getBody()))
        );*/
    }
    @Override
    public void setServer(VirtualServer virtual) {
        this.virtualServer = virtual;

        virtualServer.cleanMessageHandlers();

        virtualServer.addMessageHandler(Message.Code.START_TURN, this::start);
        virtualServer.addMessageHandler(Message.Code.END_TURN, this::end);
        virtualServer.addMessageHandler(Message.Code.CHOOSE_WORKER, this::chooseWorkerFrom);
        virtualServer.addMessageHandler(Message.Code.UPDATE, this::update);
        virtualServer.addMessageHandler(Message.Code.CHOOSE_ACTION, this::chooseActionFrom);
        virtualServer.addMessageHandler(Message.Code.WON, this::won);
        virtualServer.addMessageHandler(Message.Code.LOST, this::lost);
        virtualServer.addMessageHandler(Message.Code.CHOOSE_WORKERS_INITIAL_POSITION, this::chooseWorkersInitialPositionFrom);
        virtualServer.addMessageHandler(Message.Code.UPDATE, this::update);
        virtualServer.addMessageHandler(Message.Code.ACTIVE_TURN, this::activeTurn);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        board.setDimension(DIMENSION);
        board.setPlayerColors(this.playersAndCards);

        playersList.visibleProperty().set(playersButton.isHover());
        playersList.managedProperty().set(playersButton.isHover());

        playersList.itemsProperty().bindBidirectional(this.playersAndCards);

        endTurnButton.disableProperty().bindBidirectional(this.isTurnEndable);
        endTurnButton.setOnMouseClicked(this::sendNoAction);

        playersButton.hoverProperty().addListener((invalidationListener)-> {
            playersList.visibleProperty().set(playersButton.isHover());
            playersList.managedProperty().set(playersButton.isHover());
        });

        playersList.setCellFactory(playerCardListView -> new PlayerAndCardListViewCell());

        //TODO: sistemare questa cosa
        playersList.setMaxHeight(260);
    }

    private void sendNoAction(MouseEvent mouseEvent) {
        Map<MessageHeader, String> headersToSend = new EnumMap<>(MessageHeader.class);
        headersToSend.put(MessageHeader.IS_TURN_END, String.valueOf(true));
        this.isTurnEndable.set(true);
        this.virtualServer.sendMessage(new Message(Message.Code.CHOSEN_ACTION, headersToSend, null));
    }

    private void sendAction(MouseEvent actionEvent){
        Position actionPosition = getEventPosition(actionEvent);

        List<Action> actions = this.actionsPerPosition.get(actionPosition);

        Action chosenAction = actions.stream().findFirst().orElse(null);
        if(actions.size() > 1){
            // create a choice dialog
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

    private void sendWorker(MouseEvent actionEvent){
        Position actionPosition = getEventPosition(actionEvent);

        workerPositions.add(actionPosition);
        board.disableCell(actionPosition.getRow(), actionPosition.getColumn());

        if(workerPositions.size() == this.cardinality){
            virtualServer.sendMessage(new Message(Message.Code.CHOSEN_WORKER,
                    BodyFactory.toPosition(actionPosition)));
        }
    }

    private void sendWorkers(MouseEvent actionEvent){
        Position actionPosition = getEventPosition(actionEvent);
        workerPositions.add(actionPosition);
        board.disableCell(actionPosition.getRow(), actionPosition.getColumn());

        if(workerPositions.size() == this.cardinality) {
            virtualServer.sendMessage(new Message(Message.Code.CHOSEN_WORKERS_INITIAL_POSITION,
                    BodyFactory.toPositions(workerPositions.toArray(Position[]::new))));
        }
    }

    private Position getEventPosition(MouseEvent actionEvent) {
        Node node = (Node)actionEvent.getSource();

        int row = GridPane.getColumnIndex(node);
        int column = GridPane.getRowIndex(node);
        return new Position(row, column);
    }
}
