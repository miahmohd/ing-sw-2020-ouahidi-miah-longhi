package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.AbstractView;
import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.SocketConnection;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.ConfigCodes;
import it.polimi.ingsw.psp44.util.R;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Startup View
 */
public class StartupView {

    private static final String DEFAULT_HOSTNAME = R.getAppProperties().get(ConfigCodes.HOSTNAME);
    private static final int DEFAULT_PORT = Integer.parseInt(R.getAppProperties().get(ConfigCodes.PORT));
    private final Console console;
    private VirtualServer virtualServer;

    public StartupView() {
        this.console = new Console();
    }

    /**
     * Starts the application
     */
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
            try {
                int port = s.isEmpty() ? DEFAULT_PORT : Integer.parseInt(s);

                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(host, port), 3000);
                    connected = true;
                } catch (IOException e) {
                    console.clear();
                    console.writeLine("Connection refused, try again.");
                }
            } catch (NumberFormatException e) {
                console.clear();
                console.writeLine("Incorrect port format, try again.");
            }
        } while (!connected);

        try {

            IConnection socketConnection = new SocketConnection(socket);
            AbstractView view = new LobbyView(console);
            virtualServer = new VirtualServer(socketConnection);
            virtualServer.startPingTask();

            view.setServer(virtualServer);

            setErrorHandlers();

            Thread server = new Thread(virtualServer);
            server.start();

            server.join();

        } catch (IOException | InterruptedException e) {
            console.writeLine("ERROR: " + e.getMessage());
        }
    }

    private void setErrorHandlers() {
        virtualServer.addErrorHandler(Message.Code.NETWORK_ERROR, () -> {
            this.console.clear();
            this.console.writeLine("Network error.\nIt seems there are problems on the network,\ntry later maybe.\n");
        });

        virtualServer.addErrorHandler(Message.Code.DISCONNECTED, () -> {
            this.console.clear();
            this.console.writeLine("The server kicked you out,\nthe game was forcefully ended.\n");
        });
    }
}
