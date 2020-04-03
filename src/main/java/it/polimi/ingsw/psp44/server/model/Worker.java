package it.polimi.ingsw.psp44.server.model;

public class Worker {

    private final Sex sex;
    private final String playerNickname;

    /**
     * @param playerNickname player id to which the worker belongs.
     * @param sex            (e.g. Worker.Sex.Female)
     */
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
