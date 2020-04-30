package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.controller.states.SimpleMoveState;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FilterOpponentMoveUpTest {
    List<Action> actions;
    FilterCollection activeMoveFilter;
    Board gameBoard;
    GameModel gm;
    private Worker testWorker;
    private SimpleMoveState state;

    @Before
    public void setup() {
        state = new SimpleMoveState();
        testWorker = new Worker("p1", Worker.Sex.MALE);
        activeMoveFilter = new FilterCollection();
        activeMoveFilter.add(new FilterOpponentMoveUp());
        gm = new GameModel();
        gameBoard = gm.getBoard();
        Position p;
        for (int row = 0; row < 5; ++row) {
            for (int column = 0; column < 5; ++column) {
                p = new Position(row, column);
                switch (row + column) {
                    case 8:
                    case 7:
                    case 6:
                        gameBoard.buildUp(p);
                    case 5:
                    case 4:
                        gameBoard.buildUp(p);
                    case 3:
                    case 2:
                        gameBoard.buildUp(p);
                        break;
                }
            }
        }
        gameBoard.setWorker(new Position(0, 0), testWorker);

    }

    @Test
    public void filter() {
        Action actionToPerform;
        assertFalse(activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().isActive());
        //0,0 -> 1,1
        actions = state.getAvailableActions(gameBoard, new Position(0, 0), activeMoveFilter, null);
        actionToPerform = actions.stream().filter(a -> a.getTargetPosition().equals(new Position(1, 1))).findFirst().get();
        gm.doAction(actionToPerform);
        activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().update(actionToPerform, gameBoard);
        assertTrue(activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().isActive());

        //1,1 -> 2,1
        actions = state.getAvailableActions(gameBoard, new Position(1, 1), activeMoveFilter, null);
        actionToPerform = actions.stream().filter(a -> a.getTargetPosition().equals(new Position(2, 1))).findFirst().get();
        gm.doAction(actionToPerform);
        activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().update(actionToPerform, gameBoard);
        assertFalse(activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().isActive());

        //2,1 -> 3,2
        actions = state.getAvailableActions(gameBoard, new Position(2, 1), activeMoveFilter, null);
        actionToPerform = actions.stream().filter(a -> a.getTargetPosition().equals(new Position(3, 2))).findFirst().get();
        gm.doAction(actionToPerform);
        activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().update(actionToPerform, gameBoard);
        assertTrue(activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().isActive());

        //3,2 -> 2,3
        actions = state.getAvailableActions(gameBoard, new Position(3, 2), activeMoveFilter, null);
        actionToPerform = actions.stream().filter(a -> a.getTargetPosition().equals(new Position(2, 3))).findFirst().get();
        gm.doAction(actionToPerform);
        activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().update(actionToPerform, gameBoard);
        assertFalse(activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().isActive());

        //2,3 -> 3,3
        actions = state.getAvailableActions(gameBoard, new Position(2, 3), activeMoveFilter, null);
        actionToPerform = actions.stream().filter(a -> a.getTargetPosition().equals(new Position(3, 3))).findFirst().get();
        gm.doAction(actionToPerform);
        activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().update(actionToPerform, gameBoard);
        assertTrue(activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().isActive());

        //3,3 -> 4,4
        actions = state.getAvailableActions(gameBoard, new Position(3, 3), activeMoveFilter, null);
        actionToPerform = actions.stream().filter(a -> a.getTargetPosition().equals(new Position(4, 4))).findFirst().get();
        gm.doAction(actionToPerform);
        activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().update(actionToPerform, gameBoard);
        assertFalse(activeMoveFilter.getFilters().stream().filter(f -> f.isExternal()).findFirst().get().isActive());

        assertTrue(gameBoard.isWorker(new Position(4, 4)));


    }
}