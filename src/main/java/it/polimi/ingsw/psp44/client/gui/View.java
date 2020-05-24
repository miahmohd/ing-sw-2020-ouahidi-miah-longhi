package it.polimi.ingsw.psp44.client.gui;

import it.polimi.ingsw.psp44.client.IView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class View extends Application {

    private static Stage stage = null;
    private static boolean isLaunched = false;

    public static void setViewAndShow(String title, String viewPath, IView controller){
        if(!isLaunched) {
            new Thread(() -> View.launch(View.class)).start();
        }

        FXMLLoader loader = new FXMLLoader(View.class.getResource(viewPath));
        loader.setController(controller);

        Parent view = null;
        try {
            view = loader.load();
        } catch (IOException e) {
            //TODO gestire questa cosa
            e.printStackTrace();
        }
        Scene scene = new Scene(view);

        //https://stackoverflow.com/questions/17850191/why-am-i-getting-java-lang-illegalstateexception-not-on-fx-application-thread
        Platform.runLater(() -> {
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        });
    }


    /**
     * https://stackoverflow.com/questions/30814258/javafx-pass-parameters-while-instantiating-controller-class
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        isLaunched = false;
    }
}
