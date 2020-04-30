package it.polimi.ingsw.psp44.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Represents a communication channel with the client.
 */
public class SocketConnection implements IConnection<String> {

    private final Socket socket;

    private PrintWriter writer;
    private BufferedReader reader;


    public SocketConnection(Socket socket) {
        this.socket = socket;

        try {

            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readLine() {
        try {
            return this.reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeLine(String msg) {
        this.writer.println(msg);
    }
}
