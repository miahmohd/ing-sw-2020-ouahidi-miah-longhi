package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.network.Message;

import java.util.List;

/**
 * Class representing the view of a single player.
 */
public class VirtualView implements Runnable {

    private Connection connection;
    private MessageHandler handlers;

    public VirtualView(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {

        //TODO da aggiungere:
        // - definire Message
        // - mettere in loop

        // il server/controller selezionano la vista del client (cosa il client può fare) e la virtualview rimane in attesa della risposta.
        // alla risposta il server/contoller vengono notificati con il messaggio che hanno ricevuto, loro sanno come gestirlo non la virtualview.
        // see dispensa lab_20200407
        String rawJson = this.connection.readLine();
        //todo usare GSON
        Message message = new Message(rawJson, rawJson);
        // mando la notifica
        this.handlers.handle(this, message);
    }

    public void addMessageHandler(MessageHandlerFunction handler) {
        MessageHandler newHandler = new MessageHandler(handler);
        if (this.handlers == null)
            this.handlers = newHandler;
        else
            this.handlers.setNext(newHandler);
    }

    public void sendMessage(String message) {
        this.connection.write(message);
    }

    //todo utilizzare oggetti specifici per la trasmissione TBD
    //  conversione avviene qui o da qualche altra parte?

    /**
     * Resets the interface and prepare for the start of the game/turn.
     */
    public void start() {
        // superfluo potrebbe bastare selectWorkerFrom
    }

    /**
     * Allow the player to choose a worker.
     *
     * @param playerWorkersPositions the list of workers position to choose from.
     */
    public void selectWorkerFrom(List<Position> playerWorkersPositions) {
    }

    /**
     * Allow the player to choose an action.
     *
     * @param actions the list of actions to choose from.
     */
    public void selectActionFrom(List<Action> actions) {
    }

    /**
     * Allow the player to end his turn.
     */
    public void turnEndable() {
    }

    /**
     * Ends the turn.
     */
    public void turnEnded() {
        // è superfluo come messaggio.
    }


}






























