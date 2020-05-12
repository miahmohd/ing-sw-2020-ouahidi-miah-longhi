package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.communication.Action;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class GameView {
    private VirtualServer virtualServer;
    private Console console;


    private String playerNickname;
    private Board board;


    public GameView(String playerNickname, Console console, Board board) {
        this.playerNickname = playerNickname;
        this.console = console;
        this.board = board;

    }

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

        positionToSend = console.readPosition();
        body = BodyFactory.toPosition(positionToSend);
        message = new Message(Message.Code.CHOSEN_WORKER, body);

        virtualServer.sendMessage(message);

    }

    public void chooseActionFrom(Message actions) {
        Action chosenAction;
        Map<Position, List<Action>> actionsPerPosition;

        String body;
        Message message;

        List<Action> actionList = new ArrayList<>(Arrays.asList(
                BodyFactory.fromActions(actions.getBody())
        ));
        actionsPerPosition = actionList.stream().collect(groupingBy(Action::getTarget));

        console.printOnBoardSection(
                this.board.highlightActions(actionsPerPosition)
        );

        console.writeLine("gimme the position boyyy ");
        Position chosenPosition = console.readPosition();

        List<Action> chosenActions = actionsPerPosition.get(chosenPosition);

        if (chosenActions.size() > 1) {
            //TODO: MAKE DECIDE
            chosenAction = chosenActions.stream().findFirst().get();
        } else
            chosenAction = chosenActions.stream().findFirst().get();


        body = BodyFactory.toAction(chosenAction);
        message = new Message(Message.Code.CHOSEN_ACTION, body);

        virtualServer.sendMessage(message);
    }

    public void start(Message start) {
        console.clear();
        console.writeLine("it's your turn boy");
    }

    public void end(Message end) {
        console.writeLine("ora stai fermo");
    }

    public void lost(Message lost) {
        // TODO: What to do when you lose
        // The message body may contain some info

    }

    public void won(Message won) {
        // TODO: what to do when you win
        // The message body may contain some info

    }


    public void update(Message update) {
        Cell[] cellsToUpdate = JsonConvert.getInstance().fromJson(update.getBody(), Cell[].class);
        this.board.update(cellsToUpdate);

        console.printOnBoardSection(this.board.getBoard());
    }

    public void setServer(VirtualServer virtual) {
        this.virtualServer = virtual;

        virtualServer.cleanRoutes();

        virtualServer.addRoute(Message.Code.START_TURN, this::start);
        virtualServer.addRoute(Message.Code.END_TURN, this::end);
        virtualServer.addRoute(Message.Code.CHOOSE_WORKER, this::chooseWorkerFrom);
        virtualServer.addRoute(Message.Code.UPDATE, this::update);
        virtualServer.addRoute(Message.Code.CHOOSE_ACTION, this::chooseActionFrom);
        virtualServer.addRoute(Message.Code.WON, this::won);
        virtualServer.addRoute(Message.Code.LOST, this::lost);

    }
}
