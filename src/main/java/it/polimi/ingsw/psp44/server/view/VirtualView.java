package it.polimi.ingsw.psp44.server.view;

import it.polimi.ingsw.psp44.network.SocketConnection;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHandlerFunction;
import it.polimi.ingsw.psp44.network.message.MessageRouter;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.network.IVirtual;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class representing the view of a single player.
 */
public class VirtualView implements Runnable, IVirtual<Message> {

    private final SocketConnection connection;
    private final MessageRouter router;
    private final ExecutorService executor;

    public VirtualView(SocketConnection connection) {
        this.connection = connection;
        this.router = new MessageRouter();
        this.executor = Executors.newFixedThreadPool(100);
    }

    @Override
    public void run() {
        //TODO da aggiungere:
        // - sistemare in loop

        while (true) {
            String rawJson = this.connection.readLine();
            Message message = JsonConvert.getInstance().fromJson(rawJson, Message.class);
            this.router.route(this, message);
            //TODO: Make it better
            /*
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    VirtualView.this.router.route(VirtualView.this, message);
                }
            });
            */

        }
    }

    public void addMessageHandler(Message.Code code, MessageHandlerFunction handler) {
        this.router.addRoute(code, handler);
    }



    @Override
    public void sendMessage(Message message) {
        String messageString = JsonConvert.getInstance().toJson(message, Message.class);
        connection.writeLine(messageString);
    }
}






























