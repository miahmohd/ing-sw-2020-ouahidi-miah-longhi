package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

/**
 * this state allows to compute all the positions where the worker can build according
 * the active filters
 */
public class SimpleBuildState extends State {
    @Override
    public List<Action> getAvailableActions(Board board, Position selectedWorker) {
        return null;
    }
}
