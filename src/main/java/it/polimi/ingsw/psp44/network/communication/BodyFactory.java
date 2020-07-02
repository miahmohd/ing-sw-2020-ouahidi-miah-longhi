package it.polimi.ingsw.psp44.network.communication;

import it.polimi.ingsw.psp44.server.controller.CardController;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Card;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.Position;

import java.util.List;
import java.util.Map;

/**
 * A class grouping a set of function for converting Objects to String suitable for transmission and viceversa.
 */
public class BodyFactory {


    private BodyFactory() {
    }


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


    public static Cell[] fromCells(String body) {
        return JsonConvert.getInstance().fromJson(body, Cell[].class);
    }


    public static String toActions(List<Action> availableActions) {
        it.polimi.ingsw.psp44.network.communication.Action[] actionList = new
                it.polimi.ingsw.psp44.network.communication.Action[availableActions.size()];
        int count = 0;
        for (Action action : availableActions) {
            actionList[count] = new it.polimi.ingsw.psp44.network.communication.Action(
                    count,
                    action.getDescription(),
                    action.getTargetPosition(),
                    action.isBuild(),
                    action.isMovement()
            );
            count++;
        }

        return JsonConvert.getInstance().toJson(actionList, it.polimi.ingsw.psp44.network.communication.Action[].class);
    }

    public static String toAction(it.polimi.ingsw.psp44.network.communication.Action action) {
        return JsonConvert.getInstance().toJson(action, it.polimi.ingsw.psp44.network.communication.Action.class);
    }

    public static it.polimi.ingsw.psp44.network.communication.Action[] fromActions(String actions) {
        return JsonConvert.getInstance().fromJson(actions, it.polimi.ingsw.psp44.network.communication.Action[].class);
    }

    public static it.polimi.ingsw.psp44.network.communication.Action fromAction(String chosenAction) {
        return JsonConvert.getInstance().fromJson(chosenAction, it.polimi.ingsw.psp44.network.communication.Action.class);
    }


    public static String toNewGame(String playerNickname, int numberOfPlayers) {
        BodyTemplates.NewGame body = new BodyTemplates.NewGame(playerNickname, numberOfPlayers);
        return JsonConvert.getInstance().toJson(body, BodyTemplates.NewGame.class);
    }

    public static BodyTemplates.NewGame fromNewGame(String body) {
        return JsonConvert.getInstance().fromJson(body, BodyTemplates.NewGame.class);
    }

    public static String toJoinGame(String playerNickname, int gameId) {
        BodyTemplates.JoinGame body = new BodyTemplates.JoinGame(playerNickname, gameId);
        return JsonConvert.getInstance().toJson(body, BodyTemplates.JoinGame.class);
    }

    public static BodyTemplates.JoinGame fromJoinGame(String body) {
        return JsonConvert.getInstance().fromJson(body, BodyTemplates.JoinGame.class);
    }

    public static String toCards(Card[] cards) {
        return JsonConvert.getInstance().toJson(cards, Card[].class);
    }


    public static Card[] fromCards(String cards) {
        return JsonConvert.getInstance().fromJson(cards, Card[].class);
    }


    public static Position[] fromPositions(String positions) {
        return JsonConvert.getInstance().fromJson(positions, Position[].class);
    }

    public static Position fromPosition(String position) {
        return JsonConvert.getInstance().fromJson(position, Position.class);
    }

    public static String toPositions(Position[] positions) {
        return JsonConvert.getInstance().toJson(positions, Position[].class);
    }

    public static String toPosition(Position position) {
        return JsonConvert.getInstance().toJson(position, Position.class);
    }

    public static String[] fromPlayerNicknames(String playerNicknames) {
        return JsonConvert.getInstance().fromJson(playerNicknames, String[].class);
    }

    public static String toPlayerNicknames(String[] playerNicknames) {
        return JsonConvert.getInstance().toJson(playerNicknames, String[].class);
    }

    public static String toCard(Card card) {
        return JsonConvert.getInstance().toJson(card, Card.class);
    }

    public static Card fromCard(String card) {
        return JsonConvert.getInstance().fromJson(card, Card.class);
    }

    public static String toPlayerCards(Map<String, CardController> playerAndCard) {
        BodyTemplates.PlayerCard[] playerCards = new BodyTemplates.PlayerCard[playerAndCard.size()];
        int counter = 0;
        for (String player : playerAndCard.keySet()) {
            playerCards[counter] = new BodyTemplates.PlayerCard(player, playerAndCard.get(player).getCard());
            counter++;
        }

        return JsonConvert.getInstance().toJson(playerCards, BodyTemplates.PlayerCard[].class);
    }

    public static BodyTemplates.PlayerCard[] fromPlayerCards(String playerCards) {
        return JsonConvert.getInstance().fromJson(playerCards, BodyTemplates.PlayerCard[].class);
    }
}
