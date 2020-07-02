package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.PushForwardMovement;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * this state allows to compute all the the movements that the worker adding move action to opponents worker's position
 */
public class PushMoveState extends SimpleMoveState {

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
        Position workerAfterPushPosition;
        List<Action> availableActions = super.getAvailableActions(board, selectedWorker, moveFilter, buildFilter);
        List<Action> availablePushActions = new ArrayList<>();
        List<Action> actionsToRemove = new ArrayList<>();
        for (Action a : availableActions) {
            if (board.isWorker(a.getTargetPosition())) {
                actionsToRemove.add(a);
                workerAfterPushPosition = positionToTest(a.getSourcePosition(), a.getTargetPosition());
                if (board.isPositionInBounds(workerAfterPushPosition) && (!board.isWorker(workerAfterPushPosition)) && (!board.isDome(workerAfterPushPosition)))
                    availablePushActions.add(new PushForwardMovement(selectedWorker, a.getTargetPosition(), workerAfterPushPosition));
            }
        }
        availableActions.removeAll(actionsToRemove);
        availableActions.addAll(availablePushActions);
        return availableActions;
    }

    /**
     * Find the straight backward position on the source-target direction
     *
     * @param source
     * @param target
     * @return
     */
    private Position positionToTest(Position source, Position target) {
        int deltaR;
        int deltaC;
        deltaR = target.getRow() - source.getRow();
        deltaC = target.getColumn() - source.getColumn();
        return new Position(target.getRow() + deltaR, target.getColumn() + deltaC);
    }
}
