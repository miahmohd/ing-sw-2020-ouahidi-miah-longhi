package it.polimi.ingsw.psp44.client;


import it.polimi.ingsw.psp44.client.gui.LobbyView;
import it.polimi.ingsw.psp44.client.gui.SetupView;
import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.SocketConnection;
import it.polimi.ingsw.psp44.util.ConfigCodes;
import it.polimi.ingsw.psp44.util.R;

import java.io.IOException;
import java.net.Socket;

public class ClientApp {
    private static final String DEFAULT_HOSTNAME = R.getAppProperties().get(ConfigCodes.HOSTNAME);
    private static final int DEFAULT_PORT = Integer.parseInt(R.getAppProperties().get(ConfigCodes.PORT));


    public static void main(String[] args) {
        String hostname = DEFAULT_HOSTNAME;
        int port = DEFAULT_PORT;

//        if (args.length > 0) {
//            String[] host = args[0].split(":");
//            hostname = host[0];
//            port = Integer.parseInt(host[1]);
//        }

        /*SetupView ekkle = new SetupView("");
        ekkle.prova();*/
        try {

             Socket socket = new Socket(hostname, port);
             IConnection<String> socketConnection = new SocketConnection(socket);
             IView view = new LobbyView();
             VirtualServer virtualServer = new VirtualServer(socketConnection);
             virtualServer.startPingTask();

             view.setServer(virtualServer);

             Thread server = new Thread(virtualServer);
             server.start();

             server.join();
             System.out.println("fine main");

        } catch (IOException | InterruptedException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
