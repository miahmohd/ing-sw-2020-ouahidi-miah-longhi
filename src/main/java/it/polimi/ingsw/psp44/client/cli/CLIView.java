package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.IView;
import it.polimi.ingsw.psp44.network.IVirtual;
import it.polimi.ingsw.psp44.network.communication.BodyTemplates;
import it.polimi.ingsw.psp44.network.communication.Cell;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.Card;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.Position;

import java.lang.reflect.Array;
import java.util.*;


public class CLIView implements IView<Message>, Runnable {

    private StringBuffer display;
    private String playerNickname;
    private Scanner input;
    private Board board;
    private IVirtual<Message> virtualServer;
    private Map<String, Message.Code> gameOptions;
    private Console console;


    public CLIView(Scanner input, StringBuffer display, Board board, Map<String, Message.Code> gameOptions, Console console) {
        this.input = input;
        this.display = display;
        this.board = board;
        this.gameOptions = gameOptions;
        this.console = console;
    }

    public CLIView() {
        this(new Scanner(System.in), new StringBuffer(), new Board(), new HashMap<>(), new Console());
        initGameOptions();
    }


    @Override
    public void run() {
        String chosenOption, responseBody;
        Message response;
        Message.Code messageCode;
        BodyTemplates.FirstMessage messageBody;

        int numberOfPlayers;

        //TODO: this prompt needs to be in another location
        console.writeLine("Gimme your nickname");

        this.playerNickname = getInput();

        console.writeLine("What you want to do? New Game or Join Game? N/J");
        chosenOption = getInput();
        chosenOption = chosenOption.replace(" ", "").toLowerCase();

        messageCode = this.gameOptions.get(chosenOption);

        if (messageCode == Message.Code.NEW_GAME) {
            console.writeLine("How many Players");
            numberOfPlayers = Integer.parseInt(getInput());
            messageBody = new BodyTemplates.FirstMessage(this.playerNickname, numberOfPlayers);

        } else {
            messageBody = new BodyTemplates.FirstMessage(this.playerNickname);
        }

        responseBody = JsonConvert.getInstance().toJson(messageBody, BodyTemplates.FirstMessage.class);
        response = new Message(messageCode, responseBody);
        virtualServer.sendMessage(response);
    }


    private String getInput() {
        //System.out.print(Graphics.Behaviour.MOVE_RIGHT);
        return input.nextLine();
    }


    @Override
    public void chooseCardsFrom(Message cards) {
        int cardinality;
        Card[] cardList, chosenCards;
        Card chosenCard;
        String responseBody;
        Message response;

        cardinality = Integer.parseInt(cards.getHeader().get(MessageHeader.CARDINALITY));

        //TODO: Change this using body factory
        cardList = JsonConvert.getInstance().fromJson(cards.getBody(), Card[].class);
        chosenCards = new Card[cardinality];

        console.writeLine(String.format("You need to choose %d cards from this pile (just id)", cardinality));


        for (Card card : cardList) {
            console.writeLine(card.toString());
            console.writeLine(Graphics.Behaviour.NEW_LINE.toString());
        }

        for(int numberOfCardCounter = 1; numberOfCardCounter <= cardinality; numberOfCardCounter++) {
            console.writeLine(String.format("chose id number %d", numberOfCardCounter));
            int chosenCardId = Integer.parseInt(getInput());

            chosenCard = Arrays.stream(cardList).filter(card -> card.getId()==chosenCardId).findAny().get();

            chosenCards[numberOfCardCounter-1] = chosenCard;
        }

        //TODO: Change this using body factory
        responseBody = JsonConvert.getInstance().toJson(chosenCards, Card[].class);
        response = new Message(Message.Code.CHOSEN_CARDS, responseBody);
        virtualServer.sendMessage(response);
    }

    @Override
    public void chooseCardFrom(Message cards) {
        int chosenCardId;
        Card[] cardList, restOfCards;
        Card chosenCard;
        BodyTemplates.CardMessage bodyTemplate;
        String responseBody;
        Message response;

        //TODO: Change this using body factory
        cardList = JsonConvert.getInstance().fromJson(cards.getBody(), Card[].class);
        console.writeLine(String.format("choose your card"));

        for (Card card : cardList) {
            console.writeLine(card.toString());
            console.writeLine(Graphics.Element.EMPTY.toString());
        }

        chosenCardId = Integer.parseInt(getInput());

        chosenCard = Arrays.stream(cardList).filter(card -> card.getId()==chosenCardId).findAny().get();
        restOfCards = Arrays.stream(cardList).filter(card -> !card.equals(chosenCard)).toArray(Card[]::new);

        //TODO: Change this using body factory
        bodyTemplate = new BodyTemplates.CardMessage(chosenCard, restOfCards);

        responseBody = JsonConvert.getInstance().toJson(bodyTemplate, BodyTemplates.CardMessage.class);
        response = new Message(Message.Code.CHOSEN_CARD, responseBody);
        virtualServer.sendMessage(response);
    }

    @Override
    public void chooseWorkersInitialPositionFrom(Message workers) {
        List<Position> positionsToHighlight = new ArrayList<>(
                Arrays.asList(JsonConvert.getInstance().fromJson(workers.getBody(), Position[].class))
        );

        String board = this.board.highlightPositions(positionsToHighlight);
        console.printOnBoardSection(board);

        console.writeLine("choose positions ({row},{column})");
        console.writeLine("gimme the female");
        Position femalePosition = getPosition();
        console.writeLine("gimme the male");
        Position malePosition = getPosition();
        Position[] positionsToSend = {femalePosition, malePosition};
        String responseBody = JsonConvert.getInstance().toJson(positionsToSend, Position[].class);
        Message response = new Message(Message.Code.CHOSEN_WORKERS_INITIAL_POSITION, responseBody);
        virtualServer.sendMessage(response);
    }

    @Override
    public void chooseWorkerFrom(Message workers) {
        List<Position> positionsToHighlight = new ArrayList<>(
                Arrays.asList(JsonConvert.getInstance().fromJson(workers.getBody(), Position[].class))
        );

        String board = this.board.highlightPositions(positionsToHighlight);
        console.printOnBoardSection(board);


        console.writeLine("choose position");

        Position positionToSend = getPosition();
        String responseBody = JsonConvert.getInstance().toJson(positionToSend, Position.class);
        Message response = new Message(Message.Code.CHOSEN_WORKERS_INITIAL_POSITION, responseBody);
        virtualServer.sendMessage(response);

    }

    @Override
    public void chooseActionFrom(Message actions) {
    }



    @Override
    public void chooseNickname(Message chooseNickname) {
        String nickname, responseBody;
        Message response;

        //TODO: move message into an appropriate location
        System.out.println("Give me your nickname");

        nickname = input.nextLine();
        responseBody = JsonConvert.getInstance().toJson(nickname, String.class);
        response = new Message(Message.Code.PLAYER_NICKNAME, responseBody);

        this.playerNickname = nickname;
        virtualServer.sendMessage(response);
    }


    @Override
    public void lost(Message lost) {
        // TODO: What to do when you lose
        // The message body may contain some info

    }

    @Override
    public void won(Message won) {
        // TODO: what to do when you win
        // The message body may contain some info

    }

    @Override
    public void startTurn(Message startTurn) {
        console.clear();
        console.printOnBoardSection(this.board.getBoard());
        console.printOnPlayersSection(this.board.getPlayers());
        console.writeLine("It's your turn boy");
    }

    @Override
    public void endTurn(Message endTurn) {
        // TODO: What to do when your turn is over
    }


    @Override
    public void update(Message update) {
        Cell[] cellsToUpdate = JsonConvert.getInstance().fromJson(update.getBody(), Cell[].class);
        this.board.update(cellsToUpdate);

        console.printOnBoardSection(this.board.getBoard());
        //console.printOnPlayersSection(this.board.getPlayers());
    }

    @Override
    public void allPlayerNicknames(Message players) {
        String[] allPlayers;
        List<String> opponents;

        allPlayers = JsonConvert.getInstance().fromJson(players.getBody(), String[].class);

        opponents = new ArrayList<>(Arrays.asList(allPlayers));
        opponents.remove(this.playerNickname);

        board.setPlayers(this.playerNickname, opponents);
    }


    @Override
    public void setServer(IVirtual<Message> virtualServer) {
        this.virtualServer = virtualServer;
    }


    private void initGameOptions() {
        this.gameOptions.put("newgame", Message.Code.NEW_GAME);
        this.gameOptions.put("n", Message.Code.NEW_GAME);

        this.gameOptions.put("joingame", Message.Code.JOIN_GAME);
        this.gameOptions.put("j", Message.Code.JOIN_GAME);
    }

    private Position getPosition() {
        int row, column;
        String position;
        String[] rowAndColumn;

        position = getInput();

        rowAndColumn = position.split(",");
        row = Integer.parseInt(rowAndColumn[0]);
        column = Integer.parseInt(rowAndColumn[1]);

        return new Position(row, column);
    }
}