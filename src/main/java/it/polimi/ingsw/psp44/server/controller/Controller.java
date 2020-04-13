package it.polimi.ingsw.psp44.server.controller;
//TODO

import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;

import java.util.HashMap;

/**
 * This class implements the logic of the match.
 */

public class Controller {
    private HashMap<String, CardController> players;
    private CardController currentPlayer;
    private GameModel model;
    private Position selectedWorkerPosition;
    private Action selectedAction;

    /**
     * menages the sequence of the player's turns
     */
    public void beginMatch(){
        while(!currentPlayer.hasWon()) {
            if (currentPlayer.isTurnEnded()) {
                model.nextTurn();
                currentPlayer = players.get(model.getCurrentPlayerNickname());
                currentPlayer.startTurn();
                currentPlayer.getWorkers(model.getBoard(), model.getCurrentPlayerNickname());
                selectedWorkerPosition = sendWorker();
            }
            currentPlayer.getAction(model.getBoard(), selectedWorkerPosition);
            selectedAction = sendMosse();
            eseguiMosse(selectedAction);
            currentPlayer.nextState(selectedAction);
        }
    }


    private void eseguiMosse(Action selectedAction) {
    }

    private Action sendMosse() {
        return null;
    }

    private Position sendWorker() {
        return null;
    }


}
