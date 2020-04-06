package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.SimpleBuild;
import it.polimi.ingsw.psp44.server.model.actions.SimpleMovement;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * this state allows to compute all the the build actions that the worker can do according
 * the active filters
 */
public class SimpleBuildState extends State {
    @Override
    public List<Action> getAvailableActions(Board board, Position selectedWorker) {
        List<Action> availableActions=new ArrayList<>();
        List<Position> builds= board.getNeighbouringPositions(selectedWorker);
        activeBuildFilters.filter(selectedWorker, builds,board);
        for(Position p: builds){
            availableActions.add(new SimpleBuild(p));
        }
        return availableActions;
    }
}

