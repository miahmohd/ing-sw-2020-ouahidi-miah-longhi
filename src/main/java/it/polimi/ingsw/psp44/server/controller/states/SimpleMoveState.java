package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.SimpleMovement;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * this state allows to compute all the the movements that the worker can do according
 * the active filters
 */
public class SimpleMoveState extends State {
    @Override
    public List<Action> getAvailableActions(Board board, Position selectedWorker) {
        List<Action> availableActions=new ArrayList<>();
        List<Position> moves= board.getNeighbouringPositions(selectedWorker);
        activeMoveFilters.filter(selectedWorker,moves,board);
        for(Position p:moves){
            availableActions.add(new SimpleMovement(selectedWorker, p));
        }
        return availableActions;
    }
}
