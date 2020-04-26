package it.polimi.ingsw.psp44.client;


import it.polimi.ingsw.psp44.client.cli.CLIViewSetup;
import it.polimi.ingsw.psp44.network.Connection;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.network.IVirtual;

import java.io.IOException;

public class ClientApp {
    private static String HOST = "localhost";
    private static final int PORT = 3000;


    public static void main(String[] args) {
        try {
            Connection<Message> socketConnection = new SocketConnection(HOST, PORT);
            VirtualServer virtualServer = new VirtualServer(socketConnection);
            CLIViewSetup cliViewSetup = new CLIViewSetup();
            virtualServer.run();
        } catch (IOException e) {
            System.err.println("ERROR: "+ e.getMessage());
        }

    }
}
