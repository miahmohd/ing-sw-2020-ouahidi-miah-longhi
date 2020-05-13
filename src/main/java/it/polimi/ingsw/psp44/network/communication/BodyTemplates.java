package it.polimi.ingsw.psp44.network.communication;

import it.polimi.ingsw.psp44.util.Card;

public class BodyTemplates {

    /**
     * Template for NewGame message
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


    public static class ChosenCard {
        private Card chosen;
        private Card[] rest;

        /**
         * Needed for Gson serialization-deserialization
         * see https://github.com/google/gson/blob/master/UserGuide.md#writing-an-instance-creator
         */
        private ChosenCard() {
        }

        public ChosenCard(Card chosen, Card[] rest) {
            this.chosen = chosen;
            this.rest = rest;
        }

        public Card getChosen() {
            return chosen;
        }

        public Card[] getRest() {
            return rest;
        }
    }

}
