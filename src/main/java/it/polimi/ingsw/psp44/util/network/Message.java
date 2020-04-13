package it.polimi.ingsw.psp44.util.network;

public class Message {

    private String header;
    private String body;

    public Message(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}