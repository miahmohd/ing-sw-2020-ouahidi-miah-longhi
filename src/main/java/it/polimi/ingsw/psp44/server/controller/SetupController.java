package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.network.VirtualView;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.server.model.Card;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Player;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.InitialPlacement;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.R;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages the setup phase of the game.
 */
public class SetupController {
    private final Controller controller;
    private final GameModel model;

    private final Map<String, VirtualView> playerViews;
    private final Map<String, CardController> playerCardController;

    public SetupController() {
        this(new Controller(), new GameModel(), new HashMap<>(), new HashMap<>());
    }

    public SetupController(Controller controller,
                           GameModel model,
                           Map<String, VirtualView> playerViews,
                           Map<String, CardController> playerCardController) {
        this.controller = controller;
        this.model = model;
        this.playerViews = playerViews;
        this.playerCardController = playerCardController;
    }



    public void addPlayer(String nickname, VirtualView view) {
        Player player = new Player(nickname);
        this.playerViews.put(nickname, view);
        this.model.addPlayer(player);
        this.setHandlers(view);
    }



    private void setHandlers(VirtualView view) {
        view.addMessageHandler(Message.Code.CHOSEN_CARDS, this::chosenCardsMessageHandler);
        view.addMessageHandler(Message.Code.CHOSEN_CARD, this::chosenCardMessageHandler);
        view.addMessageHandler(Message.Code.CHOSEN_WORKERS_INITIAL_POSITION, this::chosenWorkersInitialPositionsMessageHandler);
        view.addMessageHandler(Message.Code.CHOSEN_WORKER, this.controller::chosenWorkerMessageHandler);
        view.addMessageHandler(Message.Code.CHOSEN_ACTION, this.controller::chosenActionMessageHandler);
    }

    /**
     * @return the number of players currently registered.
     */
    public int getRegisteredPlayer() {
        return this.model.getNumberOfPlayer();
    }


    /**
     * Starts the setup phase of the game.
     * Sends a message containing the list of cards and information on how many cards the player needs to choose.
     */
    public void start() {
        VirtualView currentPlayer = this.playerViews.get(this.model.getCurrentPlayerNickname());
        Card[] allCards = R.getCards();
        String body = JsonConvert.getInstance().toJson(allCards, Card[].class);
        Map<String, String> headers = new HashMap<>();

        // TODO possiamo fare con la classe locale come in this::chosenCardMessageHandler?
        headers.put("cardinality", String.valueOf(this.getRegisteredPlayer()));
        Message message = new Message(Message.Code.CHOOSE_CARDS, headers, body);

        currentPlayer.startTurn(new Message(Message.Code.START));
        currentPlayer.chooseCardsFrom(message);
    }


    /**
     * Callback that handles the cards chosen by the first player.
     * It redirects the chosen cards to the second player.
     *
     * @param view    the player that chose the cards.
     * @param message message with code CHOSEN_CARDS containing information about the chosen cards.
     */
    public void chosenCardsMessageHandler(VirtualView view, Message message) {
        if (message.getCode() == Message.Code.CHOSEN_CARDS) {
            this.model.nextTurn();
            VirtualView currentPlayer = this.playerViews.get(this.model.getCurrentPlayerNickname());
            Message toSend = new Message(Message.Code.CHOOSE_CARD, message.getBody());

            currentPlayer.chooseCardFrom(toSend);
        }
    }


    /**
     * Callback that handles the card chosen by the player.
     * Creates the CardController associated with the chosen card and redirects the rest of the cards to the next player.
     * If the all players have chosen the card, the first player start choosing his workers initial positions.
     *
     * @param view    the player that chose the card.
     * @param message message with code CHOSEN_CARD containing information about the chosen card.
     */
    public void chosenCardMessageHandler(VirtualView view, Message message) {
        if (message.getCode() == Message.Code.CHOSEN_CARD) {

            class BodyTemplate {
                Card chosen;
                Card[] rest;
            }

            BodyTemplate body = JsonConvert.getInstance().fromJson(message.getBody(), BodyTemplate.class);
            Card chosen = body.chosen;
            Card[] rest = body.rest;

//          TODO this.playerCardController.put(this.model.getCurrentPlayerNickname(), CardFactory.getController(chosen));

            if (!this.model.isFullRound()) {
                this.model.nextTurn();
                VirtualView nextPlayer = this.playerViews.get(this.model.getCurrentPlayerNickname());
                Message toSend = new Message(Message.Code.CHOOSE_CARD, JsonConvert.getInstance().toJson(rest, Card[].class));

                nextPlayer.chooseCardFrom(toSend);
            } else {
//                now view is the first player
                Position[] positions = (Position[]) this.model.getBoard().getUnoccupiedPosition().toArray();
                Message toSend = new Message(Message.Code.CHOOSE_WORKERS_INITIAL_POSITION,
                        JsonConvert.getInstance().toJson(positions, Position[].class));

                view.startTurn(new Message(Message.Code.START));
                view.chooseWorkersInitialPosition(toSend);
            }
        }
    }


    /**
     * Callback that handles the workers initial positions chosen by the player.
     * The first position is for the female worker, the second one is for the male worker.
     * It places the workers on the board.
     * If all all the players have placed the workers, it starts the main phase of the game.
     *
     * @param view    the player that chose the card.
     * @param message message with code CHOSEN_WORKERS_INITIAL_POSITION containing information about the chose positions.
     */
    public void chosenWorkersInitialPositionsMessageHandler(VirtualView view, Message message) {
        if (message.getCode() == Message.Code.CHOSEN_WORKERS_INITIAL_POSITION) {
            Position[] chosenPositions = JsonConvert.getInstance().fromJson(message.getBody(), Position[].class);

            String currentPlayerNickname = this.model.getCurrentPlayerNickname();
            Worker female = new Worker(currentPlayerNickname, Worker.Sex.FEMALE);
            Worker male = new Worker(currentPlayerNickname, Worker.Sex.MALE);

            InitialPlacement femalePlacement = new InitialPlacement(chosenPositions[0], female);
            InitialPlacement malePlacement = new InitialPlacement(chosenPositions[0], male);
            this.model.applyAction(femalePlacement);
            this.model.applyAction(malePlacement);

            this.model.nextTurn();
            VirtualView nextPlayer = this.playerViews.get(this.model.getCurrentPlayerNickname());

            if (!this.model.isFullRound()) {
                Position[] positions = (Position[]) this.model.getBoard().getUnoccupiedPosition().toArray();
                Message toSend = new Message(Message.Code.CHOOSE_WORKERS_INITIAL_POSITION,
                        JsonConvert.getInstance().toJson(positions, Position[].class));

                nextPlayer.chooseWorkersInitialPosition(toSend);
            } else {
                this.controller.setVirtualViews(this.playerViews);
                this.controller.setCardControllers(this.playerCardController);
                this.controller.setModel(this.model);

                this.controller.start();
            }
        }
    }

}
