package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.server.controller.CardController;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.network.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A class representing a single game. It is self-sustained.
 */
// FIXME fa più o meno quello che fa Controller, li uniamo?
public class Game {

    private int maxPlayers;
    private VirtualView currentView;
    // LinkedHashMap preserve insertion order for the keys, may not be necessary.
    private Map<String, VirtualView> connectedViews = Collections.synchronizedMap(new LinkedHashMap<>());


//    Attributi dell'attuale Controller
    private Map<String, CardController> players = Collections.synchronizedMap(new HashMap<>());
    private CardController currentPlayer;
    private GameModel model;
    private Position selectedWorkerPosition;
    private Action selectedAction;


    public Game(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }


    /**
     * Add a new player to the game. When the game reaches full capacity it starts automatically.
     * @param nickname
     * @param view
     */
    public void addPlayer(String nickname, VirtualView view) {
        this.connectedViews.put(nickname, view);
        if (this.isFull())
            this.setup();
    }

    public boolean containsPlayer(String nickname) {
        return this.connectedViews.containsKey(nickname);
    }

    public boolean isFull() {
        return this.connectedViews.size() == maxPlayers;
    }


    /**
     * Start the setup process and arranges the necessary objects for the gameplay.
     */
    private void setup() {
//        Todo implementazione TBD
        /*
        creazione del model
        selezione vista dei player per scelta divinità, e relativi handler (l'handler creerà anche i CardController, Player)
        selezione vista dei player per il posizionamento, ed inviare le Action per posizionamento, e relativi handler
         */
        this.start();
    }

    /**
     * Arranges the message handlers for the turn management, and start the first turn for the first player.
     */
    private void start() {
        for (Map.Entry<String, VirtualView> entry : this.connectedViews.entrySet()) {
            entry.getValue().addMessageHandler(this::workerSelectedMessageHandler);
            entry.getValue().addMessageHandler(this::actionSelectedMessageHandler);
            entry.getValue().addMessageHandler(this::endTurnMessageHandler);
            //...dopo tutti gli handler
            entry.getValue().start();
        }
        // first player start
        String currentPlayerNickname = this.model.getCurrentPlayerNickname();
        this.currentView.selectWorkerFrom(this.model.getBoard().getPlayerWorkersPositions(currentPlayerNickname));

    }


    /**
     * Callback that handles and processes "worker selected" message type.
     * Next the view must choose an action.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information about the selected worker
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    private Boolean workerSelectedMessageHandler(VirtualView view, Message message) {
        if (message.getHeader() == "worker selected") {
//            this.selectedWorkerPosition = json.parse(message.getBody()); todo parse from json

            view.selectActionFrom(this.currentPlayer.getAction(this.model.getBoard(), this.selectedWorkerPosition));

            return true;
        }
        return false;
    }


    /**
     * Callback that handles and processes "action selected" message type.
     * Next the view can choose an action, or end the turn.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information about the selected action
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    private Boolean actionSelectedMessageHandler(VirtualView view, Message message) {
        if (message.getHeader() == "action selected") {
//            this.selectedAction = json.parse( message.getBody()); todo parse from json
            this.selectedAction = null;
            this.model.applyAction(this.selectedAction); // notificherà automatichamente le view per aggiornare l'interfaccia
            this.currentPlayer.nextState(this.selectedAction);

            view.selectActionFrom(this.currentPlayer.getAction(this.model.getBoard(), this.selectedWorkerPosition)); // la virtual view può sceglere la prossima action

            if (this.currentPlayer.isTurnEnded())
                view.turnEndable(); // la view potra anche terminare il turno.

            return true;
        }
        return false;
    }


    /**
     * Callback that handles and processes "end turn" message type.
     * End the turn of the current player and start the turn for the next player.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information for ending the turn
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    private Boolean endTurnMessageHandler(VirtualView view, Message message) {
        if (message.getHeader() == "end turn") {
            view.turnEnded();

            this.model.nextTurn();
            String currentPlayerNickname = this.model.getCurrentPlayerNickname();
            this.currentPlayer = this.players.get(currentPlayerNickname);
            this.currentView = this.connectedViews.get(currentPlayerNickname);

            this.currentView.selectWorkerFrom(this.model.getBoard().getPlayerWorkersPositions(currentPlayerNickname));

            return true;
        }
        return false;
    }
}
