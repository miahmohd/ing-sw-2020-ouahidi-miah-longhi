package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.server.controller.SetupController;

import java.util.Date;

/**
 * A class representing a single game. It is self-sustained.
 */
public class Game {

    private final int maxPlayers;
    private final long id;
    private final SetupController setupController;


    public Game(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.id = new Date().getTime();
        this.setupController = new SetupController();
    }


    /**
     * Add a new player to the game. When the game reaches full capacity it starts automatically.
     *
     * @param nickname a unique string tha identify a player
     * @param view     the virtual-view bounded to the player
     */
    public synchronized void addPlayer(String nickname, VirtualView view) {
        setupController.addPlayer(nickname, view);
        if (this.isFull())
            setupController.start();
    }

    public synchronized boolean isFull() {
        return setupController.getRegisteredPlayer() == maxPlayers;
    }

    public long getId() {
        return this.id;
    }

}
