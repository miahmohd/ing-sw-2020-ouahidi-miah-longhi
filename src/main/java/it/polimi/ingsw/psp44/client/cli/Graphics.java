package it.polimi.ingsw.psp44.client.cli;

/**
 * garafics based on ANSI standard @see
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

        /**
         * Up: \u001b[{n}A Down: \u001b[{n}B Right: \u001b[{n}C Left: \u001b[{n}D
         */

        DOME("\u001B[38;5;4m"),
        /**
         * dark blue
         */
        MY_PLAYER("\u001B[38;5;33m"),
        /**
         * light blue
         */
        OPPONENT_1("\u001B[38;5;94m"),
        /**
         * brown
         */
        OPPONENT_2("\u001B[38;5;15m"), /** white*/


        /**
         * TODO: add colors
         */
        MOVE_HIGHLIGHT("\u001B[48;5;m"),
        /**
         * white
         */
        BUILD_HIGHLIGHT("\u001B[48;5;15m"),
        /**
         * white
         */
        BOTH_HIGHLIGHT("\u001B[48;5;15m");
        /**
         * white
         */

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
        MALE_WORKER("\u2659 "),
        FEMALE_WORKER("\u2659 "),


        DOME("\uD835\uDEF7 "),
        EMPTY("  ");


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

        PADDING(""), NEW_LINE("\n");
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
