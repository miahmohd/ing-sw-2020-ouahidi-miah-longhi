package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.ForceBackwardsMovement;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * this state allows to compute all the the movements that the worker adding move action to opponents worker's position
 */
public class ForceBackwardsMoveState extends SimpleMoveState {

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
        List<Position> targetPositions = board.getNeighbouringPositions(selectedWorker);
        for (Position targetPosition : targetPositions) {
            if (board.isWorker(targetPosition) &&
                    (!board.getWorker(targetPosition).getPlayerNickname().equals(board.getWorker(selectedWorker).getPlayerNickname()))) {
                int[] delta = {-targetPosition.getRow() + selectedWorker.getRow(), -targetPosition.getColumn() + selectedWorker.getColumn()};
                Position forcedPosition = new Position(selectedWorker.getRow() + delta[0], selectedWorker.getColumn() + delta[1]);
                if (board.isPositionInBounds(forcedPosition) && (!board.isDome(forcedPosition)) && (!board.isWorker(forcedPosition)))
                    availableActions.add(new ForceBackwardsMovement(selectedWorker, targetPosition, forcedPosition));
            }
        }
        return availableActions;

    }
}
