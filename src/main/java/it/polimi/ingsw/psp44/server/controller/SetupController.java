package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.network.VirtualView;
import it.polimi.ingsw.psp44.util.network.Message;

public class SetupController {
    public void setup(Controller controller) {

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

}
