package it.polimi.ingsw.psp44.server.model;

/**
 * Player placeholder on the board. Each player has two Worker and he can use them to move or build blocks on the board
 */
public class Worker {

    /**
     * sex of the worker (male or female) it will be used by some god powers)
     */
    private final Sex sex;

    /**
     * player id to which the worker belongs.
     */
    private final String playerNickname;


    public Worker(String playerNickname, Sex sex) {
        this.playerNickname = playerNickname;
        this.sex = sex;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public Sex getSex() {
        return sex;
    }


    public enum Sex {
        MALE,
        FEMALE
    }

}
