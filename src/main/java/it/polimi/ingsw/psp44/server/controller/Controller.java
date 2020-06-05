package it.polimi.ingsw.psp44.server.controller;


import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.InitialPlacement;
import it.polimi.ingsw.psp44.server.view.VirtualView;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.R;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;
import it.polimi.ingsw.psp44.util.exception.ProtocolException;

import java.util.List;
import java.util.Map;

/**
 * This class implements the logic of the match.
 */

public class Controller {
    private Map<String, CardController> players;
    private Map<String, VirtualView> playerViews;
    private CardController currentPlayer;
    private VirtualView currentPlayerView;
    private GameModel model;
    private List<Action> availableActions;

    public void setVirtualViews(Map<String, VirtualView> playerViews) {
        this.playerViews = playerViews;
    }

    public void setCardControllers(Map<String, CardController> players) {
        this.players = players;
    }

    public void setModel(GameModel model) {
        this.model = model;
    }


    /**
     * Add a build filter to opponents build filter list
     *
     * @param filter to apply to opponents filter
     */
    public void appliesOpponentsBuildFilter(Filter filter, Action lastAction) {
        players.values().stream()
                .filter((cardController) -> cardController != currentPlayer)
                .forEach((cardController) -> cardController.addBuildFilter(filter, lastAction, model.getBoard()));
    }

    /**
     * Add a move filter to opponents move filter list
     *
     * @param filter to apply to opponents filter
     */
    public void appliesOpponentsMoveFilter(Filter filter, Action lastAction) {
        players.values().stream()
                .filter((cardController) -> cardController != currentPlayer)
                .forEach((cardController) -> cardController.addMoveFilter(filter, lastAction, model.getBoard()));
    }


    /**
     * Callback that handles and processes "chosen worker" message type.
     * Next the view must choose the action to perform
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information about the selected worker
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    public void chosenWorkerMessageHandler(VirtualView view, Message message) {
        Position selectedWorkerPosition;
        if (view.equals(currentPlayerView)) {
            selectedWorkerPosition = BodyFactory.fromPosition(message.getBody());
            model.setWorker(selectedWorkerPosition);
            actions();
        }
    }

    /**
     * Callback that handles and processes "chosen action" message type.
     * Next perform the action and the transition to next state
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information about the selected worker
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    public void chosenActionMessageHandler(VirtualView view, Message message) {
        Action selectedAction;
        if (view.equals(currentPlayerView)) {
            if(Boolean.parseBoolean(message.getHeader().get(MessageHeader.IS_TURN_END))) {
                end(true);
                nextTurn(false);
                return;
            }
            selectedAction = availableActions.get(BodyFactory.fromAction(message.getBody()).getId());
            model.doAction(selectedAction);
            if (currentPlayer.checkVictory(selectedAction, model.getBoard())) {
                won();
                return;
            }
            if (currentPlayer.nextState(selectedAction, model.getBoard())) {
                actions();
            } else {
                end(false);
                nextTurn(false);
            }
        }
    }

    /**
     * Callback that handles the workers initial positions chosen by the player.
     * The first position is for the female worker, the second one is for the male worker.
     * It places the workers on the board.     *
     *
     * @param view    the player that chose the card.
     * @param message message with code CHOSEN_WORKERS_INITIAL_POSITION containing information about the chose positions.
     */
    public void chosenWorkersInitialPositionsMessageHandler(VirtualView view, Message message) {
        if (currentPlayerView.equals(view)) {
            Position[] chosenPositions = BodyFactory.fromPositions(message.getBody());
            setWorkersInitialPositions(chosenPositions);
            end(false);
            init(model.isFullRound());
        }
    }


    /**
     * The initialization phase of the match. Each player has to place his workers
     * when each player has placed his workers start with the first game turn
     *
     * @param gameReady all player's workers have been placed, the game can start
     */
    public void init(boolean gameReady) {
        if (gameReady)
            nextTurn(false);
        else {
            start();
            initialsWorkers();
        }

    }

    /**
     * Called each time that a turn change, if the current player has won because the previous one was blocked notice the client
     * and end the match. Otherwise sends him its workers positions
     *
     * @param hasWon if <code>true</code> the current player has won the game
     */
    private void nextTurn(boolean hasWon) {
        start();
        if (hasWon) {
            won();
        } else {
            workers();
        }
    }


    /**
     * The start of a turn player, fetch current player view and controller and send the position of his worker
     */
    private void start() {
        this.currentPlayer = players.get(model.getCurrentPlayerNickname());
        this.currentPlayerView = playerViews.get(model.getCurrentPlayerNickname());
        currentPlayerView.sendMessage(new Message(Message.Code.START_TURN));
        this.broadcastActivePlayer();
    }

    /**
     * notices all player sanding them the current player's nickname
     */
    private void broadcastActivePlayer() {
        for (VirtualView player : playerViews.values()) {
            player.sendMessage(new Message(Message.Code.ACTIVE_TURN, this.model.getCurrentPlayerNickname()));
        }
    }

    /**
     * end the turn and change the player
     */
    private void end(Boolean nextStatus) {
        if (nextStatus)
            currentPlayer.nextState(null, model.getBoard());
        currentPlayerView.sendMessage(new Message(Message.Code.END_TURN));
        model.nextTurn();
    }

    /**
     * Called when current player loose the game.
     * - 3 players match: remove current player from the game
     * - 2 players match: end the match, the opponent win
     */
    private void lost() {
        model.removePlayer(model.getCurrentPlayerNickname());
        players.remove(model.getCurrentPlayerNickname());
        currentPlayerView.sendMessage(new Message(Message.Code.LOST));
        currentPlayerView.sendMessage(new Message(Message.Code.END_TURN));
        currentPlayerView.close();
        playerViews.remove(model.getCurrentPlayerNickname());
        model.nextTurn();
        nextTurn(model.getNumberOfPlayer() == 2);
        if (model.getNumberOfPlayer() == 3) {
            end(false);
            nextTurn(false);
        } else {
            end(false);
            nextTurn(true);
        }

    }

    /**
     * Called when current player wins the match
     */
    private void won() {
        currentPlayerView.sendMessage(new Message(Message.Code.WON));
        currentPlayerView.sendMessage(new Message(Message.Code.END_TURN));
        currentPlayerView.close();
        players.remove(model.getCurrentPlayerNickname());
        playerViews.remove(model.getCurrentPlayerNickname());
        for(VirtualView player: playerViews.values()){
            player.sendMessage(new Message(Message.Code.START_TURN));
            player.sendMessage(new Message(Message.Code.LOST));
            player.sendMessage(new Message(Message.Code.END_TURN));
            player.close();
        }
        playerViews.clear();
        players.clear();
    }

    /**
     * Sends available actions to current player
     * Call lost routine if no actions are available and turn is not endable
     * Call endable routine if the turn can be ended!=
     */
    private void
    actions() {
        availableActions = currentPlayer.getAvailableAction(model.getBoard(), model.getWorker());
        Message actionsMessage = new Message(Message.Code.CHOOSE_ACTION);
        if (!availableActions.isEmpty()) {
            actionsMessage.setBody(BodyFactory.toActions(availableActions));
            if (currentPlayer.isEndableTurn())
                actionsMessage.addHeader(MessageHeader.IS_TURN_ENDABLE, String.valueOf(true));
            currentPlayerView.sendMessage(actionsMessage);
        } else {
            if (currentPlayer.isEndableTurn()) {
                end(true);
                this.nextTurn(false);
            } else
                lost();
        }
    }

    /**
     * Sends workers list to current player.
     * Call lost routine if list is empty
     */
    private void workers() {
        List<Position> workers = model.getBoard().getPlayerWorkersPositions(model.getCurrentPlayerNickname());
        Position[] workersArray = workers.toArray(Position[]::new);
        if (workers.isEmpty())
            lost();
        else {
            currentPlayerView.sendMessage(new Message(Message.Code.CHOOSE_WORKER, BodyFactory.toPositions(workersArray)));
        }
    }

    /**
     * Sends a list of unoccupied position to the current player.
     */
    private void initialsWorkers() {
        List<Position> unoccupiedPositions = model.getBoard().getUnoccupiedPosition();
        Position[] unoccupiedPositionsArray = unoccupiedPositions.toArray(Position[]::new);
        Message toSend = new Message(
                Message.Code.CHOOSE_WORKERS_INITIAL_POSITION,
                BodyFactory.toPositions(unoccupiedPositionsArray));
        currentPlayerView.sendMessage(toSend);
    }

    /**
     * Creates two initial placement actions that places the player's workers at the selected position
     * and performs it
     *
     * @param chosenPositions positions where place the current player's workers
     */
    private void setWorkersInitialPositions(Position[] chosenPositions) {
        if(chosenPositions.length!=2)
            throw new ProtocolException(String.format(R.getAppProperties().get(ErrorCodes.ILLEGAL_GAME_PARAMS), chosenPositions.length));
        String currentPlayerNickname = this.model.getCurrentPlayerNickname();
        Worker female = new Worker(currentPlayerNickname, Worker.Sex.FEMALE);
        Worker male = new Worker(currentPlayerNickname, Worker.Sex.MALE);
        InitialPlacement femalePlacement = new InitialPlacement(chosenPositions[0], female);
        InitialPlacement malePlacement = new InitialPlacement(chosenPositions[1], male);
        this.model.doAction(femalePlacement);
        this.model.doAction(malePlacement);
    }

}
