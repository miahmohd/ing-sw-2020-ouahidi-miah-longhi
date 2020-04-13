package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.util.network.Connection;
import it.polimi.ingsw.psp44.util.network.Message;

public class SocketConnection extends Connection<Message> {

    private static final String HOST = "localhost";
    private static final int PORT = 3030;


    private String host;
    private int port;

    /**
     * Creates a socketConnection initializing the socket
     */
    public SocketConnection(String host, int port) {
		this.host = host;
		this.port = port;
    }
    
    public SocketConnection() {
        this(HOST, PORT);
    }

    @Override
    public void connect(){
        //TODO: Begin connection so that you are ready for reading and writing
    }

    @Override
    public Message read() {
        // TODO: Auto-generated method stub
        return null;
    }

    @Override
    public void write(Message message) {
        // TODO: Auto-generated method stub

    }



}