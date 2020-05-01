package it.polimi.ingsw.psp44.util;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getId() == card.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return this.getId() + "\n" +
                this.getTitle() + "\n" +
                this.getSubtitle() + "\n" +
                this.getDescription();
    }
}
