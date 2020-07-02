package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.DomeBuild;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * this state allows to compute all the the build actions that the worker can do adding build dome action at every level
 */
public class FemaleDomeBuildState extends SimpleBuildState {
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
        List<Action> availableAction;
        List<Action> availableDomeAction;
        List<Position> availablePosition;
        availableDomeAction = new ArrayList<>();
        availableAction = super.getAvailableActions(board, selectedWorker, moveFilter, buildFilter);
        if (board.getWorker(selectedWorker).getSex() == Worker.Sex.FEMALE) {
            for (Action action : availableAction) {
                if (!board.isFinalLevel(action.getTargetPosition()))
                    availableDomeAction.add(new DomeBuild(action.getTargetPosition()));
            }
            availableAction.addAll(availableDomeAction);
        } else {
            selectedWorker = board.getPlayerWorkersPositions(board.getWorker(selectedWorker).getPlayerNickname())
                    .stream().filter(worker -> board.getWorker(worker).getSex() == Worker.Sex.FEMALE).findFirst().get();
            availablePosition = board.getNeighbouringPositions(selectedWorker);
            buildFilter.filter(selectedWorker, availablePosition, board);
            for (Position p : availablePosition) {
                if (!availableAction.stream().anyMatch(action -> action.getTargetPosition().equals(p)) || !board.isFinalLevel(p))
                    availableAction.add(new DomeBuild(p));
            }
        }
        return availableAction;
    }
}

