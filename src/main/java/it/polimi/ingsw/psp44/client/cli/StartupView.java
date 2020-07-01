package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.View;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.SocketConnection;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.ConfigCodes;
import it.polimi.ingsw.psp44.util.R;

import java.io.IOException;
import java.net.Socket;

public class StartupView {

    private static final String DEFAULT_HOSTNAME = R.getAppProperties().get(ConfigCodes.HOSTNAME);
    private static final int DEFAULT_PORT = Integer.parseInt(R.getAppProperties().get(ConfigCodes.PORT));

    private VirtualServer virtualServer;
    private Console console;

    public StartupView() {
        this.console = new Console();
    }

    public void start() {
        boolean connected = false;
        Socket socket = null;


        console.writeLine("Welcome to Santorini!\n");
        do {
            console.writeLine("Server address (empty for localhost): ");
            String host = console.readLine();
            host = host.isEmpty() ? DEFAULT_HOSTNAME : host;

            console.writeLine("Server port (empty for 8080): ");
            String s = console.readLine();
            int port = s.isEmpty() ? DEFAULT_PORT : Integer.parseInt(s);

            try {
                socket = new Socket(host, port);
                connected = true;
            } catch (IOException e) {
                System.out.println("Connection refused, try again.");
            }
        } while (!connected);

        try {

            IConnection<String> socketConnection = new SocketConnection(socket);
            View view = new LobbyView(console);
            virtualServer = new VirtualServer(socketConnection);
            virtualServer.startPingTask();

            view.setServer(virtualServer);

            setErrorHandlers();

            Thread server = new Thread(virtualServer);
            server.start();

            server.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    private void setErrorHandlers() {
        virtualServer.addErrorHandler(Message.Code.NETWORK_ERROR, () -> {
            this.console.clear();
            this.console.writeLine("Network error.\nIt seems there are problems on the network,\ntry later maybe.");
        });

        virtualServer.addErrorHandler(Message.Code.DISCONNECTED, () -> {
            this.console.clear();
            this.console.writeLine("The server kicked you out,\nthe game was forcefully ended.");
        });
    }
}
