package it.polimi.ingsw.psp44.server;

import it.polimi.ingsw.psp44.network.IConnection;

import java.io.*;

public class FileConnection implements IConnection<String> {

    private PrintWriter writer;
    private BufferedReader reader;

    public FileConnection(Reader in, Writer out) {
        writer = new PrintWriter(out, true);
        reader = new BufferedReader(in);
    }


    @Override
    public String readLine() throws IOException {
        return this.reader.readLine();
    }

    @Override
    public void writeLine(String message) {
        writer.println(message);
    }

    @Override
    public void close() {
        writer.close();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
