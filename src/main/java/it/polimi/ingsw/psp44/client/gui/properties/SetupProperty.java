package it.polimi.ingsw.psp44.client.gui.properties;

import it.polimi.ingsw.psp44.util.Card;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

/**
 * Setup Property containing properties to bind to SetupView
 */
public class SetupProperty {

    private final SimpleListProperty<Card> chooseCards;
    private final SimpleListProperty<Card> chosenCards;
    private final SimpleBooleanProperty isStartGame;
    private final SimpleBooleanProperty isNotStartGame;
    private final SimpleBooleanProperty isGameStarted;
    private final SimpleStringProperty startText;

    private int maxChosenCardsSize;

    public SetupProperty(boolean isStartGame, ObservableList chooseCardsProperty, ObservableList chosenCardsProperty, String startText) {
        this.isStartGame = new SimpleBooleanProperty(isStartGame);
        this.isNotStartGame = new SimpleBooleanProperty(!isStartGame);
        this.startText = new SimpleStringProperty(startText);

        this.chooseCards = new SimpleListProperty<>(chooseCardsProperty);
        this.chosenCards = new SimpleListProperty<>(chosenCardsProperty);

        this.isGameStarted = new SimpleBooleanProperty(false);
    }

    /**
     * Moves selectedCard from the choose Cards Property to chosen Cards Property
     *
     * @param selectedCard card to move
     */
    public void moveCardFromChooseToChosen(Card selectedCard) {
        chooseCards.remove(selectedCard);
        chosenCards.add(selectedCard);
        updateStatus();
    }

    /**
     * Moves selectedCard from the chosen Cards Property to choose Cards Property
     *
     * @param selectedCard card to move
     */
    public void moveCardFromChosenToChoose(Card selectedCard) {
        chosenCards.remove(selectedCard);
        chooseCards.add(selectedCard);
        updateStatus();
    }

    /**
     * Adds cards to choose Cards Property
     *
     * @param cards to be added
     */
    public void addCards(Card[] cards) {
        chooseCards.addAll(cards);
    }

    public SimpleListProperty<Card> chooseCardsProperty() {
        return chooseCards;
    }

    public SimpleListProperty<Card> chosenCardsProperty() {
        return chosenCards;
    }

    public SimpleBooleanProperty isStartGameProperty() {
        return isStartGame;
    }

    public SimpleBooleanProperty isNotStartGameProperty() {
        return isNotStartGame;
    }

    public SimpleStringProperty startTextProperty() {
        return startText;
    }

    public void setStartText(String startText) {
        this.startText.set(startText);
    }

    public void setMaxChosenCardsSize(int maxChosenCardsSize) {
        this.maxChosenCardsSize = maxChosenCardsSize;
    }

    private boolean isChosenCardsAtMaxSize() {
        return chosenCards.size() == maxChosenCardsSize;
    }

    private void updateStatus() {
        isStartGame.set(isChosenCardsAtMaxSize());
        isNotStartGame.set(!isChosenCardsAtMaxSize());
    }

    public Card[] getChosenCards() {
        return chosenCards.toArray(Card[]::new);
    }

    /**
     * Set all boolean properties to true
     */
    public void disableAll() {
        isStartGame.set(true);
        isNotStartGame.set(true);
        isGameStarted.set(true);
    }

    public SimpleBooleanProperty isGameStartedProperty() {
        return this.isGameStarted;
    }
}
