package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.IViewSetup;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.network.IVirtual;

import java.util.*;

public class CLIViewSetup implements IViewSetup<Message>, Runnable {

    private StringBuffer display;
    private String playerNickname;
    private Scanner input;
    private Board board;
    private IVirtual<Message> virtualServer;
    private Map<String, Message.Code> gameOptions;


    public CLIViewSetup(Scanner input, Board board, Map<String, Message.Code> gameOptions){
        this.input = input;
        this.board = board;
        this.gameOptions = gameOptions;

    }


    public CLIViewSetup(){
        this(new Scanner(System.in), new Board(), new HashMap<>());

        initGameOptions();
    }


    @Override
    public void run() {
        String chosenOption, responseBody;
        Message response;

        //TODO: this prompt needs to be in another location
        System.out.println("What do you want to do? Join Game (J) | New Game (N)");

        chosenOption = input.nextLine();
        chosenOption = chosenOption.replace(" ", "").toLowerCase();

        responseBody = JsonConvert.getInstance().toJson(chosenOption, String.class);
        response = new Message(gameOptions.get(chosenOption), responseBody);

        virtualServer.sendMessage(response);
    }


    @Override
    public void chooseFromOptions(Message options) {

    }



    @Override
    public void chooseCardsFrom(Message cards) {
        //Display the cards and make the player choose according to the coso



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



    private void initGameOptions(){
        this.gameOptions.put("newgame", Message.Code.NEW_GAME);
        this.gameOptions.put("n", Message.Code.NEW_GAME);

        this.gameOptions.put("joingame", Message.Code.JOIN_GAME);
        this.gameOptions.put("j", Message.Code.JOIN_GAME);

    }

}
