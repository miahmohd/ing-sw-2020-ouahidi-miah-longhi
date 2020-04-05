package it.polimi.ingsw.psp44.server.model;

public class Player {
    /**
     * player identifier
     */
    private final String nickname;

    /**
     * the god card choose by the player
     */
    private String cardName;


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
