package it.polimi.ingsw.psp44.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LobbyView extends Application{

    private static LobbyController controller;

    public static void setController(LobbyController lobbyController) {
        controller = lobbyController;
    }

    /**
     * https://stackoverflow.com/questions/30814258/javafx-pass-parameters-while-instantiating-controller-class
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/lobby.fxml"));

        loader.setController(controller);
        Parent parentView = loader.load();
        Scene scene = new Scene(parentView);

        primaryStage.setTitle("Lobby View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }





}
