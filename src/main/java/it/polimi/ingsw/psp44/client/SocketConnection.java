package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.JsonConvert;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketConnection implements IConnection<Message> {

    private Socket socket;
    private Scanner read;
    private PrintWriter write;

    public SocketConnection(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.read = new Scanner(socket.getInputStream());
        this.write = new PrintWriter(socket.getOutputStream());
    }


    @Override
    public Message readLine() {
        String messageString = this.read.nextLine();
        Message message = JsonConvert.getInstance().fromJson(messageString, Message.class);
        return message;
    }

    @Override
    public void writeLine(Message message) {
        String messageString = JsonConvert.getInstance().toJson(message, Message.class);
        this.write.println(messageString);
        this.write.flush();
    }
}
