package it.polimi.ingsw.psp44.server;


import it.polimi.ingsw.psp44.server.Server;

public class App {
    public static void main(String[] args) {
        setup();
    }

    private static void setup() {

        Server server = new Server(8080);
        System.out.println("Server started...");
        server.start();

    }


}

