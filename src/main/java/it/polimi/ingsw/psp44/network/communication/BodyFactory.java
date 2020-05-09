package it.polimi.ingsw.psp44.network.communication;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

public class BodyFactory {


    /**
     * Convert a list of modified positions to Cell, suitable for transmission
     *
     * @param positions the list of Position to convert
     * @param board     the board where the change happened
     * @return a json string representing a list of Cell
     */
    public static String toCells(List<Position> positions, Board board) {
        Cell[] cells = new Cell[positions.size()];
        int i = 0;
        for (Position position : positions) {
            Worker worker = board.isDome(position) ? null : board.getWorker(position);
            Worker.Sex sex = worker != null ? worker.getSex() : null;
            String nickname = worker != null ? worker.getPlayerNickname() : null;

            cells[i] = new Cell(position, board.getLevel(position), board.isDome(position), sex, nickname);
            i++;
        }
        return JsonConvert.getInstance().toJson(cells, Cell[].class);
    }

    public static String toActions(List<Action> availableActions) {
        it.polimi.ingsw.psp44.network.communication.Action[] actionList = new
                it.polimi.ingsw.psp44.network.communication.Action[availableActions.size()];
        int count = 0;
        for (Action action : availableActions){
            actionList[count] = new it.polimi.ingsw.psp44.network.communication.Action(
                    action.getTargetPosition(),
                    action.isBuild(),
                    action.isMovement(),
                    0,
                    false
            );
            count++;
        }

        return JsonConvert.getInstance().toJson(actionList, it.polimi.ingsw.psp44.network.communication.Action[].class);
    }
}
