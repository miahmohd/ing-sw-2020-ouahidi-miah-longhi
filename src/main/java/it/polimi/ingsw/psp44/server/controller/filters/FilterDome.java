package it.polimi.ingsw.psp44.server.controller.filters;

import java.util.List;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

public class FilterDome extends Filter {

    @Override
    public void filter(Position startingPosition, List<Position> positionsToFilter, Board gameBoard) {
        positionsToFilter.removeIf((position) -> gameBoard.isDome(position));
    }


}