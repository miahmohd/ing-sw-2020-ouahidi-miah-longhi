package it.polimi.ingsw.psp44.client.cli;

/**
 * graphics based on ANSI standard @see
 * https://stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences
 * https://www.lihaoyi.com/post/BuildyourownCommandLinewithANSIescapecodes.html
 */
public class Graphics {

    /**
     * NOTE: 38 is for foreground colors and 48 is for background colors OTHER NOTE:
     * we are using a
     */
    public enum Color implements AnsiElement {

        /**
         * Levels are a shade of a specific color, for now we are going with grey
         * GROUND_LEVEL is green ATTENTION: if you change the colors change THIS COMMENT
         */
        GROUND_LEVEL("\u001B[48;5;48m"), FIRST_LEVEL("\u001B[48;5;255m"), SECOND_LEVEL("\u001B[48;5;250m"),
        THIRD_LEVEL("\u001B[48;5;245m"),

        DOME("\u001B[38;5;4m"),
        /**
         * dark blue
         */
        MY_PLAYER("\u001b[36m"),
        /**
         * light blue
         */
        OPPONENT_1("\u001b[31m"),
        /**
         * brown
         */
        OPPONENT_2("\u001b[35m"),
        /**
         * white
         */


        POSITION_HIGHLIGHT("\u001B[48;5;20m");

        static final String RESET = "\u001B[0m";

        private final String escape;

        Color(String escape) {
            this.escape = escape;
        }

        @Override
        public String getEscape() {
            return this.escape;
        }

        @Override
        public String toString() {
            return this.escape;
        }

    }

    /**
     * http://www.fileformat.info/info/unicode/char/search.htm
     */
    public enum Element implements AnsiElement {
        /**
         * Pawn looks better
         */
        MALE_WORKER("\u2654 "),
        FEMALE_WORKER("\u2655 "),


        DOME("\u0398 "),
        EMPTY("  "),

        SEPARATOR(" : ");

        private final String escape;

        Element(String escape) {
            this.escape = escape;
        }

        @Override
        public String getEscape() {
            return this.escape;
        }

        @Override
        public String toString() {
            return this.escape;
        }

    }


    public enum Behaviour implements AnsiElement {

        NEW_LINE("\n"),
        CLEAR("\033[H\033[2J");

        private final String escape;

        Behaviour(String escape) {
            this.escape = escape;
        }

        @Override
        public String getEscape() {
            return this.escape;
        }

        @Override
        public String toString() {
            return this.escape;
        }
    }


    private interface AnsiElement {
        /**
         * retrieve the escape char
         *
         * @return escape
         */
        String getEscape();

    }


}
