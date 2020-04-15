package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.util.network.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ExecutorService executor = Executors.newFixedThreadPool(100);
    private int port;

    //TODO servono?
    private List<Connection> connections = Collections.synchronizedList(new ArrayList<>());

    private Game game;

    public Server(int port) {
        this.port = port;
    }

    /**
     * Waits for new players
     */
    public void start() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                Socket socket = server.accept();

                Connection connection = new Connection(socket);
                this.connections.add(connection);

                VirtualView view = new VirtualView(connection);
                view.addMessageHandler(this::newGameMessageHandler);
                view.addMessageHandler(this::joinGameMessageHandler);

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
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    private boolean newGameMessageHandler(VirtualView view, Message message) {
        //todo definire header come costanti e/o file
        if (message.getHeader() == "new game") {
            //todo gestire il ritenta al posto dell'errore
            if (this.game != null)// ricavare il numero da nickname.
                throw new IllegalStateException();

            this.game = new Game(Integer.parseInt(message.getBody())); // ricavare il numero da message.
            this.game.addPlayer(message.getBody(), view); // ricavare il nickname da message
            return true;
        }
        return false;
    }

    /**
     * Callback that handles and processes "join game" message type.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information for joining an existing game
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    private boolean joinGameMessageHandler(VirtualView view, Message message) {
        // todo see todos in this::newGameMessageHandler
        if (message.getHeader() == "join game") {
            String nickname = message.getBody();
            if (this.game == null || this.game.containsPlayer(nickname) || this.game.isFull())
                throw new IllegalStateException();

            this.game.addPlayer(nickname, view);
            return true;
        }
        return false;
    }


}
