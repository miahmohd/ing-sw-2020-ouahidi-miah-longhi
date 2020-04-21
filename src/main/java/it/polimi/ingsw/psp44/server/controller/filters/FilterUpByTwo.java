package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

public class FilterUpByTwo extends Filter {


    @Override
    public void filter(Position startingPosition, List<Position> positionsToFilter, Board gameBoard, boolean external) {
        positionsToFilter.removeIf(position -> {
            int startingLevel = gameBoard.getLevel(startingPosition);
            int arrivingLevel = gameBoard.getLevel(position);

            int delta = arrivingLevel - startingLevel;
            return delta > 1;
        });

    } 
}