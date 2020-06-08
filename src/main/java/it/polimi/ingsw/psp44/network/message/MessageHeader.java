package it.polimi.ingsw.psp44.network.message;

/**
 * Header of message, contains information for understanding the message
 */
public enum MessageHeader {
    CARDINALITY,
    ERROR,
    ERROR_DESCRIPTION,

    IS_TURN_ENDABLE,
    IS_TURN_END
}