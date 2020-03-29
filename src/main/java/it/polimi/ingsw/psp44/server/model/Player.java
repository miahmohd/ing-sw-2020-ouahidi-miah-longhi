package it.polimi.ingsw.psp44.server.model;

public class Player {

    private final String nickname;
    private String cardName;

    /**
     *
     * @param nickname identifies the player
     */
    public Player(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

}
