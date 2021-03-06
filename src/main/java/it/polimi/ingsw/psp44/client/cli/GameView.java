package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.AbstractGameView;
import it.polimi.ingsw.psp44.network.communication.Action;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.Position;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class GameView extends AbstractGameView {
    private final Console console;
    private final Board board;

    public GameView(String playerNickname, Console console, Board board) {
        this.playerNickname = playerNickname;
        this.console = console;
        this.board = board;
    }

    @Override
    public void chooseWorkersInitialPositionFrom(Message workers) {
        Position femalePosition, malePosition;
        Position[] positionsToSend;

        String body;
        Message message;

        List<Position> positionsToChooseFrom = new ArrayList<>(
                Arrays.asList(BodyFactory.fromPositions(workers.getBody()))
        );

        String board = this.board.highlightPositions(positionsToChooseFrom);
        console.printOnBoardSection(board);

        console.writeLine("choose positions ");
        console.writeLine("gimme the female ");
        femalePosition = getCorrectPosition(positionsToChooseFrom);
        positionsToChooseFrom.remove(femalePosition);
        console.writeLine("gimme the male ");
        malePosition = getCorrectPosition(positionsToChooseFrom);

        positionsToSend = new Position[]{femalePosition, malePosition};

        body = BodyFactory.toPositions(positionsToSend);

        message = new Message(Message.Code.CHOSEN_WORKERS_INITIAL_POSITION, body);
        virtualServer.sendMessage(message);
    }

    @Override
    public void chooseWorkerFrom(Message workers) {
        Position positionToSend;

        String body;
        Message message;

        List<Position> positionsToChooseFrom = new ArrayList<>(
                Arrays.asList(BodyFactory.fromPositions(workers.getBody()))
        );

        console.printOnBoardSection(
                this.board.highlightPositions(positionsToChooseFrom)
        );

        console.writeLine("choose position ");

        positionToSend = getCorrectPosition(positionsToChooseFrom);
        body = BodyFactory.toPosition(positionToSend);
        message = new Message(Message.Code.CHOSEN_WORKER, body);

        virtualServer.sendMessage(message);
    }

    @Override
    public void chooseActionFrom(Message actions) {
        Action chosenAction;
        Map<Position, List<Action>> actionsPerPosition;
        Map<MessageHeader, String> headers;
        boolean isTurnEndable;

        String body;
        Map<MessageHeader, String> headersToSend;
        Message message;

        List<Action> actionList = new ArrayList<>(Arrays.asList(
                BodyFactory.fromActions(actions.getBody())
        ));
        actionsPerPosition = actionList.stream().collect(groupingBy(Action::getTarget));

        console.printOnBoardSection(
                this.board.highlightPositions(actionsPerPosition.keySet())
        );

        headers = actions.getHeader();
        if (headers != null)
            isTurnEndable = Boolean.parseBoolean(headers.get(MessageHeader.IS_TURN_ENDABLE));
        else
            isTurnEndable = false;

        chosenAction = getAction(actionsPerPosition, isTurnEndable);
        headersToSend = new EnumMap<>(MessageHeader.class);
        if (chosenAction == null)
            headersToSend.put(MessageHeader.IS_TURN_END, String.valueOf(true));

        body = BodyFactory.toAction(chosenAction);
        message = new Message(Message.Code.CHOSEN_ACTION, headersToSend, body);

        virtualServer.sendMessage(message);
    }

    @Override
    public void start(Message start) {
        console.writeLine("it's you turn boyyy");
    }

    @Override
    public void end(Message end) {
        console.writeLine("ora stai fermo");
    }

    @Override
    public void lost(Message lost) {
        console.clear();
        console.writeLine("You lost, looser");
        this.virtualServer.close();
    }

    @Override
    public void won(Message won) {
        console.clear();
        console.writeLine("You won, good job!");
        this.virtualServer.close();
    }

    @Override
    public void update(Message update) {
        Cell[] cellsToUpdate = BodyFactory.fromCells(update.getBody());
        this.board.update(cellsToUpdate);
        console.printOnBoardSection(this.board.getBoard());
    }

    @Override
    public void activeTurn(Message activePlayer) {
        String currentPlayer = activePlayer.getBody();
        String print;
        if (currentPlayer.equals(this.playerNickname))
            print = "It's your turn";
        else
            print = String.format("%s's turn", currentPlayer);
        console.clear();
        console.printOnTurnSection(print);
        console.printOnBoardSection(this.board.getBoard());
        console.printOnPlayersSection(this.board.getPlayers());
        console.printOnLegendSection(this.board.getLegend());
    }

    private Action getAction(Map<Position, List<Action>> actionsPerPosition, boolean isTurnEndable) {
        if (isTurnEndable) {
            console.writeLine("do you want to end your turn? (Y/other key) ");
            String choice = console.readLine();

            if (isAffirmative(choice)) {
                return null;
            }
        }

        console.writeLine("gimme the position boy ");
        Position chosenPosition = getCorrectPosition(
                new ArrayList<>(actionsPerPosition.keySet()));
        List<Action> actionsToChooseFrom = actionsPerPosition.get(chosenPosition);
        return getCorrectAction(actionsToChooseFrom);
    }

    private boolean isAffirmative(String choice) {
        return choice.toLowerCase().contains("y");
    }

    private Action getCorrectAction(List<Action> actionsToChooseFrom) {
        Action chosenAction;

        if (actionsToChooseFrom.size() == 1)
            chosenAction = actionsToChooseFrom.stream().findFirst().get();

        else {
            for (Action action : actionsToChooseFrom) {
                console.writeLine(action.getId());
                console.writeLine(action.getDescription());
            }

            do {
                console.writeLine("Choose brother ");
                int chosenActionId = console.readNumber();
                chosenAction = actionsToChooseFrom.stream().filter(action -> action.getId() == chosenActionId)
                        .findFirst()
                        .orElse(null);

                if (chosenAction == null)
                    console.writeLine("incorrect id");

            } while (chosenAction == null);

        }
        return chosenAction;
    }

    private Position getCorrectPosition(List<Position> correctPositions) {
        Position position;
        boolean isCorrect;

        isCorrect = false;
        do {
            position = console.readPosition();
            if (!correctPositions.contains(position))
                console.writeLine("Not a correct Position ");
            else
                isCorrect = true;
        } while (!isCorrect);

        return position;
    }
}
