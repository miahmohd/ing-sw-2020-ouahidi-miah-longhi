package it.polimi.ingsw.psp44.util.exception;

public final class ErrorCodes {
    public static final String DOME_PRESENT = "DOME_PRESENT";
    public static final String NULL_POS = "NULL_POS";
    public static final String OUT_OF_BOUNDS = "OUT_OF_BOUNDS";
    public static final String CAN_NOT_BUILD = "CAN_NOT_BUILD";
    public static final String CAN_NOT_UNBUILD = "CAN_NOT_UNBUILD";

    public static final String NULL_PLAYER = "NULL_PLAYER";
    public static final String PLAYER_IN_GAME = "PLAYER_IN_GAME";
    public static final String PLAYER_NOT_IN_GAME = "PLAYER_NOT_IN_GAME";
    public static final String NO_PLAYERS_IN_GAME = "NO_PLAYERS_IN_GAME";

    public static final String NULL_FILTER = "NULL_FILTER";
    public static final String FILTER_IN_COLLECTION = "FILTER_IN_COLLECTION";
    public static final String FILTER_NOT_IN_COLLECTION = "FILTER_NOT_IN_COLLECTION";
    public static final String NO_FILTER_IN_COLLECTION = "NO_FILTER_IN_COLLECTION";

    private ErrorCodes() {
    }
}
