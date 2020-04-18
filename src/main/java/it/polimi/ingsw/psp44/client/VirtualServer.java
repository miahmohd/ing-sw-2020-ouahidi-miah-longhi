package it.polimi.ingsw.psp44.client;

import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.psp44.util.network.IVirtual;
import it.polimi.ingsw.psp44.network.Connection;
import it.polimi.ingsw.psp44.network.message.Message;

public class VirtualServer implements IVirtual<Message>, Runnable {

    private Connection<Message> connection;
    private IView<Message> view;
    private Map<String, Runnable> router;

    public VirtualServer(Connection<Message> connection, IView<Message> view){
        this.connection = connection;
        this.view = view;
        this.view.setVirtual(this);
        router = new HashMap<>();
    }


    @Override
    public void run() {
        //FIXME: Doesn't work, just an example
        Message message = connection.readLine();
        Message.Code code = message.getCode();
        router.get(code).run();
    }

    @Override
    public void sendMessage(Message message) {
        // TODO Auto-generated method stub
    }
}