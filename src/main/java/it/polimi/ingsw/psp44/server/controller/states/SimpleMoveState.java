package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
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

    /**
     * Compute the available actions that the player can perform
     * @param board          representation of the playing field
     * @param selectedWorker worker selected from the player
     * @param moveFilter     filter to apply to move actions
     * @param buildFilter    filter to apply to build actions
     * @return list of available actions
     */
    @Override
    public List<Action> getAvailableActions(Board board, Position selectedWorker, FilterCollection moveFilter, FilterCollection buildFilter) {
        List<Action> availableActions=new ArrayList<>();
        List<Position> moves= board.getNeighbouringPositions(selectedWorker);
        moveFilter.filter(selectedWorker,moves,board);
        for(Position p:moves){
            availableActions.add(new SimpleMovement(selectedWorker, p));
        }
        return availableActions;
    }
}
