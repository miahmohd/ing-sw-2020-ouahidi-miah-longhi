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

    /**
     * Needed for Gson serialization-deserialization
     * see https://github.com/google/gson/blob/master/UserGuide.md#writing-an-instance-creator
     */
    private Card() {
    }

    public Card(int id, String title, String subtitle, String description) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
    }

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
