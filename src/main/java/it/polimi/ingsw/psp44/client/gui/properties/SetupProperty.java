package it.polimi.ingsw.psp44.client.gui.properties;

import it.polimi.ingsw.psp44.util.Card;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class SetupProperty {

    private SimpleListProperty<Card> chooseCards;
    private SimpleListProperty<Card> chosenCards;
    private SimpleBooleanProperty isStartGame;
    private SimpleBooleanProperty isNotStartGame;
    private SimpleBooleanProperty visibleAndManaged;
    private SimpleBooleanProperty isGameStarted;
    private SimpleStringProperty startText;

    private int maxChosenCardsSize;

    public SetupProperty(boolean isStartGame, boolean visibleAndManaged, ObservableList chooseCardsProperty, ObservableList chosenCardsProperty, String startText) {
        this.isStartGame = new SimpleBooleanProperty(isStartGame);
        this.isNotStartGame = new SimpleBooleanProperty(!isStartGame);
        this.visibleAndManaged = new SimpleBooleanProperty(visibleAndManaged);
        this.startText = new SimpleStringProperty(startText);

        this.chooseCards = new SimpleListProperty<>(chooseCardsProperty);
        this.chosenCards = new SimpleListProperty<>(chosenCardsProperty);

        this.isGameStarted = new SimpleBooleanProperty(false);
    }

    public void moveCardFromChooseToChosen(Card selectedCard) {
        chooseCards.remove(selectedCard);
        chosenCards.add(selectedCard);
        updateStatus();
    }

    public void moveCardFromChosenToChoose(Card selectedCard) {
        chosenCards.remove(selectedCard);
        chooseCards.add(selectedCard);
        updateStatus();
    }

    public void addCards(Card[] fromCards) {
        chooseCards.addAll(fromCards);
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
    public SimpleBooleanProperty visibleAndManagedProperty() {
        return visibleAndManaged;
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

    private boolean isChosenCardsAtMaxSize(){
        return chosenCards.size() == maxChosenCardsSize;
    }

    private void updateStatus() {
        visibleAndManaged.set(!chosenCards.isEmpty());
        isStartGame.set(isChosenCardsAtMaxSize());
        isNotStartGame.set(!isChosenCardsAtMaxSize());
    }

    public Card[] getChosenCards() {
        return chosenCards.toArray(Card[]::new);
    }

    public void disableAll() {
        isStartGame.set(true);
        isNotStartGame.set(true);
        isGameStarted.set(true);
    }

    public SimpleBooleanProperty isGameStartedProperty() {
        return this.isGameStarted;
    }
}
