package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.network.Connection;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.network.IVirtual;

import java.util.HashMap;
import java.util.Map;

public class VirtualServer implements IVirtual<Message>, Runnable {

    private final Connection<Message> connection;
    private final Map<String, Runnable> router;

    public VirtualServer(Connection connection) {
        this.connection = connection;
        router = new HashMap<>();
    }


    @Override
    public void run() {
        //FIXME: Doesn't work, just an example

        while(true){
            Message message = connection.readLine();
            Message.Code code = message.getCode();
            router.get(code).run();
        }
    }

    @Override
    public void sendMessage(Message message) {
        // TODO Auto-generated method stub
    }
}