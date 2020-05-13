package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.client.cli.LobbyView;
import it.polimi.ingsw.psp44.network.IConnection;
import it.polimi.ingsw.psp44.network.message.Message;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TEst extends Application {

    public void start(Stage stage) {

        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        IConnection<Message> socketConnection = new SocketConnection("localhost", 8080);
//        LobbyView view = new LobbyView();
//        VirtualServer virtualServer = new VirtualServer(socketConnection);
//
//        view.setServer(virtualServer);
//
//        Thread server = new Thread(virtualServer);
//        server.start();


        launch();
//        server.join();
    }
}
