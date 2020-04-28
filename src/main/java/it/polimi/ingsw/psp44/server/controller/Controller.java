package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.view.VirtualView;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.communication.Cell;
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
        currentPlayerView.sendMessage(new Message(Message.Code.START));
        workers();
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
        if (message.getCode() == Message.Code.CHOSEN_WORKER) {
            selectedWorkerPosition = JsonConvert.getInstance().fromJson(message.getBody(), Position.class);
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
        Integer selectedActionIndex;
        Action selectedAction;
        if (message.getCode() == Message.Code.CHOSEN_ACTION) {
            selectedActionIndex = JsonConvert.getInstance().fromJson(message.getBody(), Integer.class);
            selectedAction = this.availableActions.get(selectedActionIndex);
            model.applyAction(selectedAction);
            if (currentPlayer.checkVictory(selectedAction, model.getBoard()))
                won();
            if (currentPlayer.nextState(selectedAction)) {
                actions();
            } else {
                endable();
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
        currentPlayerView.sendMessage(new Message(Message.Code.TURN_END));
        model.nextTurn();
        this.start();
    }

    /**
     * Add a build filter to opponents build filter list
     *
     * @param filter to apply to opponents filter
     */
    public void appliesOpponentsBuildFilter(Filter filter, Action lastAction) {
        players.values().stream()
                .filter((cardController) -> cardController != currentPlayer)
                .forEach((cardController) -> cardController.addBuildFilter(filter, lastAction));
    }

    /**
     * Add a move filter to opponents move filter list
     *
     * @param filter to apply to opponents filter
     */
    public void appliesOpponentsMoveFilter(Filter filter, Action lastAction) {
        players.values().stream()
                .filter((cardController) -> cardController != currentPlayer)
                .forEach((cardController) -> cardController.addMoveFilter(filter, lastAction));
    }


    /**
     * Called when current player loose the game.
     * - 3 players match: remove current player from the game
     * - 2 players match: end the match, the opponent win
     */
    private void lost() {
        if(model.getNumberOfPlayer()==3){
            model.removePlayer(model.getCurrentPlayerNickname());
            model.nextTurn();
            start();
        }else{
            model.nextTurn();
            currentPlayerView=playerViews.get(model.getCurrentPlayerNickname());
            currentPlayer=players.get(model.getCurrentPlayerNickname());
            won();
        }

    }

    /**
     * Called when current player wins the match
     */
    public void won() {

    }

    /**
     * Sends available actions to current player
     * Call lost routine if no actions are available and turn is not endable
     * Call endable routine if the turn can be ended
     */
    private void actions(){
        availableActions = currentPlayer.getAvailableAction(model.getBoard(), model.getWorker());
        currentPlayerView.sendMessage(new Message(Message.Code.CHOOSE_ACTION,
                JsonConvert.getInstance().toJson(Cell.convertActionList(availableActions), List.class)));
        if (availableActions.isEmpty() && (!currentPlayer.isEndableTurn()))
            lost();
        if (currentPlayer.isEndableTurn())
            endable();
    }

    /**
     * Sends workers list to current player.
     * Call lost routine if list is empty
     */
    private void workers() {
        List<Position> workers = model.getBoard().getPlayerWorkersPositions(model.getCurrentPlayerNickname());
        currentPlayerView.sendMessage(new Message(Message.Code.CHOOSE_WORKER, JsonConvert.getInstance().toJson(workers, List.class)));
        if (workers.isEmpty())
            lost();
    }

    /**
     * Notices tha current player that the match can be ended
     */
    private void endable() {
        currentPlayerView.sendMessage(new Message(Message.Code.TURN_ENDABLE));
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
