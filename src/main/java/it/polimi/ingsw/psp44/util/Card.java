package it.polimi.ingsw.psp44.util;

/**
 * This class contains all the information of a gods card
 * It's used when the player has to chose the gods to use in the match
 */
public class Card {
    private int id;
    private String title;
    private String subtitle;
    private String description;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }
}
