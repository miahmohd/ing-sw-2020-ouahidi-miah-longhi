package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHandlerFunction;
import it.polimi.ingsw.psp44.network.message.MessageRouter;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

/**
 * Class representing the view of a single player.
 */
public class VirtualView implements Runnable {

    private final SocketConnection connection;
    private final MessageRouter router;

    public VirtualView(SocketConnection connection) {
        this.connection = connection;
        this.router = new MessageRouter();
    }

    @Override
    public void run() {
        //TODO da aggiungere:
        // - definire Message
        // - sistemare in loop

        while (true) {
            String rawJson = this.connection.readLine();
            Message message = JsonConvert.getInstance().fromJson(rawJson, Message.class);
            this.router.route(this, message);
        }
    }

    public void addMessageHandler(Message.Code code, MessageHandlerFunction handler) {
        this.router.addRoute(code, handler);
    }

    //todo utilizzare oggetti specifici per la trasmissione TBD
    //  conversione avviene qui o da qualche altra parte?

    /**
     * Resets the interface and prepare for the start of the game/turn.
     */
    public void startTurn() {
        // superfluo potrebbe bastare selectWorkerFrom
    }

    /**
     * Allow the player to choose a worker.
     *
     * @param playerWorkersPositions the list of workers position to choose from.
     */
    public void selectWorkerFrom(List<Position> playerWorkersPositions) {
    }

    /**
     * Allow the player to choose an action.
     *
     * @param actions the list of actions to choose from.
     */
    public void selectActionFrom(List<Action> actions) {

    }

    /**
     * Allow the player to end his turn.
     */
    public void turnEndable() {
    }

    /**
     * Ends the turn.
     */
    public void turnEnded() {
        // è superfluo come messaggio.
    }


    /**
     * Send a list containing all the cards, and a number indicating how many to chose
     * @param message
     */
    public void chooseCardsFrom(Message message) {

    }

    /**
     * Allow the player to choose a card .
     */
    public void chooseCardFrom() {
    }

    /**
     * Permette al giocatore di scegliere le posizioni iniziali del player
     */
    public void chooseWorkersInitialPosition() {

    }
}






























