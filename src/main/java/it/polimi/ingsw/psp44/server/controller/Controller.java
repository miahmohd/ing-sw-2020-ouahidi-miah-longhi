package it.polimi.ingsw.psp44.server.controller;
//TODO

import it.polimi.ingsw.psp44.network.VirtualView;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.model.GameModel;

import java.util.HashMap;

/**
 * This class implements the logic of the match.
 */

public class Controller {
    private HashMap<String, CardController> players;
    private HashMap<String, VirtualView> playerViews;
    private CardController currentPlayer;
    private VirtualView currentPlayerView;
    private GameModel model;


    public void start() {
        /*
        setta il current player, la currentplayerview
        currentview.startTurn()
        current.view.chooseWorkerFrom(lista posizioni)
        */
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
        /*
            Posizione p = message.parse()

            if(lista mosse vuota){
                controllo se sono in 2, se  si l'altro vince. senò continuo il gioco e invio gli worker al prossimo.
                view.lost()
                // rimuovere dal model gli worker, player, ma non l'observer
                this.start() // con il prossimo player
            }else{
            model.setchosenWorker(p)
            Message m = controller.getMosse(p)
            view.choseActionFrom(m)
            }
        * */
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

//        Integer s = JsonConvert.getInstance().fromJson(message.getBody(), Integer.class);
        /*
        ricavo in qualche modo  dal messagio la action selezionata
        model.doaction(action selezionata) // aggiorna automaticamente gli altri

        controllo vittoria con action selezionata.
        cardController.nextStatus(action)

        invio le prossime action al giocatore corrente.

        if(controller.puòTerminare)
            view.turnEndable()

        // se l'unica cosa che può fare è terminare il turno, invia direttamente  currentView.endTurn(), e relativa gestione del prossimo turno.
        */
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
       /*
            currentView.endTurn()
            model.nextplayer()
            this.start().
       *
       * */
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
}
