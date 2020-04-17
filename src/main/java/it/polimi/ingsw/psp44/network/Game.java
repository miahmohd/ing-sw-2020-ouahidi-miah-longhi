package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.server.controller.CardController;
import it.polimi.ingsw.psp44.server.controller.Controller;
import it.polimi.ingsw.psp44.server.controller.SetupController;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.network.Message;

import java.sql.Timestamp;
import java.util.*;

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
    public void addPlayer(String nickname, VirtualView view) {
        setupController.addPlayer(nickname,view);

        if (this.isFull())
            setupController.setup(playerViews);
    }

    //TODO this must be synchronized
    public boolean isFull() {
        return controller.getRegisteredPlayer() == maxPlayers;
    }






    }
