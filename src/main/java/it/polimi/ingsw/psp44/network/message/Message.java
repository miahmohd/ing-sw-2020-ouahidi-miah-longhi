package it.polimi.ingsw.psp44.network.message;

import java.util.Map;

/**
 * Class based on w3 specification of HTTP @see https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html
 */
public class Message {

    /**
     * Code of message
     */
    private Code code;
    /**
     * Additional information about the message
     */
    private Map<MessageHeader, String> headers;
    /**
     * Body of the message
     */
    private String body;

    /**
     * Needed for Gson serialization-deserialization
     * see https://github.com/google/gson/blob/master/UserGuide.md#writing-an-instance-creator
     */
    private Message() {
    }

    public Message(Code code) {
        this(code, "");
    }

    public Message(Code code, Map<MessageHeader, String> headers) {
        this(code, headers, "");
    }

    public Message(Code code, String body) {
        this(code, null, body);
    }


    public Message(Code code, Map<MessageHeader, String> headers, String body) {
        this.code = code;
        this.headers = headers;
        this.body = body;
    }

    public Code getCode() {
        return code;
    }

    public Map<MessageHeader, String> getHeader() {
        return this.headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addHeader(MessageHeader header, String value) {
        headers.put(header, value);
    }

    public void removeHeader(MessageHeader header) {
        headers.remove(header);
    }

    public enum Code {
        NEW_OR_JOIN,
        GAME_CREATED,
        GAME_JOINED,

        NEW_GAME,
        JOIN_GAME,

        CHOOSE_CARDS,
        CHOSEN_CARDS,

        CHOOSE_CARD,
        CHOSEN_CARD,

        CHOOSE_WORKERS_INITIAL_POSITION,
        CHOSEN_WORKERS_INITIAL_POSITION,

        ALL_PLAYER_NICKNAMES,
        ALL_PLAYER_CARDS,

        START_TURN,
        END_TURN,

        CHOOSE_WORKER,
        CHOOSE_ACTION,
        CHOSEN_WORKER,
        CHOSEN_ACTION,
        UPDATE,
        WON,
        LOST,

        ACTIVE_TURN
    }
}