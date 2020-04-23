package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.network.VirtualView;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.modelview.Cell;
import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.Position;

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

    /**
     * The start of a turn player, fetch current player view and controller and send the position of his worker
     */
    public void start() {
        this.currentPlayer = players.get(model.getCurrentPlayerNickname());
        this.currentPlayerView = playerViews.get(model.getCurrentPlayerNickname());
        currentPlayerView.startTurn(new Message(Message.Code.START));
        List<Position> workers = model.getBoard().getPlayerWorkersPositions(model.getCurrentPlayerNickname());
        if (!workers.isEmpty())
            currentPlayerView.choseWorkerFrom(new Message(Message.Code.CHOOSE_WORKER, JsonConvert.getInstance().toJson(workers, List.class)));
        else
            lost();
    }

    /**
     * Callback that handles and processes "get worker" message type.
     * Next the view must choose the worker.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information about the selected worker
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    public void chosenWorkerMessageHandler(VirtualView view, Message message) {
        Position selectedWorkerPosition;
        List<Action> availableActions;

        if (message.getCode() == Message.Code.CHOSEN_WORKER) {
            selectedWorkerPosition = JsonConvert.getInstance().fromJson(message.getBody(), Position.class);
            model.setWorker(selectedWorkerPosition);
            availableActions = currentPlayer.getAvailableAction(model.getBoard(), model.getWorker());
            if (availableActions.isEmpty() && (!currentPlayer.isEndableTurn())) {
                lost();
            }
            if (!availableActions.isEmpty()) {
                this.availableActions = availableActions;
                currentPlayerView.choseActionFrom(new Message(Message.Code.CHOOSE_ACTION,
                        JsonConvert.getInstance().toJson(Cell.convertActionList(availableActions), List.class)));
            }
            if (currentPlayer.isEndableTurn()) {
                currentPlayerView.turnEndable(new Message(Message.Code.TURN_ENDABLE));

            }

        }

    }

    /**
     * Callback that handles and processes "worker selected" message type.
     * Next the view must choose an action.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information about the selected worker
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    public void chosenActionMessageHandler(VirtualView view, Message message) {
        Integer selectedActionIndex;
        Action selectedAction;
        List<Action> AvailableActions;
        if (message.getCode() == Message.Code.CHOSEN_ACTION) {
            selectedActionIndex = JsonConvert.getInstance().fromJson(message.getBody(), Integer.class);
            selectedAction = this.availableActions.get(selectedActionIndex);
            model.applyAction(selectedAction);
            if (selectedAction.isMovement())
                model.setWorker(selectedAction.getTargetPosition());
            if (currentPlayer.checkVictory(selectedAction, model.getBoard()))
                won();
            if (currentPlayer.nextState(selectedAction)) {
                availableActions = currentPlayer.getAvailableAction(model.getBoard(), model.getWorker());
                if (availableActions.isEmpty() && (!currentPlayer.isEndableTurn())) {
                    lost();
                }
                if (!availableActions.isEmpty()) {
                    this.availableActions = availableActions;
                    currentPlayerView.choseActionFrom(new Message(Message.Code.CHOOSE_ACTION,
                            JsonConvert.getInstance().toJson(Cell.convertActionList(availableActions), List.class)));
                }
                if (currentPlayer.isEndableTurn()) {
                    currentPlayerView.turnEndable(new Message(Message.Code.TURN_ENDABLE));
                }
            } else {
                currentPlayerView.turnEndable(new Message(Message.Code.TURN_ENDABLE));

            }
        }
    }


    /**
     * Callback that handles and processes "end turn" message type.
     * End the turn of the current player and start the turn for the next player.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information for ending the turn
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    public void endTurnMessageHandler(VirtualView view, Message message) {
        currentPlayerView.turnEnd(new Message(Message.Code.TURN_END));
        model.nextTurn();
        this.start();
    }

    /**
     * Add a build filter to opponents build filter list
     *
     * @param filter to apply to opponents filter
     */
    public void appliesOpponentsBuildFilter(Filter filter) {
        players.values().stream()
                .filter((cardController) -> cardController != currentPlayer)
                .forEach((cardController) -> cardController.addBuildFilter(filter));
    }

    /**
     * Add a move filter to opponents move filter list
     *
     * @param filter to apply to opponents filter
     */
    public void appliesOpponentsMoveFilter(Filter filter) {
        players.values().stream()
                .filter((cardController) -> cardController != currentPlayer)
                .forEach((cardController) -> cardController.addMoveFilter(filter));
    }

    public void addPlayer(String nickname, VirtualView view) {
    }

    public int getRegisteredPlayer() {
        return 5;
    }

    /**
     * Called when current player loose the game.
     * - 3 players match: remove current player from the game
     * - 2 players match: end the match, the opponent win
     */
    private void lost() {
        /* controllo se sono in 2, se  si l'altro vince. senò continuo il gioco e invio gli worker al prossimo.
                view.lost()
                // rimuovere dal model gli worker, player, ma non l'observer
                this.start() // con il prossimo player
        */
    }

    private void won() {
    }

    public void setVirtualViews(Map<String, VirtualView> playerViews) {
        this.playerViews = playerViews;
    }

    public void setCardControllers(Map<String, CardController> players) {
        this.players = players;
    }

    public void setModel(GameModel model) {
        this.model = model;
    }
}
