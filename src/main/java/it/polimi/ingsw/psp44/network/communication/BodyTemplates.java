package it.polimi.ingsw.psp44.network.communication;

import it.polimi.ingsw.psp44.util.Card;
import it.polimi.ingsw.psp44.util.Position;

public class BodyTemplates {

    /**
     * Template for NewGame message
     */
    public static class NewGame {
        private String playerNickname;
        private int numberOfPlayers;

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

    public static class ActionMessage {

        private final Position target;
        private final boolean build;
        private final boolean move;

        /**
         * Not set if the action is move
         */
        private final int level;


        /**
         * Not set if the action is move
         */
        private final boolean dome;

        public ActionMessage(Position target, boolean build, boolean move, int level, boolean dome) {
            this.target = target;
            this.build = build;
            this.move = move;
            this.level = level;
            this.dome = dome;
        }

        public Position getTarget() {
            return target;
        }


        public boolean isBuild() {
            return build;
        }


        public boolean isMove() {
            return move;
        }

        public int getLevel() {
            return level;
        }

        public boolean isDome() {
            return dome;
        }


    }
}
