package it.polimi.ingsw.psp44;


import it.polimi.ingsw.psp44.network.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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


