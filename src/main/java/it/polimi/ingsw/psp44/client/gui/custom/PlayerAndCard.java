package it.polimi.ingsw.psp44.client.gui.custom;

import it.polimi.ingsw.psp44.network.communication.BodyTemplates;

/**
 * Player And card class binds a player card to a color
 */
public class PlayerAndCard {

    private final BodyTemplates.PlayerCard playerCard;
    private final String color;

    public PlayerAndCard(BodyTemplates.PlayerCard playerCard, String color) {
        this.playerCard = playerCard;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getPlayerNickname() {
        return playerCard.getPlayerNickname();
    }

    public int getCardId() {
        return playerCard.getCard().getId();
    }
}
