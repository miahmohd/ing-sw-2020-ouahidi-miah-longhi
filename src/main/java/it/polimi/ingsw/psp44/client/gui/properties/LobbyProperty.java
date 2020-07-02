package it.polimi.ingsw.psp44.client.gui.properties;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Lobby Property containing properties to bind to LobbyView
 */
public class LobbyProperty {
    private final StringProperty infoText;
    private final BooleanProperty newGame;
    private final BooleanProperty joinGame;
    private final StringProperty optionText;
    private final StringProperty optionPromptText;
    private final StringProperty nicknameText;

    private final String newGameOptionPromptText;
    private final String joinGameOptionPromptText;

    public LobbyProperty(boolean newGame, String newGameOptionPromptText, String joinGameOptionPromptText, String gameOptionText, String nicknameText){
        this.newGameOptionPromptText = newGameOptionPromptText;
        this.joinGameOptionPromptText = joinGameOptionPromptText;

        this.newGame = new SimpleBooleanProperty(newGame);
        this.joinGame = new SimpleBooleanProperty(!newGame);

        this.optionText = new SimpleStringProperty(gameOptionText);
        this.optionPromptText = new SimpleStringProperty();

        this.nicknameText = new SimpleStringProperty(nicknameText);

        this.infoText = new SimpleStringProperty();
        setGameOptionPromptText();
    }


    public StringProperty nicknameTextProperty() {
        return nicknameText;
    }

    public StringProperty optionPromptTextProperty() {
        return optionPromptText;
    }

    public BooleanProperty newGameProperty() {
        return newGame;
    }

    public BooleanProperty joinGameProperty() {
        return joinGame;
    }

    public StringProperty optionTextProperty() {
        return optionText;
    }

    public StringProperty infoTextProperty() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText.set(infoText);
    }

    public boolean isNewGame() {
        return newGame.get();
    }

    public String getOptionText() {
        return optionText.get();
    }

    public String getNicknameText() {
        return nicknameText.get();
    }

    /**
     * Flips the join game and new game properties
     */
    public void flipGameOptions(){
        this.joinGame.set(!this.joinGame.get());
        this.newGame.set(!this.newGame.get());
        setGameOptionPromptText();
    }

    private void setGameOptionPromptText(){
        if(this.newGameProperty().get()){
            this.optionPromptText.set(newGameOptionPromptText);
        } else {
            this.optionPromptText.set(joinGameOptionPromptText);
        }
    }
}
