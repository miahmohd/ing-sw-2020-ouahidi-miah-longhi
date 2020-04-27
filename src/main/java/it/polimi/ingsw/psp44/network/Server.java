package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.network.communication.BodyTemplates;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.server.view.VirtualView;
import it.polimi.ingsw.psp44.util.JsonConvert;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ExecutorService executor;
    private final int port;

    //TODO servono?
    private final List<SocketConnection> connections = Collections.synchronizedList(new ArrayList<>());

    private Game game;

    public Server(int port) {
        this.port = port;
        this.executor = Executors.newFixedThreadPool(100);
    }

    /**
     * Waits for new players
     */
    public void start() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                Socket socket = server.accept();

                SocketConnection connection = new SocketConnection(socket);
                this.connections.add(connection);

                VirtualView view = new VirtualView(connection);

                view.addMessageHandler(Message.Code.NEW_GAME, this::newGameMessageHandler);
                view.addMessageHandler(Message.Code.JOIN_GAME, this::joinGameMessageHandler);

                executor.execute(view);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Callback that handles and processes "new game" message type.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information for creating a new game
     */
    private void newGameMessageHandler(VirtualView view, Message message) {
        //todo definire header come costanti e/o file
        //todo gestire il ritenta al posto dell'errore
        if (this.game != null)// ricavare il numero da nickname.
            throw new IllegalStateException();
        BodyTemplates.FirstMessage firstMessage = JsonConvert.getInstance().fromJson(message.getBody(), BodyTemplates.FirstMessage.class);

        this.game = new Game(firstMessage.getNumberOfPlayers()); // ricavare il numero da message.
        this.game.addPlayer(firstMessage.getPlayerNickname(), view); // ricavare il nickname da message
    }

    /**
     * Callback that handles and processes "join game" message type.
     *
     * @param view    the VirtualView that sent the message
     * @param message the message containing information for joining an existing game
     */
    private void joinGameMessageHandler(VirtualView view, Message message) {
        // todo see todos in this::newGameMessageHandler

        if (this.game == null /*|| this.game.containsPlayer(nickname) */ || this.game.isFull())
            throw new IllegalStateException();
        BodyTemplates.FirstMessage firstMessage = JsonConvert.getInstance().fromJson(message.getBody(), BodyTemplates.FirstMessage.class);

        this.game.addPlayer(firstMessage.getPlayerNickname(), view);
    }


}
