package it.polimi.ingsw.psp44.server;


import it.polimi.ingsw.psp44.util.ConfigCodes;
import it.polimi.ingsw.psp44.util.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class App {
    private static int DEFAULT_PORT = Integer.parseInt(R.getAppProperties().get(ConfigCodes.PORT));

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0)
            port = Integer.parseInt(args[0]);

        Server server = new Server(port);

        try {
            System.out.println(String.format("Server starting at %s:%d", InetAddress.getLocalHost().getHostAddress(), port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        server.start();
    }
}


