package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.message.Message;

import java.util.HashMap;
import java.util.Map;

public class LobbyView {

    private Console console;
    private VirtualServer virtualServer;

    private String playerNickname;
    private Map<String, Message.Code> gameOptions;


    public LobbyView(Console console){
        this.console = console;

        this.gameOptions = new HashMap<>();

        initGameOptions();
    }

    public LobbyView() {
        this(new Console());
    }

    public void newJoin(Message joinOrNew){
        String body;
        Message message;
        Message.Code messageCode;
        //TODO: ADD header support

        int numberOfPlayers;
        int gameId;

        console.writeLine("Gimme your nickname ");
        this.playerNickname = console.readLine();

        messageCode = this.getMessageCode();

        if (messageCode == Message.Code.NEW_GAME) {
            console.writeLine("How many Players ");
            numberOfPlayers = console.readNumber();
            body = BodyFactory.toNewGame(this.playerNickname, numberOfPlayers);

        } else {
            console.writeLine("gimme Code id ");
            gameId = console.readNumber();
            body = BodyFactory.toJoinGame(this.playerNickname, gameId);
        }
        message = new Message(messageCode, body);

        virtualServer.sendMessage(message);
    }

    public void gameCreated(Message gameCreated){
        console.writeLine(gameCreated.getBody());
        console.writeLine("game created now wait and don't do anything, please");
        changeView();
    }

    public void gameJoined(Message gameJoined) {
        console.writeLine(gameJoined.getBody());
        console.writeLine("game joined now wait and don't do anything, please");
        changeView();
    }

    public void setServer(VirtualServer virtual){
        this.virtualServer = virtual;

        virtualServer.addRoute(Message.Code.NEW_OR_JOIN, this::newJoin);
        virtualServer.addRoute(Message.Code.GAME_CREATED, this::gameCreated);
        virtualServer.addRoute(Message.Code.GAME_JOINED, this::gameJoined);
    }

    private void changeView(){
        SetupView setupView = new SetupView(this.playerNickname, this.console);
        setupView.setServer(this.virtualServer);
    }

    private Message.Code getMessageCode(){
        String chosenOption;
        do {
            console.writeLine("What you want to do? New Game or Join Game? N/J ");
            chosenOption = console.readLine();
            chosenOption = chosenOption.replace(" ", "").toLowerCase();
        } while (gameOptions.get(chosenOption) == null);

        return gameOptions.get(chosenOption);
    }

    private void initGameOptions() {
        this.gameOptions.put("newgame", Message.Code.NEW_GAME);
        this.gameOptions.put("n", Message.Code.NEW_GAME);

        this.gameOptions.put("joingame", Message.Code.JOIN_GAME);
        this.gameOptions.put("j", Message.Code.JOIN_GAME);
    }
}
