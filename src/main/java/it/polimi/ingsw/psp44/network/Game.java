package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.server.controller.SetupController;

import java.util.Date;

/**
 * A class representing a single game. It is self-sustained.
 */
public class Game{

    private int maxPlayers;
    private long idMatch;
    private SetupController setupController;



    public Game(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.idMatch= new Date().getTime();
        this.setupController=new SetupController();
    }


    /**
     * Add a new player to the game. When the game reaches full capacity it starts automatically.
     * @param nickname
     * @param view
     */
    //TODO this must be syncronized
    public synchronized void addPlayer(String nickname, VirtualView view) {
        synchronized (setupController){
            setupController.addPlayer(nickname, view);
            if (this.isFull())
                setupController.start();
        }
    }

    //TODO this must be synchronized
    public boolean isFull() {
        return setupController.getRegisteredPlayer() == maxPlayers;
    }






    }
