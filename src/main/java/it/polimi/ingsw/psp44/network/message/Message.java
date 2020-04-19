package it.polimi.ingsw.psp44.network.message;

/**
 * Class based on w3 specification of HTTP @see https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html
 */
public class Message {

    /**
     * Code of message
     */
    private Code code;
    /**
     * Additional infromation about the message
     */
    private MessageHeader header;
    /**
     * Body of the message
     */
    private String body;

    public Message(Code code, String body) {
        this(code, null, body);
    }


    public Message(Code code, MessageHeader header, String body) {
        this.code = code;
        this.header = header;
        this.body = body;
    }

    public Code getCode() {
        return code;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public enum Code {
        NEW_GAME,
        JOIN_GAME,
        CHOOSE_CARDS
    }
}