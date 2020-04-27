package it.polimi.ingsw.psp44.network.communication;

import it.polimi.ingsw.psp44.server.model.Card;

public class BodyTemplates {

    /**
     * Template for first message
     */
    public static class FirstMessage {
        private String playerNickname;
        private int numberOfPlayers;

        public FirstMessage(String playerNickname, int numberOfPlayers) {
            this.playerNickname = playerNickname;
            this.numberOfPlayers = numberOfPlayers;
        }

        public FirstMessage(String nickname){
            this(nickname, 0);
        }

        public String getPlayerNickname() {
            return playerNickname;
        }

        public int getNumberOfPlayers() {
            return numberOfPlayers;
        }
    }


    public static class CardMessage {
        private Card chosen;
        private Card[] rest;

        public CardMessage(Card chosen, Card[] rest) {
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
