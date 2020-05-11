package it.polimi.ingsw.psp44.server;

import it.polimi.ingsw.psp44.server.controller.SetupController;
import it.polimi.ingsw.psp44.server.view.VirtualView;

/**
 * A class representing a single game. It is self-sustained.
 */
public class Lobby {

    private static int idGen = 0;

    private final int maxPlayers;
    private final int id;
    private final SetupController setupController;


    public Lobby(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.id = idGen++;
        this.setupController = new SetupController();
    }


    /**
     * Add a new player to the game.
     *
     * @param nickname a unique string tha identify a player
     * @param view     the virtual-view bounded to the player
     */
    public void addPlayer(String nickname, VirtualView view) {
        setupController.addPlayer(nickname, view);
    }

    public boolean contains(String nickname) {
        return setupController.getRegisteredPlayers().contains(nickname);
    }

    public boolean isFull() {
        return setupController.getRegisteredPlayers().size() == maxPlayers;
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
