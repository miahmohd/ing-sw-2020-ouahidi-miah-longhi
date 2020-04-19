package it.polimi.ingsw.psp44;


import it.polimi.ingsw.psp44.network.Server;

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


