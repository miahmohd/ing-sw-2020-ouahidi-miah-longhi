package it.polimi.ingsw.psp44.client;


import it.polimi.ingsw.psp44.client.cli.LobbyView;
import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.message.Message;

import java.io.IOException;

public class ClientApp {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;


    public static void main(String[] args) {
        try {
            IConnection<Message> socketConnection = new SocketConnection(HOST, PORT);
            LobbyView view = new LobbyView();
            VirtualServer virtualServer = new VirtualServer(socketConnection);

            view.setServer(virtualServer);

            Thread server = new Thread(virtualServer);
            server.start();

            server.join();
        } catch (IOException | InterruptedException e) {
            System.err.println("ERROR: " + e.getMessage());
        }

    }
}
