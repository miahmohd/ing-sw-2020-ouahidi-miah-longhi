package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.util.ConfigCodes;
import it.polimi.ingsw.psp44.util.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Represents a communication channel with the client.
 */
public class SocketConnection implements IConnection {

    private static final int TIMEOUT = Integer.parseInt(R.getAppProperties().get(ConfigCodes.TIMEOUT));

    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;


    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setSoTimeout(TIMEOUT);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String readLine() throws IOException {
        return this.reader.readLine();
    }

    public void writeLine(String msg) {
        this.writer.println(msg);
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
