package it.polimi.ingsw.psp44.network.communication;

import it.polimi.ingsw.psp44.util.Card;


/**
 * Group of classes that represents additional formats for bodies in Message.
 */
public class BodyTemplates {

    /**
     * Template for NEW_GAME message
     */
    public static class NewGame {
        private String playerNickname;
        private int numberOfPlayers;

        /**
         * Needed for Gson serialization-deserialization
         * see https://github.com/google/gson/blob/master/UserGuide.md#writing-an-instance-creator
         */
        private NewGame() {
        }

        public NewGame(String playerNickname, int numberOfPlayers) {
            this.playerNickname = playerNickname;
            this.numberOfPlayers = numberOfPlayers;
        }

        public String getPlayerNickname() {
            return playerNickname;
        }

        public int getNumberOfPlayers() {
            return numberOfPlayers;
        }
    }

    /**
     * Template for JOIN_GAME message
     */
    public static class JoinGame {
        private String playerNickname;
        private int gameId;

        /**
         * Needed for Gson serialization-deserialization
         * see https://github.com/google/gson/blob/master/UserGuide.md#writing-an-instance-creator
         */
        private JoinGame() {
        }

        public JoinGame(String playerNickname, int gameId) {
            this.playerNickname = playerNickname;
            this.gameId = gameId;
        }

        public String getPlayerNickname() {
            return playerNickname;
        }

        public int getGameId() {
            return gameId;
        }
    }

    /**
     * Template for ALL_PLAYER_CARDS message
     */
    public static class PlayerCard {
        private String playerNickname;
        private Card card;

        /**
         * Needed for Gson serialization-deserialization
         * see https://github.com/google/gson/blob/master/UserGuide.md#writing-an-instance-creator
         */
        private PlayerCard() {
        }

        public PlayerCard(String playerNickname, Card card) {
            this.playerNickname = playerNickname;
            this.card = card;
        }

        public String getPlayerNickname() {
            return playerNickname;
        }

        public Card getCard() {
            return this.card;
        }
    }


}
