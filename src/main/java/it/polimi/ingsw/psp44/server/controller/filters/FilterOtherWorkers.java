package it.polimi.ingsw.psp44.server.controller.filters;

import java.util.List;

import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.util.Position;

public class FilterOtherWorkers extends Filter {

    @Override
    public void filter(Position startingPosition, List<Position> positionsToFilter, Board gameBoard, boolean external) {
        Worker myWorker = gameBoard.getWorker(startingPosition);
        String myNickname = myWorker.getPlayerNickname();
        
        positionsToFilter.removeIf(position -> {
            Worker otherWorker = gameBoard.getWorker(position);
            return otherWorker != null && !myNickname.equals(otherWorker.getPlayerNickname());
        });
    }

}