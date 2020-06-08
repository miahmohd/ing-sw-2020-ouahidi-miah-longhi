package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.IGameView;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.communication.Action;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
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
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class GameView implements IGameView, Initializable {

    private static final int DIMENSION = 5;
    private VirtualServer virtualServer;


    private SimpleStringProperty[][] board;
    private SimpleBooleanProperty[][] boardDisabled;
    private SimpleObjectProperty<EventHandler<ActionEvent>>[][] boardAction;

    private SimpleBooleanProperty isTurnEndable;

    private SimpleStringProperty playerAndCards;
    private int cardinality;
    private List<Position> workerPositions;

    private SimpleStringProperty turnProperty;

    private Map<Position, List<Action>> actionsPerPosition;

    @FXML
    private GridPane boardGridPane;
    @FXML
    private Label playersLabel;
    @FXML
    private Label turnLabel;
    @FXML
    private Button endTurnButton;


    public GameView(String playerNickname, String myCard, Map<String, String> opponentNamesAndCards) {
        board = new SimpleStringProperty[DIMENSION][DIMENSION];
        boardDisabled = new SimpleBooleanProperty[DIMENSION][DIMENSION];
        boardAction = new SimpleObjectProperty[DIMENSION][DIMENSION];

        isTurnEndable = new SimpleBooleanProperty(true);

        turnProperty = new SimpleStringProperty("");

        String players = String.format("%s : %s\n", playerNickname, myCard);

        for (String player : opponentNamesAndCards.keySet()){
            players += String.format("%s : %s\n", player, opponentNamesAndCards.get(player));
        }

        playerAndCards = new SimpleStringProperty(players);
        initializeAll();
    }


    @Override
    public void chooseWorkersInitialPositionFrom(Message workers) {
        List<Position> positionsToChooseFrom = new ArrayList<>(
                Arrays.asList(BodyFactory.fromPositions(workers.getBody()))
        );
        disablePositionsAndSetAction(positionsToChooseFrom, this::sendWorkers);
        cardinality = 2;
        workerPositions = new ArrayList<>();
    }
    @Override
    public void chooseWorkerFrom(Message workers) {
        List<Position> positionsToChooseFrom = new ArrayList<>(
                Arrays.asList(BodyFactory.fromPositions(workers.getBody()))
        );
        disablePositionsAndSetAction(positionsToChooseFrom, this::sendWorker);
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

        disablePositionsAndSetAction(actionsPerPosition.keySet(), this::sendAction);

        headers = actions.getHeader();
        if(headers != null)
            this.isTurnEndable.set(!Boolean.parseBoolean(headers.get(MessageHeader.IS_TURN_ENDABLE)));
        else
            this.isTurnEndable.set(true);
    }
    @Override
    public void start(Message start) {
        //TODO: questo metodo Diventa a bit inutile
        System.out.println("ekkomi nello start di gameview");
    }
    @Override
    public void end(Message end) {
        //this is utile
        disableAllBoard();
    }
    @Override
    public void lost(Message lost) {
        //TODO: todo questo
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
        for (Cell cell : cellsToUpdate){
            SimpleStringProperty stringProperty = board[cell.getPosition().getRow()][cell.getPosition().getColumn()];

            Platform.runLater(()-> {
                    if (cell.getPlayerNickname() == null)
                        stringProperty.set(String.format("%d:%s", cell.getLevel(), ""));
                    else
                        stringProperty.set(String.format("%d:%s", cell.getLevel(), cell.getPlayerNickname()));
                    }
            );
        }
    }
    @Override
    public void activeTurn(Message activePlayer) {
        Platform.runLater(() ->
            turnProperty.set(String.format("%s's turn",activePlayer.getBody()))
        );
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
        Button buttonToAdd;

        //INIZIALIZZO la board;
        for (int row = 0; row < DIMENSION; row++){
            for (int column = 0; column < DIMENSION; column++){
                //inizializzo il bottone
                buttonToAdd = new Button();
                buttonToAdd.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                buttonToAdd.textProperty().bindBidirectional(board[row][column]);

                buttonToAdd.disableProperty().bindBidirectional(boardDisabled[row][column]);

                buttonToAdd.onActionProperty().bindBidirectional(boardAction[row][column]);

                boardGridPane.add(buttonToAdd, row, column);
            }
        }
        endTurnButton.disableProperty().bindBidirectional(isTurnEndable);
        playersLabel.textProperty().bindBidirectional(playerAndCards);

        turnLabel.textProperty().bindBidirectional(turnProperty);
    }

    private void sendAction(ActionEvent actionEvent){
        int row, column;
        Node node = (Node)actionEvent.getSource();

        row = boardGridPane.getColumnIndex(node);
        column = boardGridPane.getRowIndex(node);

        Position actionPosition = new Position(row, column);

        List<Action> actions = this.actionsPerPosition.get(actionPosition);
        virtualServer.sendMessage(
                new Message(Message.Code.CHOSEN_ACTION, BodyFactory.toAction(actions.stream().findFirst().get()))
        );

    }
    private void sendWorker(ActionEvent actionEvent){
        int row, column;
        Node node = (Node)actionEvent.getSource();

        row = boardGridPane.getColumnIndex(node);
        column = boardGridPane.getRowIndex(node);


        workerPositions.add(new Position(row, column));
        boardDisabled[row][column].set(true);

        if(workerPositions.size() == this.cardinality){
            virtualServer.sendMessage(new Message(Message.Code.CHOSEN_WORKER,
                    BodyFactory.toPosition(new Position(row, column))));
        }
    }
    private void sendWorkers(ActionEvent actionEvent){
        int row, column;
        Node node = (Node)actionEvent.getSource();

        row = boardGridPane.getColumnIndex(node);
        column = boardGridPane.getRowIndex(node);

        workerPositions.add(new Position(row, column));
        boardDisabled[row][column].set(true);

        if(workerPositions.size() == this.cardinality) {
            virtualServer.sendMessage(new Message(Message.Code.CHOSEN_WORKERS_INITIAL_POSITION,
                    BodyFactory.toPositions(workerPositions.toArray(Position[]::new))));
        }
    }

    private void disableAllBoard() {
        for (int row = 0; row < DIMENSION; row++){
            for (int column = 0; column < DIMENSION; column++){
                boardDisabled[row][column].set(true);
            }
        }
    }
    private void initializeAll(){
        for (int row = 0; row < DIMENSION; row++){
            for (int column = 0; column < DIMENSION; column++){
                //inizializzo la property con EMPTY
                board[row][column] = new SimpleStringProperty("0:");
                boardDisabled[row][column] = new SimpleBooleanProperty(false);
                boardAction[row][column] = new SimpleObjectProperty<>();
            }
        }
    }
    private void disablePositionsAndSetAction(Collection<Position> positionsToChooseFrom, EventHandler<ActionEvent> action) {
        for (int row = 0; row < DIMENSION; row++) {
            for (int column = 0; column < DIMENSION; column++) {
                if(positionsToChooseFrom.contains(new Position(row, column))) {
                    boardDisabled[row][column].set(false);
                    boardAction[row][column].set(action);
                }
                else
                    boardDisabled[row][column].set(true);
            }
        }
    }
}
