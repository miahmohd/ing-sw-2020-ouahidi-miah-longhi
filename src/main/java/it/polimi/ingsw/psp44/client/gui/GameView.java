package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.IGameView;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.client.gui.custom.BoardPane;
import it.polimi.ingsw.psp44.network.communication.Action;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.communication.BodyTemplates;
import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.Position;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class GameView implements IGameView, Initializable {

    private final int DIMENSION = 5;
    private VirtualServer virtualServer;

/*    private SimpleBooleanProperty[][] boardDisabled;
    private SimpleObjectProperty<EventHandler<ActionEvent>>[][] boardAction;*/

    private SimpleBooleanProperty isTurnEndable;

    private SimpleStringProperty playerAndCards;

    private int cardinality;
    private List<Position> workerPositions;

    private SimpleStringProperty turnProperty;

    private Map<Position, List<Action>> actionsPerPosition;
    private Map<String, String> playerColors;

    @FXML private BoardPane board;
    //@FXML private Label playersLabel;
    //@FXML private Label turnLabel;
    //@FXML private Button endTurnButton;


    public GameView(String playerNickname, BodyTemplates.PlayerCard[] playersAndCards) {
        playerColors = new HashMap<>();
        String[] colors = {"BLUE", "GREY", "WHITE"};

        int i = 0;
        for(BodyTemplates.PlayerCard playerAndCard : playersAndCards){
            playerColors.put(playerAndCard.getPlayerNickname(), colors[i]);
            i++;
        }
    }

    @Override
    public void chooseWorkersInitialPositionFrom(Message workers) {
        System.out.println("GameView.chooseWorkersInitialPositionFrom");
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
    }
    @Override
    public void start(Message start) {
        System.out.println("GameView.start");
        board.disableProperty().set(false);
    }
    @Override
    public void end(Message end) {
        board.disableProperty().set(true);
    }
    @Override
    public void lost(Message lost) {
        System.out.println("YOU LOST, looser");
    }
    @Override
    public void won(Message won) {
        //TODO: todo anche questo
        System.out.println("you won, good job, very very good job");
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
        board.setPlayerColors(playerColors);
        System.out.println("GameView.initialize");
        //endTurnButton.disableProperty().bindBidirectional(isTurnEndable);
        //playersLabel.textProperty().bindBidirectional(playerAndCards);
        //turnLabel.textProperty().bindBidirectional(turnProperty);
    }

    private void sendAction(MouseEvent actionEvent){
        int row, column;
        Node node = (Node)actionEvent.getSource();

        row = board.getColumnIndex(node);
        column = board.getRowIndex(node);

        Position actionPosition = new Position(row, column);

        List<Action> actions = this.actionsPerPosition.get(actionPosition);
        virtualServer.sendMessage(
                new Message(Message.Code.CHOSEN_ACTION, BodyFactory.toAction(actions.stream().findFirst().get()))
        );

    }
    private void sendWorker(MouseEvent actionEvent){
        int row, column;
        Node node = (Node)actionEvent.getSource();

        row = board.getColumnIndex(node);
        column = board.getRowIndex(node);

        workerPositions.add(new Position(row, column));
        board.disableCell(row, column);

        if(workerPositions.size() == this.cardinality){
            virtualServer.sendMessage(new Message(Message.Code.CHOSEN_WORKER,
                    BodyFactory.toPosition(new Position(row, column))));
        }
    }

    private void sendWorkers(MouseEvent actionEvent){
        System.out.println("GameView.sendWorkers");
        int row, column;
        Node node = (Node)actionEvent.getSource();

        row = board.getColumnIndex(node);
        column = board.getRowIndex(node);

        workerPositions.add(new Position(row, column));
        board.disableCell(row, column);

        if(workerPositions.size() == this.cardinality) {
            virtualServer.sendMessage(new Message(Message.Code.CHOSEN_WORKERS_INITIAL_POSITION,
                    BodyFactory.toPositions(workerPositions.toArray(Position[]::new))));
        }
    }
}
