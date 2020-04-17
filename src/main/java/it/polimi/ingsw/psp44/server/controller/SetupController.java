package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.network.VirtualView;
import it.polimi.ingsw.psp44.util.network.Message;

public class SetupController {
    private Controller controller;


    public SetupController() {
        this.controller = new Controller();
    }

    public SetupController(Controller controller) {
        this.controller = controller;
    }


    public void setup() {


    }

    /**
     * Callback that handles and processes "get card" message type.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information for ending the turn
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    public Boolean getCardMessageHandler(VirtualView view, Message message) {
        if (message.getHeader() == "get card") {
            //menage request
            return true;
        }
        return false;
    }

    public void addPlayer(String nickname, VirtualView view) {
        setHandler(view);

    }
    /**
     * Arranges the message handlers for the turn management
     */
    private void setHandler(VirtualView view) {
        view.addMessageHandler(controller::getWorkerMessageHandler);
        view.addMessageHandler(setupController::getCardMessageHandler);
        //...dopo tutti gli handler
    }

}
