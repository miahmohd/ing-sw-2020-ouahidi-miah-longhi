package it.polimi.ingsw.psp44.client.gui;


import it.polimi.ingsw.psp44.client.AbstractView;
import it.polimi.ingsw.psp44.client.VirtualServer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoView extends AbstractView implements Initializable {


    private final String infoMessage;

    @FXML
    private Text textInfo;
    @FXML
    private Button btnClose;

    public InfoView(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    @Override
    public void setServer(VirtualServer virtual) {
        this.virtualServer = virtual;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnClose.setOnAction(ae -> {
            this.virtualServer.close();
            Platform.exit();
            System.exit(0);
        });
        textInfo.setText(this.infoMessage);
    }
}
