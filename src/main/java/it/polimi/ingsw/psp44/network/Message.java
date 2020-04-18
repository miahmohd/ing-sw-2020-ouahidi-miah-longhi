package it.polimi.ingsw.psp44.network;

/**
 * Class based on w3 specification of HTTP @see https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html 
 */
public class Message {

    /**
     * Code of message
     */
    private String code;

    /**
     * Additional infromation about the message
     */
    private MessageHeader header;

    /**
     * Body of the message
     */
    private String body;


    public Message(String code, String body){
        this(code, null, body);
    }


    public Message(String code, MessageHeader header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getCode() {
        return code;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}