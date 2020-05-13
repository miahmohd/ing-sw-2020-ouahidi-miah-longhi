package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.communication.BodyTemplates;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Player;
import it.polimi.ingsw.psp44.server.view.VirtualView;
import it.polimi.ingsw.psp44.util.Card;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.R;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        this.model.addObserver(view);
        this.setHandlers(view);
    }


    /**
     * @return the number of players currently registered.
     */
    public Set<String> getRegisteredPlayers() {
        return this.playerViews.keySet();
    }


    /**
     * Starts the setup phase of the game.
     * Sends a message containing the list of cards and information on how many cards the player needs to choose.
     */
    public void start() {
        sendAllPlayerNicknames();

        VirtualView currentPlayer;
        Map<MessageHeader, String> headers;
        String body;
        Message toSend;

        currentPlayer = this.playerViews.get(this.model.getCurrentPlayerNickname());

        Card[] allCards = R.getCards();
        body = BodyFactory.toCards(allCards);
        headers = new EnumMap<>(MessageHeader.class);

        headers.put(MessageHeader.CARDINALITY, String.valueOf(this.getRegisteredPlayers().size()));

        toSend = new Message(Message.Code.CHOOSE_CARDS, headers, body);

        startTurn();
        currentPlayer.sendMessage(toSend);
    }


    /**
     * Callback that handles the cards chosen by the first player.
     * It redirects the chosen cards to the second player.
     *
     * @param view    the player that chose the cards, the first one.
     * @param message message with code CHOSEN_CARDS containing information about the chosen cards.
     */
    public synchronized void chosenCardsMessageHandler(VirtualView view, Message message) {
        if (playerViews.get(this.model.getCurrentPlayerNickname()) != view)
            return;

        nextTurn();

        VirtualView currentPlayer = this.playerViews.get(this.model.getCurrentPlayerNickname());
        Message toSend = new Message(Message.Code.CHOOSE_CARD, message.getBody());

        currentPlayer.sendMessage(toSend);
    }



    /**
     * Callback that handles the card chosen by the player.
     * Creates the CardController associated with the chosen card and redirects the rest of the cards to the next player.
     * If the all players have chosen the card, the first player start choosing his workers initial positions.
     *
     * @param view    the player that chose the card.
     * @param message message with code CHOSEN_CARD containing information about the chosen card.
     */
    public synchronized void chosenCardMessageHandler(VirtualView view, Message message) {
        Message toSend;
        CardController cardController;
        VirtualView currentView = playerViews.get(this.model.getCurrentPlayerNickname());

        if (currentView != view)
            return;

        BodyTemplates.ChosenCard body = BodyFactory.fromChosenCard(message.getBody());
        Card chosen = body.getChosen();
        Card[] rest = body.getRest();
        cardController = CardFactory.getController(chosen);
        cardController.setContext(controller);
        this.playerCardController.put(this.model.getCurrentPlayerNickname(), cardController);
        endTurn();

        currentView = playerViews.get(this.model.getCurrentPlayerNickname());

        if (this.model.isFullRound()) {
            cardController = CardFactory.getController(rest[0]);
            cardController.setContext(controller);
            this.playerCardController.put(this.model.getCurrentPlayerNickname(),cardController);
            this.controller.setVirtualViews(this.playerViews);
            this.controller.setCardControllers(this.playerCardController);
            this.controller.setModel(this.model);
            this.controller.init(false);
        } else {
            startTurn();
            toSend = new Message(
                    Message.Code.CHOOSE_CARD,
                    BodyFactory.toCards(rest));
            currentView.sendMessage(toSend);
        }


    }


    private void setHandlers(VirtualView view) {
        view.addMessageHandler(Message.Code.CHOSEN_CARDS, this::chosenCardsMessageHandler);
        view.addMessageHandler(Message.Code.CHOSEN_CARD, this::chosenCardMessageHandler);
        view.addMessageHandler(Message.Code.CHOSEN_WORKERS_INITIAL_POSITION, this.controller::chosenWorkersInitialPositionsMessageHandler);
        view.addMessageHandler(Message.Code.CHOSEN_WORKER, this.controller::chosenWorkerMessageHandler);
        view.addMessageHandler(Message.Code.CHOSEN_ACTION, this.controller::chosenActionMessageHandler);
    }

    private void sendAllPlayerNicknames() {
        Message toSend;
        String[] allPlayerNicknames;
        String messageBody;

        allPlayerNicknames = new String[playerViews.keySet().size()];

        playerViews.keySet().toArray(allPlayerNicknames);
        messageBody = BodyFactory.toPlayerNicknames(allPlayerNicknames);
        toSend = new Message(Message.Code.ALL_PLAYER_NICKNAMES, messageBody);

        for (VirtualView view : playerViews.values()) {
            view.sendMessage(toSend);
        }
    }

    private void nextTurn() {
        endTurn();
        startTurn();
    }

    private void endTurn() {
        VirtualView currentView;
        currentView = this.playerViews.get(this.model.getCurrentPlayerNickname());
        currentView.sendMessage(new Message(Message.Code.END_TURN));

        this.model.nextTurn();
    }

    private void startTurn() {
        VirtualView currentView;
        currentView = this.playerViews.get(this.model.getCurrentPlayerNickname());
        currentView.sendMessage(new Message(Message.Code.START_TURN));
    }

}
