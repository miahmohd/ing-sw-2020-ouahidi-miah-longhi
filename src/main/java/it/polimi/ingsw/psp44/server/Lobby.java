package it.polimi.ingsw.psp44.server;

import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.server.controller.SetupController;
import it.polimi.ingsw.psp44.server.view.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a single game. It is self-sustained.
 */
public class Lobby {

    private static int idGen = 0;
    private final int maxPlayers;
    private final int id;
    private final SetupController setupController;
    private List<VirtualView> playersInWaiting;



    public Lobby(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.id = idGen++;

        this.playersInWaiting = new ArrayList<>();
        this.setupController = new SetupController();
    }


    /**
     * Add a new player to the game.
     *
     * @param nickname a unique string tha identify a player
     * @param view     the virtual-view bounded to the player
     */
    public void addPlayer(String nickname, VirtualView view) {
        this.playersInWaiting.add(view);
        this.setupController.addPlayer(nickname, view);
        view.setLobbyId(this.id);
        view.addMessageHandler(Message.Code.CLIENT_DISCONNECTED, this::clientDisconnectedMessageHandler);
    }


    /**
     * Callback that handles and process CLIENT_DISCONNECTED message type.
     * This callback is called when the client disconnects before joining any Lobby.
     * This callback overwrites the callback added from Server.
     * Disconnect all the connected players.
     *
     * @param view    the view that disconnected
     * @param message message with code CLIENT_DISCONNECTED
     */
    public void clientDisconnectedMessageHandler(VirtualView view, Message message) {
        System.out.println("Disconnecting lobby");
        playersInWaiting.forEach(VirtualView::close);
    }


    public boolean contains(String nickname) {
        return setupController.getRegisteredPlayers().contains(nickname);
    }

    public boolean isFull() {
        return this.playersInWaiting.size() == maxPlayers;
    }

    /**
     * Start the setup phase of the game.
     */
    public void start() {
        this.setupController.start();
    }

    public long getId() {
        return this.id;
    }

}
