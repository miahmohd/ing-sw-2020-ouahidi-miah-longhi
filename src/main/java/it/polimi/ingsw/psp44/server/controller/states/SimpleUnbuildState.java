package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.SimpleBuild;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * this state allows to compute all the the build actions that the worker can do according
 * the active filters
 */
public class SimpleUnbuildState extends State {
    /**
     * Compute the available actions that the player can perform
     *
     * @param board          representation of the playing field
     * @param selectedWorker worker selected from the player
     * @param moveFilter     filter to apply to move actions
     * @param buildFilter    filter to apply to build actions
     * @return list of available actions
     */
    @Override
    public List<Action> getAvailableActions(Board board, Position selectedWorker, FilterCollection moveFilter, FilterCollection buildFilter) {
        List<Action> availableActions = new ArrayList<>();
        Position unmovedWorker = board.getPlayerWorkersPositions(board.getWorker(selectedWorker).getPlayerNickname())
                .stream().filter(pos -> !pos.equals(selectedWorker)).findFirst().orElse(null);
        if (unmovedWorker != null) {
            List<Position> builds = board.getNeighbouringPositions(unmovedWorker);
            buildFilter.filter(selectedWorker, builds, board);
            for (Position p : builds) {
                if (!board.isGroundLevel(p))
                    availableActions.add(new SimpleBuild(p, true));
            }
        }
        return availableActions;
    }
}

