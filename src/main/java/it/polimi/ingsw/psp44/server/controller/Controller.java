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

    /**
     * Callback that handles and processes "get worker" message type.
     * Next the view must choose the worker.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information about the selected worker
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    public Boolean getWorkerMessageHandler(VirtualView view, Message message) {
        if (message.getHeader() == "get worker") {
            //menage request
            return true;
        }
        return false;
    }

    /**
     * Callback that handles and processes "worker selected" message type.
     * Next the view must choose an action.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information about the selected worker
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    public Boolean workerSelectedMessageHandler(VirtualView view, Message message) {
        if (message.getHeader() == "worker selected") {
            //menage request
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
            //menage request
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
    public Boolean endTurnMessageHandler(VirtualView view, Message message) {
        if (message.getHeader() == "end turn") {
           //menage request
            return true;
        }
        return false;
    }

    public void dispatchBuildFilter(Filter filter) {
    }

    public void dispatchMoveFilter(Filter filter) {
    }

    public void addPlayer(String nickname, VirtualView view) {
    }

    public int getRegisteredPlayer() {
        return 5;
    }
}
