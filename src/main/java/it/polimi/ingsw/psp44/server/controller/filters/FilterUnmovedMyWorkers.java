package it.polimi.ingsw.psp44.server.controller.filters;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;

public class FilterUnmovedMyWorkers extends Filter {

    @Override
    public void filter(Position startingPosition, List<Position> positionsToFilter, Board gameBoard) {
        Worker myWorker = gameBoard.getWorker(startingPosition);
        String myNickname = myWorker.getPlayerNickname();
        List<Position> myWorkerPositions = gameBoard.getPlayerWorkersPositions(myNickname);
        positionsToFilter.removeAll(myWorkerPositions);
        positionsToFilter.add(startingPosition);
    }

}