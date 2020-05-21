package it.polimi.ingsw.psp44.network;

import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.ConfigCodes;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Class intended for establishing a communication channel with another Virtual entity.
 */
public class Virtual {

    private static final int TIMEOUT = Integer.parseInt(R.getAppProperties().get(ConfigCodes.TIMEOUT));
    private static final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    protected IConnection<String> connection;
    private ScheduledFuture<?> scheduledFuture;


    /**
     * Create a generic instance of Virtual tha communicate with th given connection.
     * Also start a scheduled task tha repeatedly send PING
     *
     * @param connection the connection to use for sending messages
     */
    public Virtual(IConnection<String> connection) {
        this.connection = connection;
        startPingTask();
    }

    /**
     * Sends the specified message
     *
     * @param message is the formatted message
     */
    public void sendMessage(Message message) {
        String messageString = JsonConvert.getInstance().toJson(message, Message.class);
        this.connection.writeLine(messageString);
    }


    /**
     * Closes the connection, and the scheduled task that sends PING messages.
     */
    public void close() {
        this.scheduledFuture.cancel(true);
        this.connection.close();
    }

    /**
     * Starts a task that sends a PING message to the client repeatedly after TIMEOUT / 2 seconds.
     * The client is reachable if the server receives a PING message response.
     * If after TIMEOUT second the server did not receive a PING response, the socket throws a SocketTimeoutException.
     */
    private void startPingTask() {
        int delay = TIMEOUT / 2;
        Runnable task = () -> this.sendMessage(new Message(Message.Code.PING));
        this.scheduledFuture = scheduledExecutor.scheduleAtFixedRate(task, delay, delay, TimeUnit.MILLISECONDS);
    }

}