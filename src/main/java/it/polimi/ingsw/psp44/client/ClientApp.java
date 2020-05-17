package it.polimi.ingsw.psp44.client;


import it.polimi.ingsw.psp44.client.cli.LobbyView;
import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.message.Message;

import java.io.IOException;

public class ClientApp {
    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final int DEFAULT_PORT = 8080;


    public static void main(String[] args) {
        String hostname = DEFAULT_HOSTNAME;
        int port = DEFAULT_PORT;

//        if (args.length > 0) {
//            String[] host = args[0].split(":");
//            hostname = host[0];
//            port = Integer.parseInt(host[1]);
//        }

        try {
            IConnection<Message> socketConnection = new SocketConnection(hostname, port);
            ILobbyView view = new LobbyView();
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
