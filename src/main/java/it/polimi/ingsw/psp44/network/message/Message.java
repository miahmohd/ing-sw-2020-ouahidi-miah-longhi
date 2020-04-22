package it.polimi.ingsw.psp44.network.message;

import java.util.Map;

/**
 * Class based on w3 specification of HTTP @see https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html
 */
public class Message {

    /**
     * Code of message
     */
    private final Code code;
    /**
     * Additional infromation about the message
     */
    private final Map<String, String> headers;
    /**
     * Body of the message
     */
    private final String body;

    public Message(Code code) {
        this(code, "");
    }

    public Message(Code code, String body) {
        this(code, null, body);
    }


    public Message(Code code, Map<String, String> headers, String body) {
        this.code = code;
        this.headers = headers;
        this.body = body;
    }

    public Code getCode() {
        return code;
    }

    public Map<String, String> getHeader() {
        return this.headers;
    }

    public String getBody() {
        return body;
    }

    public enum Code {
        NEW_GAME,
        JOIN_GAME,
        CHOOSE_CARDS,
        START,
        CHOOSE_WORKER, CHOOSE_ACTION, TURN_ENDABLE, CHOSEN_ACTION, TURN_END, CHOSEN_WORKER
    }
}