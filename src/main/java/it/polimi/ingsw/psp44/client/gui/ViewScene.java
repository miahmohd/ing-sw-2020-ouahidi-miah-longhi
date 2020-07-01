package it.polimi.ingsw.psp44.client.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewScene extends Application {

    private static Stage stage = null;
    private static boolean isLaunched = false;

    public static void setViewAndShow(String title, String viewPath, Object controller){
        if(!isLaunched) {
            new Thread(() -> launch(ViewScene.class)).start();
        }

        FXMLLoader loader = new FXMLLoader(ViewScene.class.getResource(viewPath));

        if(controller != null)
            loader.setController(controller);

        Parent view;
        try {
            view = loader.load();
        } catch (IOException e) {
            return;
        }
        Scene scene = new Scene(view);

        //https://stackoverflow.com/questions/17850191/why-am-i-getting-java-lang-illegalstateexception-not-on-fx-application-thread
        Platform.runLater(() -> {
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        });

        isLaunched = true;
    }

    /**
     * https://stackoverflow.com/questions/30814258/javafx-pass-parameters-while-instantiating-controller-class
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        isLaunched = false;
        stage.setOnCloseRequest((windowEvent -> {
            Platform.exit();
            System.exit(0);
        }));
        stage.setResizable(false);
    }

}
