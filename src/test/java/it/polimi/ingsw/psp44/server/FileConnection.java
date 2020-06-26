package it.polimi.ingsw.psp44.server;

import it.polimi.ingsw.psp44.network.IConnection;

import java.io.*;

public class FileConnection implements IConnection<String> {

    private PrintWriter writer;
    private BufferedReader reader;
    private boolean active=true;

    public FileConnection(Reader in, Writer out) {

        writer = new PrintWriter( out, true);
        reader = new BufferedReader(in);
    }


    @Override
    public String readLine() throws IOException {
        return this.reader.readLine();
    }

    @Override
    public void writeLine(String message) {
        if(!message.contains("ACTIVE_TURN")&&!message.contains("PING"))
        writer.println(message);
    }

    @Override
    public void close() {
        active=false;
        writer.close();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isActive() {
       return active;
    }
}
