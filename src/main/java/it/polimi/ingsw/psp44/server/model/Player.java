package it.polimi.ingsw.psp44.server.model;

/**
 * Player is determined by its nickname
 * Two players with the same nickname in the same Lobby are the same Player
 */
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        if (nickname == null) {
            return other.nickname == null;
        } else return nickname.equals(other.nickname);
    }


}
