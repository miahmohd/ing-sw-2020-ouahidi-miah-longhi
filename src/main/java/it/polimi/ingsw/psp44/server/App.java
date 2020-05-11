package it.polimi.ingsw.psp44.server;


import java.net.Inet4Address;
import java.net.UnknownHostException;

public class App {
    private static int port = 8080;

    public static void main(String[] args) {
        Server server = new Server(port);
        try {
            System.out.println(String.format("Server starting at %s:%d", Inet4Address.getLocalHost().getHostAddress(), port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        server.start();
    }
}


