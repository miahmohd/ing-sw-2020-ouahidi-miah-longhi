package it.polimi.ingsw.psp44.client.gui.properties;

import it.polimi.ingsw.psp44.client.gui.custom.PlayerAndCard;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class GameProperty {
    private final int HEIGHT = 125;
    private final int OFFSET = 10;

    private SimpleBooleanProperty isTurnEndable;
    private SimpleListProperty<PlayerAndCard> playersAndCards;
    private SimpleStringProperty info;

    private SimpleIntegerProperty playersAndColorsMaxHeight;

    public GameProperty(boolean isTurnEndable, ObservableList playerAndCards, String info){
        this.playersAndCards = new SimpleListProperty<>(playerAndCards);
        this.isTurnEndable = new SimpleBooleanProperty(isTurnEndable);
        this.info = new SimpleStringProperty(info);
        this.playersAndColorsMaxHeight = new SimpleIntegerProperty();

        this.playersAndCards.addListener((InvalidationListener) (listener) -> playersAndColorsMaxHeight.set(playerAndCards.size()*HEIGHT + OFFSET));
    }

    public SimpleBooleanProperty isTurnEndableProperty() {
        return isTurnEndable;
    }

    public SimpleListProperty<PlayerAndCard> playersAndCardsProperty() {
        return playersAndCards;
    }

    public SimpleStringProperty infoProperty() {
        return info;
    }

    public SimpleIntegerProperty playersAndColorsMaxHeightProperty() {
        return playersAndColorsMaxHeight;
    }
}
