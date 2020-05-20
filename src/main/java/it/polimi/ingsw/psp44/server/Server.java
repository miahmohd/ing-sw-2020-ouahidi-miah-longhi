package it.polimi.ingsw.psp44.server;

import it.polimi.ingsw.psp44.network.SocketConnection;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.communication.BodyTemplates;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.server.view.VirtualView;
import it.polimi.ingsw.psp44.util.R;
import it.polimi.ingsw.psp44.util.exception.ErrorCodes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ExecutorService executor;
    private final int port;

    //TODO servono?
    private final List<VirtualView> views = Collections.synchronizedList(new ArrayList<>());

    private final List<Lobby> lobbies;

    public Server(int port) {
        this.port = port;
        this.executor = Executors.newFixedThreadPool(100);
        this.lobbies = new ArrayList<>();
    }

    /**
     * Waits for new players
     */
    public void start() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                Socket socket = server.accept();

                VirtualView view = new VirtualView(new SocketConnection(socket));
                this.views.add(view);

                setHandlers(view);
                beginCommunication(view);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Callback that handles and processes "new lobby" message type.
     * It creates a lobby with a given size and joins the first player.
     * If the lobby cannot be created then it redirect the player to NEW_OR_JOIN.
     *
     * @param view    the VirtualView that sent the message
     * @param message message with code NEW_GAME containing information for creating a new lobby
     */
    public void newGameMessageHandler(VirtualView view, Message message) {
        BodyTemplates.NewGame body = BodyFactory.fromNewGame(message.getBody());
        Map<MessageHeader, String> headers = new EnumMap<>(MessageHeader.class);
        int nPlayers = body.getNumberOfPlayers();

        if (nPlayers != 2 && nPlayers != 3) {
            headers.put(MessageHeader.ERROR, String.valueOf(true));
            headers.put(MessageHeader.ERROR_DESCRIPTION, String.format(R.getAppProperties().get(ErrorCodes.ILLEGAL_GAME_PARAMS), nPlayers));

            view.sendMessage(new Message(Message.Code.NEW_OR_JOIN, headers));
            return;
        }

        synchronized (this.lobbies) {
            Lobby lobby = new Lobby(body.getNumberOfPlayers());
            lobby.addPlayer(body.getPlayerNickname(), view);

            this.lobbies.add(lobby);

            view.sendMessage(new Message(Message.Code.GAME_CREATED, String.valueOf(lobby.getId())));
        }
    }

    /**
     * Callback that handles and processes "join lobby" message type.
     * If the player successfully joined it start the setup phase of the game.
     * If the game to join does not exist or is full, it redirects the player to NEW_OR_JOIN.
     *
     * @param view    the VirtualView that sent the message
     * @param message message with code NEW_GAME containing information for joining an existing lobby
     */
    public void joinGameMessageHandler(VirtualView view, Message message) {
        BodyTemplates.JoinGame body = BodyFactory.fromJoinGame(message.getBody());
        Map<MessageHeader, String> headers = new EnumMap<>(MessageHeader.class);
        String nickname = body.getPlayerNickname();

        synchronized (this.lobbies) {
            Lobby toJoin = this.lobbies.stream()
                    .filter(lobby -> !lobby.isFull() && lobby.getId() == body.getGameId())
                    .findFirst()
                    .orElse(null);

            headers.put(MessageHeader.ERROR, String.valueOf(false));

            if (toJoin == null) {
                headers.put(MessageHeader.ERROR_DESCRIPTION, R.getAppProperties().get(ErrorCodes.UNAVAILABLE_GAME));
            } else if (toJoin.contains(nickname)) {
                headers.put(MessageHeader.ERROR_DESCRIPTION, R.getAppProperties().get(ErrorCodes.UNAVAILABLE_NICKNAME));
            } else {
                view.sendMessage(new Message(Message.Code.GAME_JOINED));
                toJoin.addPlayer(body.getPlayerNickname(), view);
                if (toJoin.isFull())
                    toJoin.start();
                return;
            }
            headers.put(MessageHeader.ERROR, String.valueOf(true));
            view.sendMessage(new Message(Message.Code.NEW_OR_JOIN, headers));
        }
    }


    /**
     * Callback that handles and process CLIENT_DISCONNECTED message type.
     * This callback is called when the client disconnects before joining any Lobby.
     * Simply closes the connection with the view.
     * Once the view joins a Lobby, this callback will be overwritten with additional processing.
     *
     * @param view    the view that disconnected
     * @param message message with code CLIENT_DISCONNECTED
     */
    public void clientDisconnectedMessageHandler(VirtualView view, Message message) {
        System.out.println("Disconnecting server");
        view.close();
    }


    private void setHandlers(VirtualView view) {
        view.addMessageHandler(Message.Code.NEW_GAME, this::newGameMessageHandler);
        view.addMessageHandler(Message.Code.JOIN_GAME, this::joinGameMessageHandler);
        view.addMessageHandler(Message.Code.CLIENT_DISCONNECTED, this::clientDisconnectedMessageHandler);
    }

    private void beginCommunication(VirtualView view) {
        view.sendMessage(new Message(Message.Code.NEW_OR_JOIN));
        executor.execute(view);
    }



}
