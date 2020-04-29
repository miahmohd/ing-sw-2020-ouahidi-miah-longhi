package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.IView;
import it.polimi.ingsw.psp44.network.IVirtual;
import it.polimi.ingsw.psp44.network.communication.BodyTemplates;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.Position;

import java.util.*;


public class CLIView implements IView<Message>, Runnable {

    private StringBuffer display;
    private String playerNickname;
    private Scanner input;
    private Board board;
    private IVirtual<Message> virtualServer;
    private Map<String, Message.Code> gameOptions;


    public CLIView(Scanner input, StringBuffer display, Board board, Map<String, Message.Code> gameOptions) {
        this.input = input;
        this.display = display;
        this.board = board;
        this.gameOptions = gameOptions;
    }

    public CLIView() {
        this(new Scanner(System.in), new StringBuffer(), new Board(), new HashMap<>());
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
        System.out.println("Gimme your nickname");
        this.playerNickname = input.nextLine();


        chosenOption = input.nextLine();
        chosenOption = chosenOption.replace(" ", "").toLowerCase();

        messageCode = this.gameOptions.get(chosenOption);

        if (messageCode == Message.Code.NEW_GAME) {
            System.out.println("How many Players");
            numberOfPlayers = Integer.parseInt(input.nextLine());
            messageBody = new BodyTemplates.FirstMessage(this.playerNickname, numberOfPlayers);

        } else {
            messageBody = new BodyTemplates.FirstMessage(this.playerNickname);
        }

        responseBody = JsonConvert.getInstance().toJson(messageBody, BodyTemplates.FirstMessage.class);
        response = new Message(messageCode, responseBody);
        virtualServer.sendMessage(response);
    }


    @Override
    public void chooseCardsFrom(Message cards) {
        System.out.println("You need to choose cards from this pile");

        class Card {
            int id;
            String description;
        }

        int cardinality = Integer.parseInt(cards.getHeader().get(MessageHeader.CARDINALITY));

        Card[] cardsList = JsonConvert.getInstance().fromJson(cards.getBody(), Card[].class);

        for (Card card : cardsList) {
            System.out.println(card.id);
            System.out.println(card.description);
            System.out.println();
        }


    }

    @Override
    public void chooseCardFrom(Message cards) {
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
    public void chooseWorkerFrom(Message workers) {
        String body = workers.getBody();
        Position[] workerPositions = JsonConvert.getInstance().fromJson(body, Position[].class);

        //board.highlightPositions(workerPositions);

        for (Position workerPosition : workerPositions) {
            display.append(workerPosition);
        }

        //TODO: add the stuff
    }

    @Override
    public void chooseActionFrom(Message actions) {

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
        System.out.println("It's your turn boy");
    }

    @Override
    public void endTurn(Message endTurn) {
        // TODO: What to do when your turn is over

    }

    @Override
    public void players(Message players) {
        String[] allPlayers;
        List<String> opponents;

        allPlayers = JsonConvert.getInstance().fromJson(players.getBody(), String[].class);

        opponents = new ArrayList<>(Arrays.asList(allPlayers));
        opponents.remove(this.playerNickname);

        board.setPlayers(this.playerNickname, opponents);
    }

    @Override
    public void update(Message update) {
        // TODO: extract the info from Message
        // Update the view
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
}


/**
 * public static void main(String[] args) {
 * <p>
 * String myPlayer = "ciao";
 * String opponent1 = "mio dio";
 * <p>
 * board = new Board(myPlayer, Arrays.asList(opponent1));
 * <p>
 * List<Cell> cellsToUpdate;
 * <p>
 * cellsToUpdate = new ArrayList<Cell>(Arrays.asList(
 * new Cell(new Position(1, 0), 1, false, null, ""),
 * new Cell(new Position(1, 1), 2, false, null, ""),
 * new Cell(new Position(0, 1), 3, false, null, ""),
 * new Cell(new Position(2, 4), 2, false, null, myPlayer),
 * new Cell(new Position(2, 2), 0, false, null, opponent1),
 * new Cell(new Position(1, 4), 1, false, null, myPlayer),
 * new Cell(new Position(3, 3), 2, false, null, opponent1),
 * new Cell(new Position(4, 4), 1, true, null, "")
 * ));
 * <p>
 * System.out.print(board.update(cellsToUpdate));
 * //System.out.println(Graphics.Color.FIRST_LEVEL+Graphics.Color.DOME.getEscape()+Graphics.Element.FEMALE_WORKER.getEscape()+" "+Graphics.Color.SECOND_LEVEL+"secondo"+Graphics.Color.THIRD_LEVEL+"terzo"+ Graphics.Color.RESET);
 * }
 */