package it.polimi.ingsw.psp44.network.communication;

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

    public static class PlayerCard{
        private String playerNickname;
        private String cardName;

        /**
         * Needed for Gson serialization-deserialization
         * see https://github.com/google/gson/blob/master/UserGuide.md#writing-an-instance-creator
         */
        private PlayerCard() {
        }

        public PlayerCard(String playerNickname, String cardName) {
            this.playerNickname = playerNickname;
            this.cardName = cardName;
        }

        public String getPlayerNickname() {
            return playerNickname;
        }

        public String getCardName() {
            return cardName;
        }
    }


}
